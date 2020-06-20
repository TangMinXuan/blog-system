package com.tmx.blog.consumer.controller.admin;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tmx.blog.api.service.BlogService;
import com.tmx.blog.api.service.CategoryService;
import com.tmx.blog.api.util.DubboPageUtil;
import com.tmx.blog.consumer.config.Constants;
import com.tmx.blog.api.entity.Blog;
import com.tmx.blog.api.util.MyBlogUtils;
import com.tmx.blog.api.util.Result;
import com.tmx.blog.api.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Reference
    private BlogService blogService;
    @Reference
    private CategoryService categoryService;

    @GetMapping("/blogs/list")
    @ResponseBody
    public Result list(@RequestParam Map<java.lang.String, String> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        int page = Integer.parseInt(params.get("page"));
        int limit = Integer.parseInt(params.get("limit"));
        DubboPageUtil dubboPageUtil = new DubboPageUtil(page, limit);
        return ResultGenerator.genSuccessResult(blogService.getBlogsPage(dubboPageUtil));
    }


    @GetMapping("/blogs")
    public java.lang.String list(HttpServletRequest request) {
        request.setAttribute("path", "blogs");
        return "admin/blog";
    }

    @GetMapping("/blogs/edit")
    public java.lang.String edit(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        request.setAttribute("categories", categoryService.getAllCategories());
        return "admin/edit";
    }

    @GetMapping("/blogs/edit/{blogId}")
    public java.lang.String edit(HttpServletRequest request, @PathVariable("blogId") Long blogId) {
        request.setAttribute("path", "edit");
        Blog blog = blogService.getBlogById(blogId);
        if (blog == null) {
            return "error/error_400";
        }
        request.setAttribute("blog", blog);
        request.setAttribute("categories", categoryService.getAllCategories());
        return "admin/edit";
    }

    @PostMapping("/blogs/save")
    @ResponseBody
    public Result save(@RequestParam("blogTitle") java.lang.String blogTitle,
                       @RequestParam("blogCategoryId") Integer blogCategoryId,
                       @RequestParam("blogContent") java.lang.String blogContent,
                       @RequestParam("blogCoverImage") java.lang.String blogCoverImage,
                       @RequestParam("blogStatus") Byte blogStatus,
                       @RequestParam("enableComment") Byte enableComment) {
        if (StringUtils.isEmpty(blogTitle)) {
            return ResultGenerator.genFailResult("请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (StringUtils.isEmpty(blogContent)) {
            return ResultGenerator.genFailResult("请输入文章内容");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }
        Blog blog = new Blog();
        blog.setBlogTitle(blogTitle);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogContent(blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);
        String saveBlogResult = blogService.saveBlog(blog);
        if ("success".equals(saveBlogResult)) {
            return ResultGenerator.genSuccessResult("添加成功");
        } else {
            return ResultGenerator.genFailResult(saveBlogResult);
        }
    }

    @PostMapping("/blogs/update")
    @ResponseBody
    public Result update(@RequestParam("blogId") Long blogId,
                         @RequestParam("blogTitle") java.lang.String blogTitle,
                         @RequestParam("blogCategoryId") Integer blogCategoryId,
                         @RequestParam("blogContent") java.lang.String blogContent,
                         @RequestParam("blogCoverImage") java.lang.String blogCoverImage,
                         @RequestParam("blogStatus") Byte blogStatus,
                         @RequestParam("enableComment") Byte enableComment) {
        if (StringUtils.isEmpty(blogTitle)) {
            return ResultGenerator.genFailResult("请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (StringUtils.isEmpty(blogContent)) {
            return ResultGenerator.genFailResult("请输入文章内容");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }
        Blog blog = new Blog();
        blog.setBlogId(blogId);
        blog.setBlogTitle(blogTitle);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogContent(blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);
        java.lang.String updateBlogResult = blogService.updateBlog(blog);
        if ("success".equals(updateBlogResult)) {
            return ResultGenerator.genSuccessResult("修改成功");
        } else {
            return ResultGenerator.genFailResult(updateBlogResult);
        }
    }

    @PostMapping("/blogs/md/uploadfile")
    public void uploadFileByEditormd(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(name = "editormd-image-file", required = true)
                                             MultipartFile file) throws IOException, URISyntaxException {
        java.lang.String fileName = file.getOriginalFilename();
        java.lang.String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        java.lang.String newFileName = tempName.toString();
        //创建文件
        File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
        java.lang.String fileUrl = MyBlogUtils.getHost(new URI(request.getRequestURL() + "")) + "/upload/" + newFileName;
        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
        try {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            file.transferTo(destFile);
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html");
            response.getWriter().write("{\"success\": 1, \"message\":\"success\",\"url\":\"" + fileUrl + "\"}");
        } catch (UnsupportedEncodingException e) {
            response.getWriter().write("{\"success\":0}");
        } catch (IOException e) {
            response.getWriter().write("{\"success\":0}");
        }
    }

    @PostMapping("/blogs/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (blogService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

}
