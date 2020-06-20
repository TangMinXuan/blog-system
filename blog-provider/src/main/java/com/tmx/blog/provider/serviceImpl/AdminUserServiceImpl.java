package com.tmx.blog.provider.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tmx.blog.api.entity.AdminUser;
import com.tmx.blog.api.service.AdminUserService;
import com.tmx.blog.api.util.MD5Util;
import com.tmx.blog.provider.dao.AdminUserMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public AdminUser login(String userName, String password) {
        String passwordMd5 = MD5Util.MD5Encode(password, "UTF-8");
        return adminUserMapper.login(userName, passwordMd5);
    }

    @Override
    public AdminUser getUserDetailById(Integer loginUserId) {
        return adminUserMapper.selectByPrimaryKey(loginUserId);
    }
}
