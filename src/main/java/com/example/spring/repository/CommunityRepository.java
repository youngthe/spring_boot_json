package com.example.spring.repository;

import com.example.spring.dao.CommunityTb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository  extends JpaRepository<CommunityTb, Integer>, CommunityTbRepositoryCustom {


}
