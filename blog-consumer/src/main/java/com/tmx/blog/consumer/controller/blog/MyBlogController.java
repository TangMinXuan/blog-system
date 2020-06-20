package com.tmx.blog.consumer.controller.blog;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tmx.blog.api.entity.BlogCategory;
import com.tmx.blog.api.service.*;
import com.tmx.blog.api.vo.BlogDetailVO;
import com.tmx.blog.api.entity.BlogComment;
import com.tmx.blog.api.entity.BlogLink;
import com.tmx.blog.api.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class MyBlogController {

    //public static String theme = "default";
    //public static String theme = "yummy-jekyll";
    public static String theme = "yummy-jekyll";
    @Reference
    private BlogService blogService;
    @Reference
    private LinkService linkService;
    @Reference
    private CommentService commentService;
    @Reference
    private ConfigService configService;
    @Reference
    private CategoryService categoryService;

    /**
     * 首页
     *
     * @return
     */
    @GetMapping({"/", "/index", "index.html"})
    public String index(HttpServletRequest request) {
        return this.page(request, 1);
    }

    /**
     * 首页 分页数据
     *
     * @return
     */
    @GetMapping({"/page/{pageNum}"})
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum) {
        PageResult blogPageResult = blogService.getBlogVOList(pageNum);
        if (blogPageResult == null) {
            return "error/error_404";
        }
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("newBlogs", blogService.getSimpleBlogVOList(1));
        request.setAttribute("hotBlogs", blogService.getSimpleBlogVOList(0));
        request.setAttribute("pageName", "首页");
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/index";
    }

    /**
     * Categories页面(包括分类数据和标签数据)
     *
     * @return
     */
    @GetMapping({"/categories"})
    public String categories(HttpServletRequest request) {
        List<BlogCategory> categoryList = categoryService.getAllCategories();
//        for(BlogCategory category : categoryList) {
//            System.out.println("category = " + category);
//        }
        request.setAttribute("categories", categoryList);
        request.setAttribute("pageName", "分类页面");
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/category";
    }

    /**
     * 详情页
     *
     * @return
     */
    @GetMapping({"/blog/{blogId}", "/article/{blogId}"})
    public String detail(HttpServletRequest request, @PathVariable("blogId") Long blogId, @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage) {
        BlogDetailVO blogDetailVO = blogService.getBlogDetailVO(blogId);
        if (blogDetailVO != null) {
            request.setAttribute("blogDetailVO", blogDetailVO);
            request.setAttribute("commentPageResult", commentService.getCommentByBlogId(blogId, commentPage));
        }
        request.setAttribute("pageName", "详情");
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/detail";
    }

    /**
     * 分类列表页
     *
     * @return
     */
    @GetMapping({"/category/{categoryName}"})
    public String category(HttpServletRequest request, @PathVariable("categoryName") String categoryName) {
        return category(request, categoryName, 1);
    }

    /**
     * 分类列表页
     *
     * @return
     */
    @GetMapping({"/category/{categoryName}/{page}"})
    public String category(HttpServletRequest request, @PathVariable("categoryName") String categoryName, @PathVariable("page") Integer page) {
        PageResult blogPageResult = blogService.getBlogsPageByCategory(categoryName, page);
        System.out.println("blogPageResult = " + blogPageResult);

        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "分类");
        request.setAttribute("pageUrl", "category");
        request.setAttribute("keyword", categoryName);
        request.setAttribute("newBlogs", blogService.getSimpleBlogVOList(1));
        request.setAttribute("hotBlogs", blogService.getSimpleBlogVOList(0));
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/list";
    }

    /**
     * 搜索列表页
     *
     * @return
     */
    @GetMapping({"/search/{keyword}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword) {
        return search(request, keyword, 1);
    }

    /**
     * 搜索列表页
     *
     * @return
     */
    @GetMapping({"/search/{keyword}/{page}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword, @PathVariable("page") Integer page) {
        PageResult blogPageResult = blogService.getBlogsPageBySearch(keyword, page);
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "搜索");
        request.setAttribute("pageUrl", "search");
        request.setAttribute("keyword", keyword);
        request.setAttribute("newBlogs", blogService.getSimpleBlogVOList(1));
        request.setAttribute("hotBlogs", blogService.getSimpleBlogVOList(0));
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/list";
    }


    /**
     * 友情链接页
     *
     * @return
     */
    @GetMapping({"/link"})
    public String link(HttpServletRequest request) {
        request.setAttribute("pageName", "友情链接");
        Map<Byte, List<BlogLink>> linkMap = linkService.getLinksForLinkPage();
        if (linkMap != null) {
            //判断友链类别并封装数据 0-友链 1-推荐 2-个人网站
            if (linkMap.containsKey((byte) 0)) {
                request.setAttribute("favoriteLinks", linkMap.get((byte) 0));
            }
            if (linkMap.containsKey((byte) 1)) {
                request.setAttribute("recommendLinks", linkMap.get((byte) 1));
            }
            if (linkMap.containsKey((byte) 2)) {
                request.setAttribute("personalLinks", linkMap.get((byte) 2));
            }
        }
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/link";
    }

    /**
     * 评论操作
     */
    @PostMapping(value = "/blog/comment")
    @ResponseBody
    public Result comment(HttpServletRequest request, HttpSession session,
                          @RequestParam Long blogId,
                          @RequestParam String commentator, @RequestParam String email,
                          @RequestParam String commentBody) {
        String ref = request.getHeader("Referer");
        if (StringUtils.isEmpty(ref)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (null == blogId || blogId < 0) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (StringUtils.isEmpty(commentator)) {
            return ResultGenerator.genFailResult("请输入称呼");
        }
        if (StringUtils.isEmpty(email)) {
            return ResultGenerator.genFailResult("请输入邮箱地址");
        }
        if (!PatternUtil.isEmail(email)) {
            return ResultGenerator.genFailResult("请输入正确的邮箱地址");
        }
        if (StringUtils.isEmpty(commentBody)) {
            return ResultGenerator.genFailResult("请输入评论内容");
        }
        if (commentBody.trim().length() > 200) {
            return ResultGenerator.genFailResult("评论内容过长");
        }
        BlogComment comment = new BlogComment();
        comment.setBlogId(blogId);
        comment.setCommentator(MyBlogUtils.cleanString(commentator));
        comment.setEmail(email);
        comment.setCommentBody(MyBlogUtils.cleanString(commentBody));
        return ResultGenerator.genSuccessResult(commentService.addComment(comment));
    }

}
