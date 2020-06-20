package com.tmx.blog.provider.serviceImpl;


import com.alibaba.dubbo.config.annotation.Service;
import com.tmx.blog.api.entity.BlogComment;
import com.tmx.blog.api.service.CommentService;
import com.tmx.blog.api.util.DubboPageUtil;
import com.tmx.blog.api.util.PageQueryUtil;
import com.tmx.blog.api.util.PageResult;
import com.tmx.blog.provider.dao.BlogCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private BlogCommentMapper blogCommentMapper;

    @Override
    public PageResult getCommentsPage(DubboPageUtil dubboPageUtil) {
        PageQueryUtil pageUtil = new PageQueryUtil(dubboPageUtil);

        List<BlogComment> comments = blogCommentMapper.findBlogCommentList(pageUtil);
        int total = blogCommentMapper.getTotalBlogComments(pageUtil);
        PageResult pageResult = new PageResult(comments, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public int getTotalComments() {
        return blogCommentMapper.getTotalBlogComments(null);
    }

    @Override
    public Boolean addComment(BlogComment blogComment) {
        return blogCommentMapper.insertSelective(blogComment) > 0;
    }

    /**
     * 批量过审
     */
    @Override
    public Boolean checkDone(Integer[] ids) {
        return blogCommentMapper.checkDone(ids) > 0;
    }

    @Override
    public Boolean reply(Long commentId, String replyBody) {
        BlogComment blogComment = blogCommentMapper.selectByPrimaryKey(commentId);
        //blogComment不为空且状态为已审核，则继续后续操作
        if (blogComment != null && blogComment.getCommentStatus().intValue() == 1) {
            blogComment.setReplyBody(replyBody);
            blogComment.setReplyCreateTime(new Date());
            return blogCommentMapper.updateByPrimaryKeySelective(blogComment) > 0;
        }
        return false;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        return blogCommentMapper.deleteBatch(ids) > 0;
    }


    /**
     * 获取每篇博客下面的文章
     */
    @Override
    public PageResult getCommentByBlogId(Long blogId, int page) {
        if (page < 1) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        //每页8条
        params.put("limit", 8);
        params.put("blogId", blogId);
        params.put("commentStatus", 1);//过滤审核通过的数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        List<BlogComment> comments = blogCommentMapper.findBlogCommentList(pageUtil);
        if (!CollectionUtils.isEmpty(comments)) {
            int total = blogCommentMapper.getTotalBlogComments(pageUtil);
            PageResult pageResult = new PageResult(comments, total, pageUtil.getLimit(), pageUtil.getPage());
            return pageResult;
        }
        return null;
    }
}
