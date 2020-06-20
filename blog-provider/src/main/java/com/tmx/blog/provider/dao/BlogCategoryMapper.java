package com.tmx.blog.provider.dao;

import com.tmx.blog.api.entity.BlogCategory;
import com.tmx.blog.api.util.PageQueryUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogCategoryMapper {

    int insertSelective(BlogCategory record);

    BlogCategory selectByPrimaryKey(Integer categoryId);

    BlogCategory selectByCategoryName(java.lang.String categoryName);

    int updateByPrimaryKeySelective(BlogCategory record);

    List<BlogCategory> findCategoryList(PageQueryUtil pageUtil);

    List<BlogCategory> selectByCategoryIds(@Param("categoryIds") List<Integer> categoryIds);

    int getTotalCategories(PageQueryUtil pageUtil);

    int deleteBatch(Integer[] ids);
}