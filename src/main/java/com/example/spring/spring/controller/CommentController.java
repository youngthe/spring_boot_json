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

    @RequestMapping(value="/community/comment/delete/", method = RequestMethod.GET)
    public HashMap comment_Delete(@RequestBody HashMap<String, Object> data, @RequestHeader("jwt") String tokenHeader){
        HashMap<String, Object> result = new HashMap<>();

        int comment_id = Integer.parseInt(data.get("comment_id").toString());

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }
        try{

            commentRepository.deleteById(comment_id);
            commentRepository.deleteByParent(comment_id);
            result.put("resultCode", "true");
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

    @RequestMapping(value = "/community/comments/write/", method = RequestMethod.POST)
    public HashMap comment_add(@RequestBody HashMap<String, Object> data, @RequestHeader("jwt") String tokenHeader) {

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

    @RequestMapping(value = "/community/recomment/", method = RequestMethod.POST)
    public HashMap recomment_add(@RequestBody HashMap<String, Object> data, @RequestHeader("jwt") String tokenHeader) {

        String recomment = data.get("recomment").toString();
        int commentId = Integer.parseInt(data.get("commentId").toString());

        HashMap<String, Object> result = new HashMap<>();
        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }


        log.info("jwt : {}", tokenHeader);
        log.info("comment : {}", recomment);

        String now = LocalDate.now().toString();

        try{
            int communityId = commentRepository.getCommunityIdByCommentId(commentId);
            CommentTb commentTb = new CommentTb();
            commentTb.setCommunity_id(communityId);
            commentTb.setComment(recomment);
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
}
