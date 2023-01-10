package com.example.spring.spring;

import com.example.spring.spring.dao.CommentTb;
import com.example.spring.spring.dao.CommunityTb;
import com.example.spring.spring.repository.CommentRepository;
import com.example.spring.spring.repository.CommunityRepository;
import com.example.spring.spring.repository.UserRepository;
import com.example.spring.spring.utils.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ApplicationTests {

    @Autowired
    CommunityRepository communityRepository;
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;


    @Test
    void contextLoads() {


        List<CommentTb> commentList = commentRepository.getCommentList(2);

        for(CommentTb commentTb : commentList){
            commentRepository.deleteById(commentTb.getId());
        }

    }

}
