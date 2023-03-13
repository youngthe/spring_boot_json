package com.example.spring.dto;

import com.example.spring.dao.CategoryTb;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
public class CategoryDto {

    private int category_id;

    private String category_name;

    private List<CategoryTb> subcategory;

    public CategoryDto(CategoryTb categoryTb, List<CategoryTb> subcategory) {
        this.category_id = categoryTb.getCategory_id();
        this.category_name = categoryTb.getCategory_name();
        this.subcategory = subcategory;
    }
}
