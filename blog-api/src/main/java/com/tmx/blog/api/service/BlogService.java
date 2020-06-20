package com.tmx.blog.api.service;

import com.tmx.blog.api.entity.Blog;
import com.tmx.blog.api.util.DubboPageUtil;
import com.tmx.blog.api.util.PageResult;
import com.tmx.blog.api.vo.BlogDetailVO;
import com.tmx.blog.api.vo.SimpleBlogVO;

import java.util.List;

public interface BlogService {

    java.lang.String saveBlog(Blog blog);

    PageResult getBlogsPage(DubboPageUtil pageUtil);

    Boolean deleteBatch(Integer[] ids);

    int getTotalBlogs();

    /**
     * 根据id获取详情
     *
     * @param blogId
     * @return
     */
    Blog getBlogById(Long blogId);

    /**
     * 后台修改
     *
     * @param blog
     * @return
     */
    java.lang.String updateBlog(Blog blog);

    /**
     * 获取首页文章列表
     *
     * @param page
     * @return
     */
    PageResult getBlogVOList(int page);

    /**
     * 首页侧边栏数据列表
     * 0-点击最多 1-最新发布
     *
     * @param type
     * @return
     */
    List<SimpleBlogVO> getSimpleBlogVOList(int type);

    /**
     * 文章详情
     *
     * @param blogId
     * @return
     */
    BlogDetailVO getBlogDetailVO(Long blogId);

    /**
     * 根据分类获取文章列表
     *
     * @param categoryId
     * @param page
     * @return
     */
    PageResult getBlogsPageByCategory(String categoryId, int page);

    /**
     * 根据搜索获取文章列表
     *
     * @param keyword
     * @param page
     * @return
     */
    PageResult getBlogsPageBySearch(String keyword, int page);

}
