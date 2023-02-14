package com.example.spring.repository;

import com.example.spring.dao.CommentLikeTb;
import com.example.spring.dao.CommentTb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentlikeRepository extends JpaRepository<CommentLikeTb, Integer>, CommentlikeRepositoryCustom {
}
