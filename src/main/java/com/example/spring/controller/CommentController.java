package com.example.spring.controller;

import com.example.spring.dao.CommentTb;
import com.example.spring.repository.CommentRepository;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;

@RestController
@Api
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());


    @ApiOperation(value = "댓글 삭제", notes = "게시글에 작성된 댓글 삭제, * 대댓글 포함 *")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comment_id", value = "댓글 id", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/community/comment/delete", method = RequestMethod.GET)
    public HashMap comment_delete(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader){
        HashMap<String, Object> result = new HashMap<>();

        int comment_id = Integer.parseInt(data.get("comment_id").toString());

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
            @ApiImplicitParam(name = "community_id", value = "게시글 id", required = true),
            @ApiImplicitParam(name = "comment", value = "댓글 내용", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/community/comments/write", method = RequestMethod.POST)
    public HashMap comment_write(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {

        String comment = data.get("comment").toString();
        int community_id = Integer.parseInt(data.get("community_id").toString());
        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }
        log.info("community_id : {}", community_id);
        log.info("comment : {}", comment);

        try{
            String now = LocalDate.now().toString();
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
            @ApiImplicitParam(name = "comment_id", value = "댓글 id", required = true),
            @ApiImplicitParam(name = "comment", value = "대댓글 내용", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/community/recomment/write", method = RequestMethod.POST)
    public HashMap recomment_write(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {

        String comment = data.get("comment").toString();
        int commentId = Integer.parseInt(data.get("comment_id").toString());

        HashMap<String, Object> result = new HashMap<>();
        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }


        log.info("jwt : {}", tokenHeader);
        log.info("comment : {}", comment);

        String now = LocalDate.now().toString();

        try{
            int communityId = commentRepository.getCommunityIdByCommentId(commentId);
            CommentTb commentTb = new CommentTb();
            commentTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
            commentTb.setCommunity_id(communityId);
            commentTb.setComment(comment);
            commentTb.setDate(now);
            commentTb.setParent(commentId);
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
            @ApiImplicitParam(name = "comment_id", value = "댓글 id", required = true),
            @ApiImplicitParam(name = "comment", value = "수정할 대댓글 내용", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/community/comment/modify", method = RequestMethod.POST)
    public HashMap comment_modify(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        int comment_id = Integer.parseInt(data.get("comment_id").toString());
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
}