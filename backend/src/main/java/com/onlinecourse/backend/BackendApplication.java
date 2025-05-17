package com.onlinecourse.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	// Inject các thuộc tính từ application.properties
	@Value("${spring.datasource.url}")
	private String jdbcUrl;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	//test connect
    @Bean
    public CommandLineRunner checkDatabaseConnection() {
        return args -> {
            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                System.out.println("Kết nối cơ sở dữ liệu thành công!");
            } catch (SQLException e) {
                System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
            }
        };
    }

	//test console
//	@Bean
//	public CommandLineRunner displayCategories (CategoryService categoryService) {
//		return args -> {
//			System.out.println("Danh sách danh muc:");
//			categoryService.getAllCategories().forEach(category -> System.out.println(category.getName()));
//		};
//	}

//	@Bean
//	public CommandLineRunner displayCourse (CourseService c) {
//		return args -> {
//			System.out.println("Danh sách khoá học:");
//			c.getAllCourse().forEach(course -> System.out.println(course.getTitle()));
//		};
//	}

//	@Bean
//	public CommandLineRunner displayCourse (UserContr u) {
//		return args -> {
//			System.out.println("Test:");
//			u.getUserCourseProgress(1).forEach(course -> System.out.println(course.getCourseTitle()));
//		};
//	}

}
