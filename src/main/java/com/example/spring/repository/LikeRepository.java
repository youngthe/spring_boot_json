package com.example.spring.repository;

import com.example.spring.dao.LikeTb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeTb, Integer>, LikeTbRepositoryCustom {

}
