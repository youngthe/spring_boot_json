package com.example.spring.controller;

import com.example.spring.dao.*;
import com.example.spring.dto.CommentWriterDto;
import com.example.spring.dto.CommunityWriterDto;
import com.example.spring.dto.MyCommunityDto;
import com.example.spring.dto.RecommentDto;
import com.example.spring.repository.*;
import com.example.spring.utils.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CommentlikeRepository commentlikeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private LikeRepository likeRepository;


    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());


    @ApiOperation(value = "댓글 삭제", notes = "게시글에 작성된 댓글 삭제, * 대댓글 포함 *")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/comments/{comment_id}", method = RequestMethod.DELETE)
    public HashMap comment_delete(@PathVariable ("comment_id") int comment_id, @RequestHeader("token") String tokenHeader){
        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        try{
            CommentTb commentTb = commentRepository.getCommentByCommentId(comment_id);

            int user_id = jwtTokenProvider.getUserId(tokenHeader);
            String user_role = userRepository.getRoleByUserId(user_id);
            if(commentTb.getUser_id() == jwtTokenProvider.getUserId(tokenHeader) || user_role.equals("admin")){

                commentRepository.deleteById(comment_id);
                commentRepository.deleteByParent(comment_id);
                result.put("resultCode", "true");

            }else{
                result.put("message", "unauthorized");
                result.put("resultCode", "false");
            }

            return result;

        }catch(Exception e){
            System.out.println(e);
            System.out.println("can't delete comment");
            log.info("{}", e);
            log.info("can't delete comment");
            result.put("message", "don't exist");
            result.put("resultCode", "false");
            return result;
        }
    }

    @ApiOperation(value = "댓글 작성", notes = "게시글에 댓글 작성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comment", value = "댓글 내용", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/comments/{community_id}", method = RequestMethod.POST)
    public HashMap comment_write(@PathVariable ("community_id") int community_id, @RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {



        HashMap<String, Object> result = new HashMap<>();

        if(ObjectUtils.isEmpty(data.get("comment"))){
            result.put("message", "comment is null");
            result.put("resultCode", "false");
            return result;
        }

        String comment = data.get("comment").toString();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }
        log.info("community_id : {}", community_id);
        log.info("comment : {}", comment);

        try{
            Date now = new Date();
            CommentTb commentTb = new CommentTb();
            commentTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
            commentTb.setCommunity_id(community_id);
            commentTb.setComment(comment);
            commentTb.setDate(now);
            commentRepository.save(commentTb);

            result.put("comment_id", commentTb.getComment_id());
            result.put("resultCode", "true");
            return result;

        } catch (Exception e){
            log.info("/community/comments/{community_id} error");
            result.put("resultCode", "false");
            return result;
        }

    }

    @ApiOperation(value = "대댓글 작성", notes = "게시글에 작성된 댓글에 댓글을 다는 엔드포인트")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comment", value = "대댓글 내용", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/recomments/{comment_id}", method = RequestMethod.POST)
    public HashMap recomment_write(@PathVariable ("comment_id") int comment_id, @RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {


        HashMap<String, Object> result = new HashMap<>();

        if(ObjectUtils.isEmpty(data.get("comment"))){
            result.put("message", "comment is null");
            result.put("resultCode", "false");
            return result;
        }

        String comment = data.get("comment").toString();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }


        log.info("jwt : {}", tokenHeader);
        log.info("comment : {}", comment);

        Date now = new Date();

        try{
            int communityId = commentRepository.getCommunityIdByCommentId(comment_id);
            CommentTb commentTb = new CommentTb();
            commentTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
            commentTb.setCommunity_id(communityId);
            commentTb.setComment(comment);
            commentTb.setDate(now);
            commentTb.setParent(comment_id);

            commentRepository.save(commentTb);

            result.put("comment_id", commentTb.getComment_id());
            result.put("resultCode", "true");
            return result;
        }catch (Exception e){
            result.put("resultCode", "false");
            return result;
        }

    }

    @ApiOperation(value = "댓글 수정", notes = "게시글에 작성된 댓글 수정, * 대댓글 포함 *")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comment", value = "수정할 대댓글 내용", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/comments/{comment_id}", method = RequestMethod.PATCH)
    public HashMap comment_modify(@PathVariable("comment_id") int comment_id, @RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("comment"))){
            result.put("message", "comment is null");
            result.put("resultCode", "false");
            return result;
        }

        String comment = data.get("comment").toString();

        CommentTb commentTb = commentRepository.getCommentByCommentId(comment_id);

        int user_id = jwtTokenProvider.getUserId(tokenHeader);
        String user_role = userRepository.getRoleByUserId(user_id);
        if(commentTb.getUser_id() == jwtTokenProvider.getUserId(tokenHeader) || user_role.equals("admin")){

            commentTb.setComment(comment);
            commentRepository.save(commentTb);
            result.put("resultCode", "true");

        }else{
            result.put("message", "unauthorized");
            result.put("resultCode", "false");
            return result;
        }



        return result;
    }

    @ApiOperation(value = "댓글 좋아요", notes = "한번 누르면 좋아요 버튼 두번 누르면 좋아요 해제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "community_id", value = "커뮤니티 id", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/comments/{comment_id}", method = RequestMethod.PUT)
    public HashMap comment_like(@PathVariable("comment_id") int comment_id, @RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data)){
            result.put("message", "community_id is null");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("community_id"))){
            result.put("message", "community_id is null");
            result.put("resultCode", "false");
            return result;
        }

        int community_id = Integer.parseInt(data.get("community_id").toString());

        CommentLikeTb commentLikeTb = new CommentLikeTb();
        commentLikeTb.setComment_id(comment_id);
        commentLikeTb.setCommunity_id(community_id);
        commentLikeTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));


        if(commentlikeRepository.CommentLikeCheckByAll(commentLikeTb)){

            CommentLikeTb heart = commentlikeRepository.getCommentLike(commentLikeTb);
            commentlikeRepository.delete(heart);

            result.put("message", "unpushed");
            result.put("resultCode", "true");

        }else{
            commentlikeRepository.save(commentLikeTb);
            result.put("message", "pushed");
            result.put("resultCode", "true");
        }
        return result;


    }

    @ApiOperation(value = "내 댓글 확인", notes = "내 댓글 확인")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowpage", value = "현재 페이지 번호", required = true),
            @ApiImplicitParam(name = "count", value = "보여줄 게시글 갯수", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/my-comment", method = RequestMethod.GET)
    public HashMap my_community(@RequestParam ("nowpage") int nowpage, @RequestParam ("count") int countpage, @RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        int start = countpage * nowpage;
        int end = countpage*(nowpage+1) - 1;


        try{

            List<CommentTb> commentTbList = commentRepository.getCommentListDistinct(jwtTokenProvider.getUserId(tokenHeader));
            List<RecommentDto> recommentDtoList = new ArrayList<>();
            List<CommunityWriterDto> communityTbList = new ArrayList<>();
            List<CommentWriterDto> commentWriterDto_list = new ArrayList<>();
            String name = userRepository.getNameByPk(jwtTokenProvider.getUserId(tokenHeader));

            if(end>=commentTbList.size()) end = commentTbList.size()-1;

            for(int i=start;i<=end;i++){
                int commentlike_like = commentlikeRepository.getCommentLike_total(commentTbList.get(i).getComment_id());
                CommentLikeTb commentLikeTb = new CommentLikeTb();
                commentLikeTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
                commentLikeTb.setCommunity_id((int) commentTbList.get(i).getCommunity_id());
                commentLikeTb.setComment_id(commentTbList.get(i).getComment_id());

                boolean my_like = commentlikeRepository.CommentLikeCheckByAll(commentLikeTb);

                CommentWriterDto commentWriterDto = new CommentWriterDto(commentTbList.get(i), name, my_like, commentlike_like, recommentDtoList);

                CommunityTb community = communityRepository.getCommunityById((int) commentTbList.get(i).getCommunity_id());

                LikeTb like = new LikeTb();
                like.setCommunity_id((int) commentTbList.get(i).getCommunity_id());
                like.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
                boolean likeResult =likeRepository.LikeCheck(like);

                int like_total = likeRepository.getLikeTotal((int) commentTbList.get(i).getCommunity_id());
                int comment_total = commentRepository.getCommentListSize((int) commentTbList.get(i).getCommunity_id());
                double total_reward = community.getHits() + community.getGet_coin() + like_total;
                CommunityWriterDto communityWriterDto = new CommunityWriterDto(community, name, likeResult, like_total, comment_total);
                communityTbList.add(communityWriterDto);
                System.out.println("test : " + my_like);
                commentWriterDto_list.add(commentWriterDto);

            }
//            result.put("commentList", commentWriterDto_list);
            result.put("community", communityTbList);

            result.put("total", commentTbList.size());
            result.put("resultCode", "true");
            return result;

        }catch (Exception e){
            log.info("{}", e);
            System.out.println("test " + e);
            result.put("resultCode", "false");
            result.put("message", "db error");
            return result;

        }

    }

}
