package com.example.pongdang.fishingTrip.config;

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
                        .allowedOriginPatterns("*") // 프론트엔드 URL 허용
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true) // ✅ 쿠키 전송 허용
                        .allowedHeaders("*")
                        .exposedHeaders("Set-Cookie"); // ✅ JWT 헤더 노출
            }
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry){
                // 클라이언트가 'http://localhost:8090/uploads/파일명'으로 이미지 접근 가능하도록 설정
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("file:uploads/") // 로컬 파일 시스템에서 가져옴
                        .setCachePeriod(3600); // 캐시 유지 시간 (1시간)
            }
        };
    }
}