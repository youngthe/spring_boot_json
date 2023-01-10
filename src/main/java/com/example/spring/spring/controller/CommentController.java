package com.example.spring.spring.controller;

import com.example.spring.spring.dao.CommentTb;
import com.example.spring.spring.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDate;

@Controller
public class CommentController {

    @Autowired
    CommentRepository commentRepository;



    @RequestMapping(value="/community/comment/delete/{comment_id}")
    public String comment_Delete(@PathVariable int comment_id, Model model){

        System.out.println("comment id : " + comment_id);
        System.out.println(commentRepository.findById(comment_id));

        try{
            commentRepository.deleteById(comment_id);

        }catch(Exception e){
            System.out.println(e);
            System.out.println("can't delete comment");
        }

        return "redirect:/community/";
    }

    @RequestMapping(value = "/community/comments/{community_id}")
    public String comment_add(@PathVariable int community_id, HttpServletRequest request, HttpSession session) {

        String comment = request.getParameter("comments");
        String now = LocalDate.now().toString();
        System.out.println(comment);

        CommentTb commentTb = new CommentTb();
        commentTb.setCommunity_id(community_id);
        commentTb.setComment(comment);
        commentTb.setDate(now);
        commentTb.setParent(commentTb);
        commentRepository.save(commentTb);
        return "redirect:/community/detail/" + community_id;
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
