package com.example.spring.spring.repository;

import com.example.spring.spring.dao.UserTb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserTb, Integer> , UserTbRepositoryCustom {
}
