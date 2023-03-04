package com.example.spring.controller;

import com.example.spring.dao.*;
import com.example.spring.dto.*;
import com.example.spring.repository.*;
import com.example.spring.utils.JwtTokenProvider;
import com.querydsl.core.Tuple;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
//@Controller
public class CommunityContoller {

    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommentlikeRepository commentlikeRepository;

    @Autowired
    private CoinPushHistoryRepository coinPushHistoryRepository;

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;



    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ApiOperation(value = "게시판", notes = "게시글을 보여주는 게시판 페이지")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowpage", value = "현재 페이지 번호", required = true),
            @ApiImplicitParam(name = "count", value = "보여줄 게시글 갯수", required = true),
            @ApiImplicitParam(name = "category", value = "카테고리", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode, community")
    })
    @RequestMapping(value = "/community", method = RequestMethod.GET)
    public HashMap community(@RequestParam ("nowpage") int nowpage, @RequestParam ("count") int countpage, @RequestParam ("category") String category, @RequestHeader(value = "token", required = false) String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        int start = countpage * nowpage;
        int end = countpage*(nowpage+1) - 1;

        log.info("start : {}", start);
        log.info("end : {}", end);
        log.info("nowpage : {}", nowpage);
        log.info("countpage : {}", countpage);
        log.info("category : {}", category);


        try{

            List<TestContent> communityTbListByCategory  =  communityRepository.getCommunityByCategory(category);
            System.out.println("size : " + communityTbListByCategory.size());

            System.out.println(communityTbListByCategory);

            if(end>=communityTbListByCategory.size()) end = communityTbListByCategory.size()-1;
            List<CommunityWriterDtoWithoutContent> communityTbListByorder = new ArrayList<>();
            for(int i=start;i<=end;i++){
                String name = userRepository.getNameByPk(communityTbListByCategory.get(i).getUser_id());

                int total_like = likeRepository.getLikeTotal(communityTbListByCategory.get(i).getCommunity_id());
                double total_reward = communityTbListByCategory.get(i).getHits() + communityTbListByCategory.get(i).getGet_coin() + total_like;

                CommunityWriterDtoWithoutContent communityWriterDto = new CommunityWriterDtoWithoutContent(communityTbListByCategory.get(i), name, total_like, total_reward);
                communityTbListByorder.add(communityWriterDto);
            }

            result.put("total", communityTbListByCategory.size());
            result.put("community", communityTbListByorder);
            result.put("resultCode", "true");
            return result;

        }catch (Exception e){
            log.info("{}", e);
            log.info("/community error");
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }
    }
    
    @ApiOperation(value = "게시글 쓰기", notes = "게시글 쓰는 페이지")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "제목", required = true),
            @ApiImplicitParam(name = "content", value = "본문", required = true),
            @ApiImplicitParam(name = "highlight", value = "글 하이라이트 여부", type = "boolean"),
            @ApiImplicitParam(name = "category", value = "카테고리", type ="varchar"),
            @ApiImplicitParam(name = "comment_allow", value = "댓글 허용 미허용", required = true, dataType = "boolean"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/community", method = RequestMethod.POST)
    public HashMap community_save(@RequestHeader("token") String tokenHeader, @RequestBody HashMap<String,Object> data) throws IOException{

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("title"))){
            result.put("message", "title is null");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("content"))){
            result.put("message", "content is null");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("highlight"))){
            result.put("message", "highlight is null");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("category"))){
            result.put("message", "category is null");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("comment_allow"))){
            result.put("message", "comment_allow is null");
            result.put("resultCode", "false");
            return result;
        }


        String title = data.get("title").toString();
        String content = data.get("content").toString();
        boolean highlight = (boolean) data.get("highlight");
        String category = data.get("category").toString();
        boolean comment_allow = (boolean) data.get("comment_allow");


        log.info("jwt : {} ", tokenHeader);
        log.info("title {} ", title);
        log.info("content {} ", content);

        try{
            UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));

            if(highlight){
                if(user.getCoin() >= 1){

                    BigDecimal number1 = BigDecimal.valueOf (user.getCoin());
                    BigDecimal number2 = BigDecimal.valueOf(1);

                    user.setCoin(number1.subtract(number2).doubleValue());

                }else{
                    result.put("message", "1 coin lack");
                    result.put("resultCode", "false");
                    return result;
                }
            }

            Date now = new Date();
            CommunityTb communityTb = new CommunityTb();
            communityTb.setTitle(title);
            communityTb.setContent(content);
            communityTb.setUser_id(user.getUser_id());
            communityTb.setDate(now);
            communityTb.setHighlight(highlight);
            communityTb.setCategory(category);
            communityTb.setComment_allow(comment_allow);
            communityRepository.save(communityTb);

            result.put("pk", communityRepository.getCommunBylast().getCommunity_id());
            result.put("resultCode", "true");
            return result;
        }catch (Exception e){
            log.info("/community/write error");
            result.put("message", "not exist user");
            result.put("resultCode", "false");
            return result;
        }

    }

    @ApiOperation(value = "게시글 상세 페이지", notes = "제목 뿐만 아니라 본문이 있는 페이지")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/community/{community_id}", method = RequestMethod.GET)
    public HashMap community_detail(@PathVariable("community_id") int community_id, @RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();
        boolean likeResult = false;
        if(!tokenHeader.isEmpty()){

            if(!jwtTokenProvider.validateToken(tokenHeader)){
                result.put("message", "Token validate");
                result.put("resultCode", "false");
                return result;
            }

            System.out.println("token on");
            LikeTb like = new LikeTb();
            like.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
            like.setCommunity_id(community_id);

            likeResult = likeRepository.LikeCheck(like);

            System.out.println(likeRepository.LikeCheck(like));

        }else{
            System.out.println("token off");
            LikeTb like = new LikeTb();
            likeResult = false;
        }

        try{
            CommunityTb community = communityRepository.getCommunityById(community_id);

            UserTb user = userRepository.getUserTbByUserId(community.getUser_id());

            List<CommentTb> commentTbList = commentRepository.getCommentList(community_id);
            List<CommentWriterDto> commentWriterDto_list = new ArrayList<>();


            String name, recomment_name;
            boolean my_like = false, recomment_like;
            int like_total, recomment_like_total;

            System.out.println("commentTbList Size : " + commentTbList.size());
            for(int i=0;i<commentTbList.size();i++){
//                CommunityWriterDtoWithoutContent communityWriterDto = new CommunityWriterDtoWithoutContent(communityTbListByCategory.get(i), userRepository.getNameByPk(communityTbListByCategory.get(i).getUser_id()));
                name = userRepository.getUserTbByUserId(commentTbList.get(i).getUser_id()).getName();
                like_total = commentlikeRepository.getCommentLike_total(commentTbList.get(i).getComment_id());

                CommentLikeTb commentLikeTb = new CommentLikeTb();
                commentLikeTb.setComment_id(commentTbList.get(i).getComment_id());
                commentLikeTb.setCommunity_id(community_id);
                if(!tokenHeader.isEmpty()) {
                    commentLikeTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
                    my_like = commentlikeRepository.CommentLikeCheckByAll(commentLikeTb);
                }


                List<CommentTb> reply = commentRepository.getRecommentByCommentId(commentTbList.get(i).getComment_id());
                List<RecommentDto> reCommentWriterDto_list = new ArrayList<>();
                System.out.println("reply Size : " + reply.size());

                for(int j=0;j<reply.size();j++){

                    CommentLikeTb recommentLikeTb = new CommentLikeTb();
                    recommentLikeTb.setComment_id(reply.get(j).getComment_id());
                    recommentLikeTb.setCommunity_id(community_id);
                    if(!tokenHeader.isEmpty()) {
                        recommentLikeTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
                    }


                    recomment_name = userRepository.getUserTbByUserId(reply.get(j).getUser_id()).getName();
                    recomment_like = commentlikeRepository.CommentLikeCheckByAll(recommentLikeTb);
                    recomment_like_total = commentlikeRepository.getCommentLike_total(reply.get(j).getComment_id());
                    RecommentDto recommentDto = new RecommentDto(reply.get(j), recomment_name, recomment_like, recomment_like_total);
                    reCommentWriterDto_list.add(recommentDto);
                }
                CommentWriterDto commentWriterDto = new CommentWriterDto(commentTbList.get(i), name, my_like, like_total, reCommentWriterDto_list);
                System.out.println("test : " + my_like);
                commentWriterDto_list.add(commentWriterDto);
            }

            communityRepository.Increase_like(community);

            BigDecimal bigNumber1 = BigDecimal.valueOf(community.getTotal_reward());
            BigDecimal bigNumber2 = BigDecimal.valueOf(1);
            community.setTotal_reward(bigNumber1.add(bigNumber2).doubleValue());
            communityRepository.save(community);

            CommentLikeTb commentLikeTb = new CommentLikeTb();
            commentLikeTb.setCommunity_id(community_id);


            if(!tokenHeader.isEmpty()) {
                commentLikeTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
            }



            int get_likeTotal = likeRepository.getLikeTotal(community_id);
            int comment_total = commentRepository.getCommentListSize(community_id);
            CommunityWriterDto communityWriterDto = new CommunityWriterDto(community, user.getName(), likeResult, get_likeTotal, comment_total);

            result.put("community", communityWriterDto);
            result.put("comment", commentWriterDto_list);
            result.put("resultCode", "true");
            return result;

        }catch(Exception e){
            System.out.println(e);
            result.put("message", "");
            result.put("resultCode", "false");
            return result;
        }
    }


    @ApiOperation(value = "게시글 삭제", notes = "게시글 번호로 게시글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode, community, comment")
    })
    @RequestMapping(value= "/community/{community_id}", method = RequestMethod.DELETE)
    public HashMap community_delete(@PathVariable("community_id") int community_id, @RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        try{
            UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));

            CommunityTb community = communityRepository.getCommunityById(community_id);

            int community_writer = community.getUser_id();

            int user_id = jwtTokenProvider.getUserId(tokenHeader);
            String user_role = userRepository.getRoleByUserId(user_id);

            if(user.getUser_id() == community_writer || user_role.equals("admin")){

                communityRepository.deleteById(community_id);
                commentRepository.deleteByCommunityId(community_id);
                result.put("resultCode", "true");

            } else {
                log.info("unauthorized");
                result.put("message","unauthorized");
                result.put("resultCode", "false");
            }
        }catch(Exception e){
            result.put("message", "don't exist community");
            result.put("resultCode", "false");
        }

        return result;
    }
    
    @ApiOperation(value = "게시글 수정", notes = "게시글 번호로 게시글 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "community_id", value = "게시글 id", required = true),
            @ApiImplicitParam(name = "title", value = "변경할 제목", required = true),
            @ApiImplicitParam(name = "content", value = "변경할 본문", required = true),
            @ApiImplicitParam(name = "highlight", value = "하이트라이트 여부", required = true),
            @ApiImplicitParam(name = "category", value = "카테고리", required = true),
            @ApiImplicitParam(name = "comment_allow", value = "댓글 허용 미허용", required = true, dataType = "boolean"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })

    @RequestMapping(value = "/community/{community_id}", method = RequestMethod.PATCH)
    public HashMap community_modify(@PathVariable ("community_id") int community_id,@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) throws IOException {

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("title"))){
            result.put("message", "title is null");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("content"))){
            result.put("message", "content is null");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("highlight"))){
            result.put("message", "highlight is null");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("category"))){
            result.put("message", "category is null");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("comment_allow"))){
            result.put("message", "comment_allow is null");
            result.put("resultCode", "false");
            return result;
        }

        String title = data.get("title").toString();
        String content = data.get("content").toString();
        boolean highlight = (boolean) data.get("highlight");
        String category =data.get("category").toString();
        boolean comment_allow = (boolean) data.get("comment_allow");
        log.info("title : {}", title);
        log.info("content : {}", content);


        try{
            CommunityTb communityTb = communityRepository.getCommunityById(community_id);
            int user_id = jwtTokenProvider.getUserId(tokenHeader);
            String user_role = userRepository.getRoleByUserId(user_id);
            if(user_id == communityTb.getUser_id() || user_role.equals("admin")){
                //원래는 하이라이트를 안했다가, 하이라이트를 추가한 경우 코인 삭감
                if(!communityTb.isHighlight() && highlight){
                    UserTb userTb = userRepository.getUserTbByUserId(user_id);
                    BigDecimal bigNumber3 = BigDecimal.valueOf(userTb.getCoin());
                    BigDecimal bigNumber4 = BigDecimal.valueOf(1);

                    userTb.setCoin(bigNumber3.subtract(bigNumber4).doubleValue());

                    communityTb.setTitle(title);
                    communityTb.setContent(content);
                    communityTb.setCategory(category);
                    communityTb.setHighlight(highlight);
                    communityTb.setComment_allow(comment_allow);
                    communityRepository.updateCommunity(communityTb);
                    result.put("message", "coin pay");
                    result.put("resultCode", "true");
                }else{
                    communityTb.setTitle(title);
                    communityTb.setContent(content);
                    communityTb.setCategory(category);
                    communityTb.setComment_allow(comment_allow);
                    communityRepository.updateCommunity(communityTb);
                    result.put("resultCode", "true");
                }

            }else{
                result.put("message", "unauthorized");
                result.put("resultCode", "false");
            }

        }catch (Exception e){
            result.put("message", "don't exist community");
            result.put("resultCode", "false");
        }


        return result;
    }

    @ApiOperation(value = "게시글 좋아요", notes = "게시글 좋아요 버튼")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/community/{community_id}", method = RequestMethod.PUT)
    public HashMap heart_push(@PathVariable ("community_id") int community_id, @RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        log.info("community_id : {}", community_id);

        LikeTb likeTb = new LikeTb();
        likeTb.setCommunity_id(community_id);
        likeTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));

        CommunityTb communityTb = communityRepository.getCommunityById(community_id);
        if(likeRepository.LikeCheck(likeTb)){

            LikeTb heart = likeRepository.getLikeByUserIdAndCommunityId(likeTb);

//            System.out.println("test" + heart.getLike_id());
            likeRepository.deleteById(heart.getLike_id());
            BigDecimal bigNumber1 = BigDecimal.valueOf(communityTb.getTotal_reward());
            BigDecimal bigNumber2 = BigDecimal.valueOf(1);
            communityTb.setTotal_reward(bigNumber1.subtract(bigNumber2).doubleValue());
            communityRepository.save(communityTb);

            result.put("message", "unpushed");
            result.put("resultCode", "true");
        }else{

            BigDecimal bigNumber1 = BigDecimal.valueOf(communityTb.getTotal_reward());
            BigDecimal bigNumber2 = BigDecimal.valueOf(1);
            communityTb.setTotal_reward(bigNumber1.add(bigNumber2).doubleValue());
            communityRepository.save(communityTb);
            likeRepository.save(likeTb);
            result.put("message", "pushed");
            result.put("resultCode", "true");
        }
        return result;

    }

    @ApiOperation(value = "응원하기", notes = "게시글 작성자에게 응원하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coin", value = "응원할 코인 금액", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/community/{community_id}", method = RequestMethod.POST)
    public HashMap coin_push(@PathVariable ("community_id") int community_id, @RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("coin"))){
            result.put("message", "coin is null");
            result.put("resultCode", "false");
            return result;
        }

        double coin = Double.parseDouble(data.get("coin").toString());

        if(coin <= 0){
            result.put("message", "can't be less than 0");
            result.put("resultCode", "false");
            return result;
        }
        //사용자가 후원할 코인이 있는지 확인해야함
        UserTb Donator = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        CommunityTb communityTb = communityRepository.getCommunityById(community_id);

        if(Donator.getUser_id() == communityTb.getUser_id()){
            result.put("message", "can't donate myself");
            result.put("resultCode", "false");
            return result;
        }

        if(Donator.getCoin() >= coin){
            //커뮤니티의 사용자에게 코인을 주는 로직

            communityTb.setGet_coin(communityTb.getGet_coin() + coin);
            BigDecimal Number1 = BigDecimal.valueOf(communityTb.getTotal_reward());
            BigDecimal Number2 = BigDecimal.valueOf(coin);
            communityTb.setTotal_reward(Number1.add(Number2).doubleValue());
            communityRepository.save(communityTb);

            UserTb writerUser = userRepository.getUserTbByUserId(communityTb.getUser_id());
            BigDecimal bigNumber1 = BigDecimal.valueOf(writerUser.getCoin());
            BigDecimal bigNumber2 = BigDecimal.valueOf(coin);
            BigDecimal value = bigNumber1.add(bigNumber2);
            writerUser.setCoin(value.doubleValue());

            userRepository.save(writerUser);

            BigDecimal bigNumber3 = BigDecimal.valueOf(Donator.getCoin());
            BigDecimal bigNumber4 = BigDecimal.valueOf(coin);
            Donator.setCoin(bigNumber3.subtract(bigNumber4).doubleValue());
            userRepository.save(Donator);

            Date now = new Date();
            CoinPushHistoryTb coinPushHistoryTb = new CoinPushHistoryTb();
            coinPushHistoryTb.setCoin(coin);
            coinPushHistoryTb.setDate(now);
            coinPushHistoryTb.setGiver(Donator.getUser_id());
            coinPushHistoryTb.setReceiver(writerUser.getUser_id());

            coinPushHistoryRepository.save(coinPushHistoryTb);

            result.put("resultCode", "true");
            return result;
        }else{
            result.put("message", "coin lack");
            result.put("resultCode", "false");
            return result;
        }

    }

    @ApiOperation(value = "게시글 하이라이트 추가", notes = "게시글이 작성된 후 하이라이트를 추가할 때 사용")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/community/highlight/{community_id}", method = RequestMethod.PUT)
    public HashMap coin_push(@PathVariable ("community_id") int community_id, @RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }



        try{
            UserTb userTb = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
            if(userTb.getCoin() >= 1){
                BigDecimal bigNumber1 = BigDecimal.valueOf(userTb.getCoin());
                BigDecimal bigNumber2 = BigDecimal.valueOf(1);

                userTb.setCoin(bigNumber1.subtract(bigNumber2).doubleValue());
                CommunityTb communityTb = communityRepository.getCommunityById(community_id);
                communityTb.setHighlight(true);
                communityRepository.save(communityTb);
                result.put("resultCode", "true");

            }else{
                result.put("message", "1 coin lack");
                result.put("resultCode", "false");
            }

            return result;

        }catch (Exception e){

            result.put("resultCode", "false");
            result.put("message", "db error");
            return result;

        }

    }

    @ApiOperation(value = "내 게시글 확인", notes = "내 게시글 확인")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowpage", value = "현재 페이지 번호", required = true),
            @ApiImplicitParam(name = "count", value = "보여줄 게시글 갯수", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/my-community", method = RequestMethod.GET)
    public HashMap my_community(@RequestParam ("nowpage") int nowpage, @RequestParam ("count") int countpage, @RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        int start = countpage * nowpage;
        int end = countpage*(nowpage+1) - 1;


        System.out.println("start : " + start);
        System.out.println("end : " + end);
        try{

            List<CommunityTb> mycommunity = communityRepository.getCommunityListByUserId(jwtTokenProvider.getUserId(tokenHeader));
            List<MyCommunityDto> communityDtoList = new ArrayList<>();

            if(end>=mycommunity.size()) end = mycommunity.size()-1;

            int like_total;
            double total_reward;
            String user_name;
            int comment_total;


            for(int i=start;i<=end;i++){
                comment_total = commentRepository.getCommentListSize(mycommunity.get(i).getCommunity_id());
                like_total = likeRepository.getLikeTotal(mycommunity.get(i).getCommunity_id());
                total_reward = mycommunity.get(i).getHits() + mycommunity.get(i).getGet_coin() + like_total;
                user_name = userRepository.getNameByPk(jwtTokenProvider.getUserId(tokenHeader));
                MyCommunityDto dto = new MyCommunityDto(mycommunity.get(i), comment_total, like_total, total_reward, user_name);
                communityDtoList.add(dto);
            }
            result.put("total", mycommunity.size());
            result.put("community", communityDtoList);
            result.put("resultCode", "true");
            return result;

        }catch (Exception e){

            result.put("resultCode", "false");
            result.put("message", "db error");
            return result;

        }

    }

    @ApiOperation(value = "최근 게시글 조회", notes = "최근 게시글 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "count", value = "보여줄 게시글 갯수", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/community-recently", method = RequestMethod.GET)
    public HashMap my_community(@RequestParam int count){

        HashMap<String, Object> result = new HashMap<>();

        if(ObjectUtils.isEmpty(count)){
            result.put("message", "count is null");
            result.put("resultCode", "false");
            return result;
        }

        try{
            int like_total;
            double total_reward;
            String user_name;
            int comment_total;

            System.out.println("test");
            List<CommunityTb> communityList = communityRepository.getCommunityBylimit(count);

            System.out.println(communityList);

            List<MyCommunityDto> communityDtoList = new ArrayList<>();
            for(int i=0;i< communityList.size();i++){

                comment_total = commentRepository.getCommentListSize(communityList.get(i).getCommunity_id());
                like_total = likeRepository.getLikeTotal(communityList.get(i).getCommunity_id());
                total_reward = communityList.get(i).getHits() + communityList.get(i).getGet_coin() + like_total;
                user_name = userRepository.getNameByPk(communityList.get(i).getUser_id());
                MyCommunityDto dto = new MyCommunityDto(communityList.get(i), comment_total, like_total, total_reward, user_name);
                communityDtoList.add(dto);

            }

            result.put("community", communityDtoList);
            result.put("resultCode", "true");
            return result;

        }catch (Exception e){
            log.info("{}", e);
            result.put("resultCode", "false");
            result.put("message", "db error");
            return result;

        }

    }

    @ApiOperation(value = "인기 게시글", notes = "인기 게시글 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "count", value = "보여줄 게시글 갯수", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/community-popularity", method = RequestMethod.GET)
    public HashMap popularity_community(@RequestParam int count){

        HashMap<String, Object> result = new HashMap<>();

        if(ObjectUtils.isEmpty(count)){
            result.put("message", "count is null");
            result.put("resultCode", "false");
            return result;
        }

        try{
            int like_total;
            double total_reward;
            String user_name;
            int comment_total;

            System.out.println("test");
            List<CommunityTb> communityList = communityRepository.getPoppularCommunityBylimit(count);

            System.out.println(communityList);

            List<MyCommunityDto> communityDtoList = new ArrayList<>();
            for(int i=0;i< communityList.size();i++){

                comment_total = commentRepository.getCommentListSize(communityList.get(i).getCommunity_id());
                like_total = likeRepository.getLikeTotal(communityList.get(i).getCommunity_id());
                total_reward = communityList.get(i).getHits() + communityList.get(i).getGet_coin() + like_total;
                user_name = userRepository.getNameByPk(communityList.get(i).getUser_id());
                MyCommunityDto dto = new MyCommunityDto(communityList.get(i), comment_total, like_total, total_reward, user_name);
                communityDtoList.add(dto);

            }

            result.put("community", communityDtoList);
            result.put("resultCode", "true");
            return result;

        }catch (Exception e){
            log.info("{}", e);
            result.put("resultCode", "false");
            result.put("message", "db error");
            return result;

        }

    }

    @ApiOperation(value = "응원 받은 코인", notes = "응원 받은 코인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/chart/coin", method = RequestMethod.GET)
    public HashMap get_coin(@RequestHeader("token") String tokenHeader){

        log.info("{}", "/chart/coin");
        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        try{

            List<CoinPushHistoryTb> list = coinPushHistoryRepository.getCoinPushHistoryByUserId(jwtTokenProvider.getUserId(tokenHeader));

            if(list.size() == 0){
                result.put("message", "empty get coin");
                result.put("resultCode", "true");
                return result;
            }

            List<CoinpushDto> listDto = new ArrayList<>();
            for(int i=0;i<list.size();i++){
                CoinpushDto dto = new CoinpushDto(list.get(i));
                listDto.add(dto);
            }
            List<String> labels = new ArrayList<>();
            List<String> data = new ArrayList<>();
            BigDecimal coin = new BigDecimal("0");

            coin = BigDecimal.valueOf(listDto.get(0).getCoin());

            for(int i=1;i<list.size();i++){
                if(Objects.equals(listDto.get(i-1).getDate(), listDto.get(i).getDate())){
                    BigDecimal number2 = BigDecimal.valueOf(listDto.get(i).getCoin());
                    coin = coin.add(number2);

                    if(i == list.size()-1){
                        data.add(coin.toString());
                        labels.add(listDto.get(i-1).getDate());
                        System.out.println("test");
                    }

                }else{
                    data.add(coin.toString());
                    labels.add(listDto.get(i-1).getDate());
                    coin = BigDecimal.valueOf(listDto.get(i).getCoin());

                    if(i == list.size()-1){
                        data.add(String.valueOf(listDto.get(i).getCoin()));
                        labels.add(listDto.get(i).getDate());
                    }
                }
            }

            //일별로 합쳐야할거같은데
            result.put("labels", labels);
            result.put("datasets", data);
            result.put("resultCode", "true");
            return result;

        }catch (Exception e){
            log.info("{}", e);
            result.put("resultCode", "false");
            result.put("message", "db error");
            return result;

        }

    }

    @ApiOperation(value = "내가 쓴 글 차트", notes = "내가 쓴 글 차트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/chart/write", method = RequestMethod.GET)
    public HashMap get_write_chart(@RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        try{

            List<TestContent> list = communityRepository.getCommunityListByUserIdAndDate(jwtTokenProvider.getUserId(tokenHeader));

            System.out.println("list size : " + list.size());
            if(list.size() == 0){
                result.put("message", "empty community");
                result.put("resultCode", "true");
                return result;
            }
            List<WriteChartDto> chart = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            List<String> data = new ArrayList<>();

            for(int i=0;i<list.size();i++) {
                WriteChartDto dto = new WriteChartDto(list.get(i));
                chart.add(dto);
            }

            String date = chart.get(0).getDate();
            labels.add(date);

            int community_count = 1;
            for(int i=1;i<list.size();i++) {
                if(Objects.equals(chart.get(i-1).getDate(), chart.get(i).getDate())) {
                    community_count++;

                    if(i == list.size()-1){
                        data.add(String.valueOf(community_count));
                    }
                }else {
                    data.add(String.valueOf(community_count));
                    labels.add(chart.get(i).getDate());
                    community_count = 1;

                    if(i == list.size()-1){
                        data.add(String.valueOf(community_count));
                    }
                }
            }
            result.put("labels", labels);
            result.put("datasets", data);
            result.put("resultCode", "true");
            return result;

        }catch (Exception e ){
            log.info("{}", e);
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }
    }



}
