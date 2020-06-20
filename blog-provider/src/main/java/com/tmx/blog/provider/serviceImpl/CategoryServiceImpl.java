package com.tmx.blog.provider.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tmx.blog.api.entity.BlogCategory;
import com.tmx.blog.api.service.CategoryService;
import com.tmx.blog.api.util.DubboPageUtil;
import com.tmx.blog.api.util.PageQueryUtil;
import com.tmx.blog.api.util.PageResult;
import com.tmx.blog.provider.dao.BlogCategoryMapper;
import com.tmx.blog.provider.dao.BlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private BlogCategoryMapper blogCategoryMapper;

    @Autowired
    private BlogMapper blogMapper;

    /**
     * Category页所有categories汇总
     */
    @Override
    public PageResult getBlogCategoryPage(DubboPageUtil dubboPageUtil) {
        PageQueryUtil pageUtil = new PageQueryUtil(dubboPageUtil);

        List<BlogCategory> categoryList = blogCategoryMapper.findCategoryList(pageUtil);
        int total = blogCategoryMapper.getTotalCategories(pageUtil);
        PageResult pageResult = new PageResult(categoryList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    /**
     * 返回所有categories，不分页
     */
    @Override
    public List<BlogCategory> getAllCategories() {
        return blogCategoryMapper.findCategoryList(null);
    }

    @Override
    public int getTotalCategories() {
        return blogCategoryMapper.getTotalCategories(null);
    }

    @Override
    public Boolean saveCategory(String categoryName, String categoryIcon) {
        BlogCategory temp = blogCategoryMapper.selectByCategoryName(categoryName);
        if (temp == null) {
            BlogCategory blogCategory = new BlogCategory();
            blogCategory.setCategoryName(categoryName);
            blogCategory.setCategoryIcon(categoryIcon);
            return blogCategoryMapper.insertSelective(blogCategory) > 0;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon) {
        BlogCategory blogCategory = blogCategoryMapper.selectByPrimaryKey(categoryId);
        if (blogCategory != null) {
            blogCategory.setCategoryIcon(categoryIcon);
            blogCategory.setCategoryName(categoryName);
            return blogCategoryMapper.updateByPrimaryKeySelective(blogCategory) > 0;
        }
        return false;
    }

    /**
     * 批量删除categories，删除categories下的博客的categoriesId全部设置为1，默认分类
     */
    @Override
    @Transactional
    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //修改tb_blog表
        blogMapper.deleteBlogCategorys(ids);
        //删除分类数据
        return blogCategoryMapper.deleteBatch(ids) > 0;
    }

}
