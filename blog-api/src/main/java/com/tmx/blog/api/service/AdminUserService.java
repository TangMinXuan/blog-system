package com.tmx.blog.api.service;


import com.tmx.blog.api.entity.AdminUser;

public interface AdminUserService {

    AdminUser login(String userName, String password);

    AdminUser getUserDetailById(Integer loginUserId);

}
