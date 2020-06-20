package com.tmx.blog.provider.dao;

import com.tmx.blog.api.entity.BlogComment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlogCommentMapper {
    int deleteByPrimaryKey(Long commentId);

    int insert(BlogComment record);

    int insertSelective(BlogComment record);

    BlogComment selectByPrimaryKey(Long commentId);

    int updateByPrimaryKeySelective(BlogComment record);

    int updateByPrimaryKey(BlogComment record);

    List<BlogComment> findBlogCommentList(Map map);

    int getTotalBlogComments(Map map);

    int checkDone(Integer[] ids);

    int deleteBatch(Integer[] ids);
}