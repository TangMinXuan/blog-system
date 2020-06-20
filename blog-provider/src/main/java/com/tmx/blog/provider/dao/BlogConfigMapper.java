package com.tmx.blog.provider.dao;


import com.tmx.blog.api.entity.BlogConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogConfigMapper {
    List<BlogConfig> selectAll();

    BlogConfig selectByPrimaryKey(String configName);

    int updateByPrimaryKeySelective(BlogConfig record);

}