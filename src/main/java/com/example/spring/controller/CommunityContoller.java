package com.example.spring.controller;

import com.example.spring.dao.*;
import com.example.spring.repository.*;
import com.example.spring.utils.JwtTokenProvider;
import com.querydsl.core.Tuple;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public HashMap community(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();
        int nowpage = Integer.parseInt(data.get("nowpage").toString());
        int countpage = Integer.parseInt(data.get("count").toString());
        String category = data.get("category").toString();

        int start = countpage*nowpage;
        int end = countpage*(nowpage+1) - 1;

        log.info("start : {}", start);
        log.info("end : {}", end);

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        try{
            List<CommunityTb> communityTbList  =  communityRepository.getCommunity();
            List<TestContent> communityTbListByCategory  =  communityRepository.getCommunityByCategory(category);

            System.out.println("size : " + communityTbList.size());

            List<CategoryTb> categoryTb = categoryRepository.findAll();

            HashMap test = new HashMap();
            for(int i=0;i<categoryTb.size();i++){

                int category_size = 0;
                for(int j=0;j<communityTbList.size();j++){
                    if(communityTbList.get(j).getCategory().equals(categoryTb.get(i).getCategory_name())){
                        category_size++;
                    }
                }
                test.put("total", communityTbList.size());
                test.put(categoryTb.get(i).getCategory_name(), category_size);
            }

            if(end>=communityTbListByCategory.size()) end = communityTbListByCategory.size()-1;
            List<TestContent> communityTbListByorder = new ArrayList<>();

            for(int i=start;i<=end;i++){
                communityTbListByorder.add(communityTbListByCategory.get(i));
            }
            result.put("count_list", test);
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
        String filename = null;


        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
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
                    user.setCoin(user.getCoin() - 1);
                }else{
                    result.put("message", "1 coin lack");
                    result.put("resultCode", "false");
                    return result;
                }
            }

            LocalDate now = LocalDate.now();
            CommunityTb communityTb = new CommunityTb();
            communityTb.setTitle(title);
            communityTb.setContent(content);
            communityTb.setUser_id(user.getUser_id());
            communityTb.setDate(now);
            communityTb.setHighlight(highlight);
            communityTb.setCategory(category);
            communityTb.setComment_allow(comment_allow);
            communityRepository.save(communityTb);

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
    public HashMap community_detail(@PathVariable("community_id") int community_id , @RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        try{

            CommunityTb community = communityRepository.getCommunityById(community_id);
            List<CommentTb> commentTbList = commentRepository.getCommentList(community_id);

            communityRepository.Increase_like(community);
            result.put("community", community);
            result.put("comment", commentTbList);
            result.put("resultCode", "true");
            return result;

        }catch(Exception e){
            System.out.println("db error");
            System.out.println(e);
            result.put("message", "page null");
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

            if(user.getUser_id() == community_writer){

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
        String title = data.get("title").toString();
        String content = data.get("content").toString();
        boolean highlight = (boolean) data.get("highlight");
        String category =data.get("category").toString();
        boolean comment_allow = (boolean) data.get("comment_allow");
        log.info("title : {}", title);
        log.info("content : {}", content);

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }
        try{
            CommunityTb communityTb = communityRepository.getCommunityById(community_id);
            int user_id = jwtTokenProvider.getUserId(tokenHeader);

            if(user_id == communityTb.getUser_id()){
                //원래는 하이라이트를 안했다가, 하이라이트를 추가한 경우 코인 삭감
                if(!communityTb.isHighlight() && highlight){
                    UserTb userTb = userRepository.getUserTbByUserId(user_id);
                    userTb.setCoin(userTb.getCoin() - 1);
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

        if(likeRepository.LikeCheck(likeTb)){

            likeRepository.save(likeTb);
            result.put("message", "pushed");
            result.put("resultCode", "true");

        }else{
            LikeTb heart = likeRepository.getLike(likeTb);
            likeRepository.delete(heart);
            result.put("message", "unpushed");
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

        double coin = (double) data.get("coin");
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
            communityRepository.save(communityTb);
            UserTb writer = userRepository.getUserTbByUserId(communityTb.getUser_id());


            writer.setCoin(writer.getCoin() + coin);
            userRepository.save(writer);

            UserTb writerUser = userRepository.getUserTbByUserId(writer.getUser_id());
            writerUser.setCoin(writerUser.getCoin() + coin);
            userRepository.save(writerUser);
            Donator.setCoin(Donator.getCoin() - coin);
            userRepository.save(Donator);

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
                userTb.setCoin(userTb.getCoin() - 1);
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


}
