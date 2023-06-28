package com.Library.restAPI.service;

import com.Library.restAPI.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    public List<Category> getAllCategories();
    public Category getCategoryById(Long id);

    public void createCategory(Category category);
    public void editCategory(Category category);
    public void deleteCategoryById(Long id);

}
