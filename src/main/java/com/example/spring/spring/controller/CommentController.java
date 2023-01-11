package com.example.spring.spring.controller;

import com.example.spring.spring.dao.CommentTb;
import com.example.spring.spring.repository.CommentRepository;
import com.example.spring.spring.utils.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.xml.stream.events.Comment;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @RequestMapping(value="/community/comment/delete/")
    public HashMap comment_Delete(@RequestBody HashMap<String, Object> data){
        HashMap<String, Object> result = new HashMap<>();

        String jwt = data.get("jwt").toString();
        int comment_id = Integer.parseInt(data.get("comment_id").toString());

        if(!jwtTokenProvider.validateToken(jwt)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }
        try{

            CommentTb commentOne = commentRepository.getCommentByCommentId(comment_id);

            List<CommentTb> commentTbList = commentRepository.getCommentList(commentOne.getParent().getId());
            for(CommentTb comment : commentTbList){
                commentRepository.deleteById(comment.getId());
            }

            result.put("resultCode", "true");
            return result;

        }catch(Exception e){
            System.out.println(e);
            System.out.println("can't delete comment");
            result.put("resultCode", "false;");
            return result;
        }
    }

    @RequestMapping(value = "/community/comments/write/")
    public HashMap comment_add(@RequestBody HashMap<String, Object> data) {

        String jwt = data.get("jwt").toString();
        String comment = data.get("comment").toString();
        int community_id = Integer.parseInt(data.get("community_id").toString());
        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(jwt)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }
        log.info("community_id : {}", community_id);
        log.info("comment : {}", comment);

        try{
            String now = LocalDate.now().toString();
            CommentTb commentTb = new CommentTb();
            commentTb.setCommunity_id(community_id);
            commentTb.setComment(comment);
            commentTb.setDate(now);
            commentTb.setParent(commentTb);
            commentRepository.save(commentTb);

            result.put("resultCode", "true");
            return result;

        } catch (Exception e){
            log.info("/community/comments/{community_id} error");
            result.put("resultCode", "false");
            return result;
        }

    }

    @RequestMapping(value = "/community/recomment/")
    public HashMap recomment_add(@RequestBody HashMap<String, Object> data) {

        String jwt = data.get("jwt").toString();
        String recomment = data.get("recomment").toString();
        int commentId = Integer.parseInt(data.get("commentId").toString());

        HashMap<String, Object> result = new HashMap<>();
        if(!jwtTokenProvider.validateToken(jwt)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }


        log.info("jwt : {}", jwt);
        log.info("comment : {}", recomment);

        String now = LocalDate.now().toString();

        try{
            int communityId = commentRepository.getCommunityIdByCommentId(commentId);
            CommentTb commentTb = new CommentTb();
            commentTb.setCommunity_id(communityId);
            commentTb.setComment(recomment);
            commentTb.setDate(now);
            commentTb.setParent(commentRepository.getReferenceById(commentId));
            commentRepository.save(commentTb);

            result.put("resultCode", "true");
            return result;
        }catch (Exception e){
            result.put("resultCode", "false");
            return result;
        }

    }
}
