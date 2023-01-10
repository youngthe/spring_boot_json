package com.example.spring.spring.controller;

import com.example.spring.spring.dao.CommentTb;
import com.example.spring.spring.repository.CommentRepository;
import com.example.spring.spring.utils.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashMap;

@RestController
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @RequestMapping(value="/community/comment/delete/{comment_id}")
    public HashMap comment_Delete(@PathVariable int comment_id, @RequestBody HashMap<String, Object> data){
        HashMap<String, Object> result = new HashMap<>();

        String jwt = data.get("jwt").toString();
        if(!jwtTokenProvider.validateToken(jwt)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }
        try{
            commentRepository.deleteById(comment_id);
            result.put("resultCode", "true");
            return result;

        }catch(Exception e){
            System.out.println(e);
            System.out.println("can't delete comment");
            result.put("resultCode", "false;");
            return result;

        }
    }

    @RequestMapping(value = "/community/comments/write/{community_id}")
    public HashMap comment_add(@PathVariable int community_id, @RequestBody HashMap<String, Object> data) {

        String jwt = data.get("jwt").toString();
        String comment = data.get("comment").toString();
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

    @RequestMapping(value = "/community/recomments/{commentId}")
    public String recomment_add(@PathVariable int commentId, HttpServletRequest request, HttpSession session) {

        String recomment = request.getParameter("comments");
        String now = LocalDate.now().toString();

        int communityId = commentRepository.getCommunityIdByCommentId(commentId);

        System.out.println(recomment);

        CommentTb commentTb = new CommentTb();
        commentTb.setCommunity_id(communityId);
        commentTb.setComment(recomment);
        commentTb.setDate(now);
        commentTb.setParent(commentRepository.getReferenceById(commentId));
        commentRepository.save(commentTb);
        return "redirect:/community";
    }
}
