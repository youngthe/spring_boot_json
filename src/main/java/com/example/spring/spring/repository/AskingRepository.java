package com.example.spring.spring.repository;

import com.example.spring.spring.dao.AskingTb;
import com.example.spring.spring.dao.CommentTb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AskingRepository extends JpaRepository<AskingTb, Integer>, AskingRepositoryCustom{

    @Override
    List<AskingTb> findAllById(Iterable<Integer> integers);

}
