package com.onlinecourse.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-base-dir}")
    private String uploadBaseDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cấu hình cho thư mục user_img
        registry.addResourceHandler("/user_img/**")
                .addResourceLocations(
                        Paths.get(uploadBaseDir, "user_img") // Kết hợp đường dẫn bằng Paths
                                .toAbsolutePath() // Lấy đường dẫn tuyệt đối
                                .toUri() // Chuyển sang dạng file: URI
                                .toString() // Lấy chuỗi URI
                );

        // Cấu hình cho thư mục course_img (nếu bạn cũng phục vụ ảnh khóa học)
        registry.addResourceHandler("/course_img/**")
                .addResourceLocations(
                        Paths.get(uploadBaseDir, "course_img")
                                .toAbsolutePath()
                                .toUri()
                                .toString()
                );

        // Cấu hình mặc định cho các resource tĩnh khác (nếu cần)
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}