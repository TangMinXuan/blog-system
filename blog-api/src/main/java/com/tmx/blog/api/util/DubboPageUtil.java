package com.tmx.blog.api.util;

import java.io.Serializable;

public class DubboPageUtil implements Serializable {

    private int page;

    private int limit;

    private int start;

    public DubboPageUtil(int page, int limit) {
        this.page = page;
        this.limit = limit;
        this.start = (page - 1) * limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
