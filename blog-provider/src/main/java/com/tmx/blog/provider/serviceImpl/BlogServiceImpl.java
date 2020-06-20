package com.tmx.blog.provider.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tmx.blog.api.entity.Blog;
import com.tmx.blog.api.entity.BlogCategory;
import com.tmx.blog.api.service.BlogService;
import com.tmx.blog.api.util.*;
import com.tmx.blog.api.vo.BlogDetailVO;
import com.tmx.blog.api.vo.BlogVO;
import com.tmx.blog.api.vo.SimpleBlogVO;
import com.tmx.blog.provider.dao.BlogCategoryMapper;
import com.tmx.blog.provider.dao.BlogCommentMapper;
import com.tmx.blog.provider.dao.BlogMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogCategoryMapper categoryMapper;

    @Autowired
    private BlogCommentMapper blogCommentMapper;


    @Override
    public PageResult getBlogsPage(DubboPageUtil dubboPageUtil) {
        PageQueryUtil pageUtil = new PageQueryUtil(dubboPageUtil);
        List<Blog> blogList = blogMapper.findBlogList(pageUtil);
        int total = blogMapper.getTotalBlogs(pageUtil);
        PageResult pageResult = new PageResult(blogList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public int getTotalBlogs() {
        return blogMapper.getTotalBlogs(null);
    }

    @Override
    public Blog getBlogById(Long blogId) {
        Blog blog = blogMapper.selectById(blogId);
        String blogContent = blogMapper.selectContentById(blogId);
        blog.setBlogContent(blogContent);
        return blog;
    }

    @Override
    public PageResult getBlogVOList(int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        //每页8条
        params.put("limit", 8);
        params.put("blogStatus", 1);//过滤发布状态下的数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        List<Blog> blogList = blogMapper.findBlogList(pageUtil);

        List<BlogVO> blogVOList = tansferToVO(blogList);     //将Blog 转换为 BlogVO

        int total = blogMapper.getTotalBlogs(pageUtil);
        PageResult pageResult = new PageResult(blogVOList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    /**
     * type = 1 List按id排序
     * type = 0 List按blog_view排序
     */
    @Override
    public List<SimpleBlogVO> getSimpleBlogVOList(int type) {
        List<SimpleBlogVO> simpleBlogVOList = new ArrayList<>();
        List<Blog> blogList = blogMapper.findBlogListByType(type, 9);
        for(Blog blog : blogList) {
            SimpleBlogVO simpleBlogVO = new SimpleBlogVO();
            simpleBlogVO.setBlogId(blog.getBlogId());
            simpleBlogVO.setBlogTitle(blog.getBlogTitle());

            simpleBlogVOList.add(simpleBlogVO);
        }
        return simpleBlogVOList;
    }

    @Override
    public BlogDetailVO getBlogDetailVO(Long id) {
        Blog blog = getBlogById(id);
        //不为空且状态为已发布
        BlogDetailVO blogDetailVO = getBlogDetailVO(blog);
        if (blogDetailVO != null) {
            return blogDetailVO;
        }
        return null;
    }

    private BlogDetailVO getBlogDetailVO(Blog blog) {
        if (blog != null && blog.getBlogStatus() == 1) {
            //增加浏览量
            blog.setBlogViews(blog.getBlogViews() + 1);
            blogMapper.updateById(blog);

            BlogDetailVO blogDetailVO = new BlogDetailVO();
            BeanUtils.copyProperties(blog, blogDetailVO);
            blogDetailVO.setBlogContent(MarkDownUtil.mdToHtml(blogDetailVO.getBlogContent()));

            BlogCategory blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
            if (blogCategory == null) {
                blogCategory = new BlogCategory();
                blogCategory.setCategoryId(1);
                blogCategory.setCategoryName("默认分类");
                blogCategory.setCategoryIcon("/admin/dist/img/category/00.png");
            }

            //设置评论数
            Map params = new HashMap();
            params.put("blogId", blog.getBlogId());
            params.put("commentStatus", 1);//过滤审核通过的数据
            blogDetailVO.setCommentCount(blogCommentMapper.getTotalBlogComments(params));
            return blogDetailVO;
        }
        return null;
    }

    private List<BlogVO> tansferToVO(List<Blog> blogList) {
        List<BlogVO> res = new ArrayList<>();
        for (Blog blog : blogList) {
            BlogVO blogVO = new BlogVO();
            BlogCategory blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());

            blogVO.setBlogId(blog.getBlogId());
            blogVO.setBlogTitle(blog.getBlogTitle());
            blogVO.setBlogCoverImage(blog.getBlogCoverImage());
            blogVO.setCategory(blogCategory);

            res.add(blogVO);
        }
        return res;
    }




    @Override
    @Transactional
    public String saveBlog(Blog blog) {
        String res = "fail";
        if (blogMapper.insertBlog(blog) > 0 && blogMapper.insertBlogContent(blog) > 0) {
            res = "success";
        }
        return res;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        return blogMapper.deleteBatch(ids) > 0;
    }

    @Override
    @Transactional
    public String updateBlog(Blog blog) {
        String res = "fail";
        if(blogMapper.updateById(blog) > 0 && blogMapper.updateContentById(blog) > 0) {
            res = "success";
        }
        return res;
    }

    @Override
    public PageResult getBlogsPageByCategory(String categoryName, int page) {
        if (PatternUtil.validKeyword(categoryName)) {
            BlogCategory blogCategory = categoryMapper.selectByCategoryName(categoryName);
            if ("默认分类".equals(categoryName) && blogCategory == null) {
                blogCategory = new BlogCategory();
                blogCategory.setCategoryId(0);
            }
            if (blogCategory != null && page > 0) {
                Map param = new HashMap();
                param.put("page", page);
                param.put("limit", 9);
                param.put("blogCategoryId", blogCategory.getCategoryId());
                param.put("blogStatus", 1);//过滤发布状态下的数据
                PageQueryUtil pageUtil = new PageQueryUtil(param);
                List<Blog> blogList = blogMapper.findBlogList(pageUtil);
                List<BlogVO> blogVOS = tansferToVO(blogList);
                int total = blogMapper.getTotalBlogs(pageUtil);
                PageResult pageResult = new PageResult(blogVOS, total, pageUtil.getLimit(), pageUtil.getPage());
                return pageResult;
            }
        }
        return null;
    }

    @Override
    public PageResult getBlogsPageBySearch(String keyword, int page) {
        if (page > 0 && PatternUtil.validKeyword(keyword)) {
            Map param = new HashMap();
            param.put("page", page);
            param.put("limit", 9);
            param.put("keyword", keyword);
            param.put("blogStatus", 1);//过滤发布状态下的数据
            PageQueryUtil pageUtil = new PageQueryUtil(param);
            List<Blog> blogList = blogMapper.findBlogList(pageUtil);
            List<BlogVO> blogVOS = tansferToVO(blogList);
            int total = blogMapper.getTotalBlogs(pageUtil);
            PageResult pageResult = new PageResult(blogVOS, total, pageUtil.getLimit(), pageUtil.getPage());
            return pageResult;
        }
        return null;
    }
}
