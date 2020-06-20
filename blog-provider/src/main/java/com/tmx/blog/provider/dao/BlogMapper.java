package com.tmx.blog.provider.dao;

import com.tmx.blog.api.entity.Blog;
import com.tmx.blog.api.util.PageQueryUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogMapper {

    int insertBlog(Blog record);
    int insertBlogContent(Blog record);

    Blog selectById(Long blogId);
    java.lang.String selectContentById(Long blogId);
    List<Blog> findBlogList(PageQueryUtil pageUtil);
    List<Blog> findBlogListByType(@Param("type") int type, @Param("limit") int limit);
    int getTotalBlogs(PageQueryUtil pageUtil);

    int deleteBatch(Integer[] ids);

    int updateById(Blog record);
    int updateContentById(Blog record);
    int deleteBlogCategorys(@Param("ids") Integer[] ids);
}