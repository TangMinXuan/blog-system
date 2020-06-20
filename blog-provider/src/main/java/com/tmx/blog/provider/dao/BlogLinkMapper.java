package com.tmx.blog.provider.dao;

import com.tmx.blog.api.entity.BlogLink;
import com.tmx.blog.api.util.PageQueryUtil;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogLinkMapper {
    int deleteByPrimaryKey(Integer linkId);

    int insert(BlogLink record);

    int insertSelective(BlogLink record);

    BlogLink selectByPrimaryKey(Integer linkId);

    int updateByPrimaryKeySelective(BlogLink record);

    int updateByPrimaryKey(BlogLink record);

    List<BlogLink> findLinkList(PageQueryUtil pageUtil);

    int getTotalLinks();

    int deleteBatch(Integer[] ids);
}