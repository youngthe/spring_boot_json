package com.example.spring.repository;

import com.example.spring.dao.CategoryTb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryTb, Integer>, CategoryTbRepositoryCustom{
}
