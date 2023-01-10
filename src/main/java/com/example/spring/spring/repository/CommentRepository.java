package com.example.spring.spring.repository;

import com.example.spring.spring.dao.CommentTb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentTb, Integer>, CommentTbRepositoryCustom {

}
