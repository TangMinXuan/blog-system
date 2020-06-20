package com.tmx.blog.provider;

import com.tmx.blog.api.entity.Blog;
import com.tmx.blog.api.entity.BlogCategory;
import com.tmx.blog.api.entity.BlogComment;
import com.tmx.blog.api.entity.BlogLink;
import com.tmx.blog.api.util.DubboPageUtil;
import com.tmx.blog.api.util.PageResult;
import com.tmx.blog.api.vo.BlogDetailVO;
import com.tmx.blog.api.vo.SimpleBlogVO;
import com.tmx.blog.provider.dao.BlogMapper;
import com.tmx.blog.provider.serviceImpl.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestApplication {

//    @Autowired
//    LinkServiceImpl linkService;

//    @Autowired
//    ConfigServiceImpl configService;

//    @Autowired
//    CommentServiceImpl commentService;

//    @Autowired
//    CategoryServiceImpl categoryService;

    @Autowired
    BlogServiceImpl blogService;

    @Autowired
    BlogMapper blogMapper;

    @Test
    public void check() {
        System.out.println("Hello world");
    }

    @Test
    public void checkLink() {
//        DubboPageUtil dubboPageUtil = new DubboPageUtil(1, 10);
//        PageResult pageResult = linkService.getBlogLinkPage(dubboPageUtil);
//        System.out.println("pageResult = " + pageResult);

//        BlogLink blogLink = new BlogLink();
//        blogLink.setLinkName("temp");
//        System.out.println(linkService.saveLink(blogLink));

//        BlogLink link = linkService.selectById(20);
//        System.out.println("link = " + link);
//
//        link.setLinkName("temp2");
//        System.out.println(linkService.updateLink(link));
//
//        Integer[] ids = new Integer[]{20};
//        System.out.println(linkService.deleteBatch(ids));

//        Map<Byte, List<BlogLink>> linkMap = linkService.getLinksForLinkPage();
//        List<BlogLink> list0 = linkMap.get((byte) 0);
//        List<BlogLink> list1 = linkMap.get((byte) 1);
//        List<BlogLink> list2 = linkMap.get((byte) 2);
//
//        for (BlogLink link : list0) {
//            System.out.println("link = " + link);
//        }
//        for (BlogLink link : list1) {
//            System.out.println("link = " + link);
//        }
//        for (BlogLink link : list2) {
//            System.out.println("link = " + link);
//        }
    }

    @Test
    public void checkConfig() {
//        Map<String, String> map = configService.getAllConfigs();
//        for (String key : map.keySet()) {
//            System.out.println("Key = " + key);
//        }
//        for (String value : map.values()) {
//            System.out.println("Value = " + value);
//        }

//        String websiteName = "personal blog 1";
//        System.out.println(configService.updateConfig("websiteName", websiteName));
    }

    @Test
    public void checkComment() {
//        DubboPageUtil dubboPageUtil = new DubboPageUtil(1, 10);
//        PageResult pageResult = commentService.getCommentsPage(dubboPageUtil);
//        System.out.println("pageResult = " + pageResult);

//        BlogComment blogComment = new BlogComment();
//        blogComment.setBlogId(1L);
//        blogComment.setCommentator("laoba");
//        blogComment.setCommentStatus((byte) 1);
//        System.out.println(commentService.addComment(blogComment));
//
//        Integer[] ids = new Integer[]{1, 2};
//        System.out.println(commentService.checkDone(ids));

//        String replyBody = "lao ba niu bi";
//        System.out.println(commentService.reply(1L, replyBody));

//        Integer[] ids = new Integer[]{1, 2};
//        System.out.println(commentService.deleteBatch(ids));

//        PageResult pageResult = commentService.getCommentByBlogId(1L, 1);
//        System.out.println("pageResult = " + pageResult);
    }

    @Test
    public void checkCategory() {
//        DubboPageUtil dubboPageUtil = new DubboPageUtil(1, 10);
//        PageResult pageResult = categoryService.getBlogCategoryPage(dubboPageUtil);
//        System.out.println("pageResult = " + pageResult);

//        List<BlogCategory> categoryList = categoryService.getAllCategories();
//        for (BlogCategory category : categoryList) {
//            System.out.println("category = " + category);
//        }

//        System.out.println(categoryService.saveCategory("test3", "111"));

//        String newName = "test3_update";
//        System.out.println(categoryService.updateCategory(3, newName, "222"));

//        Integer[] ids = new Integer[]{4, 5};
//        System.out.println(categoryService.deleteBatch(ids));
    }

    @Test
    public void checkBlog() {
//        DubboPageUtil dubboPageUtil = new DubboPageUtil(1, 10);
//        PageResult pageResult = blogService.getBlogsPage(dubboPageUtil);
//        System.out.println("pageResult = " + pageResult);
//
//        Blog blog = blogService.getBlogById(1L);
//        System.out.println("blog = " + blog);
//
//        PageResult result = blogService.getBlogVOList(1);
//        System.out.println("result = " + result);
//
//        List<SimpleBlogVO> list1 = blogService.getSimpleBlogVOList(1);
//        List<SimpleBlogVO> list0 = blogService.getSimpleBlogVOList(0);
//        for (SimpleBlogVO simpleBlogVO : list1) {
//            System.out.println("simpleBlogVO = " + simpleBlogVO);
//        }
//        for (SimpleBlogVO simpleBlogVO : list0) {
//            System.out.println("simpleBlogVO = " + simpleBlogVO);
//        }
//
//        BlogDetailVO blogDetailVO = blogService.getBlogDetailVO(1L);
//        System.out.println("blogDetailVO = " + blogDetailVO);

//        Blog blog = new Blog();
//        blog.setBlogTitle("bababa");
//        System.out.println(blogService.saveBlog(blog));

//        Blog blog1 = new Blog();
//        blog1.setBlogId(9L);
//        blog1.setBlogTitle("ababa");
//        System.out.println(blogService.updateBlog(blog1));

//        Integer[] ids = new Integer[]{5, 6, 7, 8, 9};
//        System.out.println(blogService.deleteBatch(ids));


    }
}
