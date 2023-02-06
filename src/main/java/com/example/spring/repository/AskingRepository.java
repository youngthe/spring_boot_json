package com.example.spring.repository;

import com.example.spring.dao.AskingTb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AskingRepository extends JpaRepository<AskingTb, Integer>, AskingRepositoryCustom{

    @Override
    List<AskingTb> findAllById(Iterable<Integer> integers);

}
