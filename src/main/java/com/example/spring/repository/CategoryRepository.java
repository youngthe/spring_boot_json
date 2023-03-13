package com.example.spring.repository;

import com.example.spring.dao.CategoryTb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryTb, Integer>, CategoryTbRepositoryCustom{
}
