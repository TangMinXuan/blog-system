package com.tmx.blog.api.service;


import com.tmx.blog.api.entity.BlogCategory;
import com.tmx.blog.api.util.DubboPageUtil;
import com.tmx.blog.api.util.PageResult;

import java.util.List;

public interface CategoryService {

    PageResult getBlogCategoryPage(DubboPageUtil dubboPageUtil);

    int getTotalCategories();

    /**
     * 添加分类数据
     *
     * @param categoryName
     * @param categoryIcon
     * @return
     */
    Boolean saveCategory(java.lang.String categoryName, java.lang.String categoryIcon);

    Boolean updateCategory(Integer categoryId, java.lang.String categoryName, java.lang.String categoryIcon);

    Boolean deleteBatch(Integer[] ids);

    List<BlogCategory> getAllCategories();
}
