package com.example.pongdang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
                        .allowedOriginPatterns("*") // 프론트엔드 URL 허용 로컬일때 [체인지]
                        .allowedOrigins("https://www.pongdangserver.shop") // 운영일때  [체인지]
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // 허용할 HTTP 메서드
                        .allowCredentials(true) // 쿠키 전송 허용
                        .allowedHeaders("*")
                        .exposedHeaders("Set-Cookie"); // JWT 헤더 노출
            }

            @Value("${file.upload-dir}")
            private String uploadDir;

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry){
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("file:" + uploadDir + "/") // yml에서 동적으로 불러오기
                        .setCachePeriod(3600); // 캐시 유지 시간 (1시간)

                registry.addResourceHandler("/uploads/profile/**")
                        .addResourceLocations("file:" + uploadDir + "/profile/") // yml에서 동적으로 불러오기
                        .setCachePeriod(3600); // 캐시 유지 시간 (1시간)
            }
        };
    }
}