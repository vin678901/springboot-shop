package com.example.springbootshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")//application.properties에서 uploadPath를 읽어온다.
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")//images로 시작하는 경우 uplodaPath에 설정한 폴더를 기준으로 파일을 읽어오도록 설정
                .addResourceLocations(uploadPath);//로컬 컴퓨터에 저장된 파일을 읽어올 root 경로를 설정
    }
}
