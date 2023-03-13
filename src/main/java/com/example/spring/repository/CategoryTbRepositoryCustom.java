package com.example.spring.repository;

import com.example.spring.dao.CategoryTb;

import java.util.List;

public interface CategoryTbRepositoryCustom {
    public List<CategoryTb> getCategoryParent();

    public List<CategoryTb> getCategoryChildByParent(int parent);
}
