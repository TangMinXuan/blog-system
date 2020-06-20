package com.tmx.blog.api.service;


import com.tmx.blog.api.entity.BlogComment;
import com.tmx.blog.api.util.DubboPageUtil;
import com.tmx.blog.api.util.PageResult;

public interface CommentService {
    /**
     * 添加评论
     *
     * @param blogComment
     * @return
     */
    Boolean addComment(BlogComment blogComment);

    /**
     * 后台管理系统中评论分页功能
     *
     * @param pageUtil
     * @return
     */
    PageResult getCommentsPage(DubboPageUtil pageUtil);

    int getTotalComments();

    /**
     * 批量审核
     *
     * @param ids
     * @return
     */
    Boolean checkDone(Integer[] ids);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    Boolean deleteBatch(Integer[] ids);

    /**
     * 添加回复
     *
     * @param commentId
     * @param replyBody
     * @return
     */
    Boolean reply(Long commentId, java.lang.String replyBody);

    /**
     * 根据文章id和分页参数获取文章的评论列表
     *
     * @param blogId
     * @param page
     * @return
     */
    PageResult getCommentByBlogId(Long blogId, int page);
}
