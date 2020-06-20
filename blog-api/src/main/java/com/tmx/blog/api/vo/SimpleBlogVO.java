package com.tmx.blog.api.vo;

import java.io.Serializable;

public class SimpleBlogVO implements Serializable {

    private Long blogId;

    private String blogTitle;



    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    @Override
    public String toString() {
        return "SimpleBlogVO{" +
                "blogId=" + blogId +
                ", blogTitle='" + blogTitle + '\'' +
                '}';
    }
}
