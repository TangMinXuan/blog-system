package com.tmx.blog.provider;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class BlogProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogProviderApplication.class, args);
    }
}
