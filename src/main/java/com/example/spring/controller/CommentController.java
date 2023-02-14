package com.example.spring.controller;

import com.example.spring.dao.CommentLikeTb;
import com.example.spring.dao.CommentTb;
import com.example.spring.dao.LikeTb;
import com.example.spring.repository.CommentRepository;
import com.example.spring.repository.CommentlikeRepository;
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
import java.util.Date;
import java.util.HashMap;

@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CommentlikeRepository commentlikeRepository;

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

            ;
            if(commentTb.getUser_id() == jwtTokenProvider.getUserId(tokenHeader)){

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

        if(commentTb.getUser_id() == jwtTokenProvider.getUserId(tokenHeader)){

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


        if(commentlikeRepository.CommentLikeCheck(commentLikeTb)){

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

}
