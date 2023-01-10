package com.example.spring.spring.repository;

import com.example.spring.spring.dao.HeartTb;
import com.example.spring.spring.dao.UserTb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<HeartTb, Integer>, HeartTbRepositoryCustom{

}
