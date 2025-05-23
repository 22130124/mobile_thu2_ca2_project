/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 100432 (10.4.32-MariaDB)
 Source Host           : localhost:3306
 Source Schema         : online_courses

 Target Server Type    : MySQL
 Target Server Version : 100432 (10.4.32-MariaDB)
 File Encoding         : 65001

 Date: 23/05/2025 13:12:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE INDEX `name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of categories
-- ----------------------------
INSERT INTO `categories` VALUES (3, 'Kinh doanh');
INSERT INTO `categories` VALUES (5, 'Kỹ năng mềm');
INSERT INTO `categories` VALUES (1, 'Lập trình');
INSERT INTO `categories` VALUES (4, 'Ngoại ngữ');
INSERT INTO `categories` VALUES (2, 'Thiết kế');

-- ----------------------------
-- Table structure for courses
-- ----------------------------
DROP TABLE IF EXISTS `courses`;
CREATE TABLE `courses`  (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                            `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
                            `category_id` int NOT NULL,
                            `image_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                            `number_of_lessons` int NOT NULL DEFAULT 0,
                            `difficulty` enum('EASY','MEDIUM','HARD') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                            `created_at` timestamp NULL DEFAULT current_timestamp(),
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `category_id`(`category_id` ASC) USING BTREE,
                            CONSTRAINT `courses_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of courses
-- ----------------------------
INSERT INTO `courses` VALUES (1, 'Lập trình Java (Java Core)', 'Khóa học phù hợp cho người mới bắt đầu học lập trình hoặc những ai muốn củng cố kiến thức Java căn bản một cách hệ thống, qua các bài giảng lý thuyết kết hợp thực hành chi tiết.', 1, 'https://titv.vn/wp-content/uploads/2023/01/Lap-trinh-Java-870x440.png', 10, 'MEDIUM', '2025-05-03 20:48:10');
INSERT INTO `courses` VALUES (2, 'Thiết kế UI/UX nâng cao', 'Khóa học chuyên sâu về thiết kế giao diện.', 2, '/images/thiet-ke-ui-ux-nang-cao.jpg', 8, 'HARD', '2025-05-03 20:48:10');
INSERT INTO `courses` VALUES (3, 'Kinh doanh online hiệu quả', 'Cách khởi nghiệp kinh doanh trên nền tảng số.', 3, '/images/kinh-doanh-online.jpg', 12, 'EASY', '2025-05-03 20:48:10');
INSERT INTO `courses` VALUES (5, 'Tiếng anh giao tiếp', 'Học Tiếng Anh giao tiếp cho người mới bắt đầu', 4, '/images/tieng-anh-giao-tiep.jpg', 6, 'EASY', '2025-05-08 08:59:52');
INSERT INTO `courses` VALUES (6, 'Kỹ năng thuyết trình', 'Kỹ năng thuyết trình hiệu quả giúp chinh phục đám đông', 5, '/images/ky-nang-thuyet-trinh.jpg', 5, 'EASY', '2025-05-08 09:08:05');

-- ----------------------------
-- Table structure for enrollments
-- ----------------------------
DROP TABLE IF EXISTS `enrollments`;
CREATE TABLE `enrollments`  (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `user_id` int NOT NULL,
                                `course_id` int NOT NULL,
                                `enrolled_at` timestamp NULL DEFAULT current_timestamp(),
                                PRIMARY KEY (`id`) USING BTREE,
                                UNIQUE INDEX `user_id`(`user_id` ASC, `course_id` ASC) USING BTREE,
                                INDEX `course_id`(`course_id` ASC) USING BTREE,
                                CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enrollments
-- ----------------------------
INSERT INTO `enrollments` VALUES (1, 1, 1, '2025-05-03 21:12:17');
INSERT INTO `enrollments` VALUES (2, 2, 1, '2025-05-03 21:12:17');
INSERT INTO `enrollments` VALUES (3, 3, 2, '2025-05-03 21:12:17');

-- ----------------------------
-- Table structure for lesson_progress
-- ----------------------------
DROP TABLE IF EXISTS `lesson_progress`;
CREATE TABLE `lesson_progress`  (
                                    `id` int NOT NULL AUTO_INCREMENT,
                                    `enrollment_id` int NOT NULL,
                                    `lesson_id` int NOT NULL,
                                    `is_completed` tinyint(1) NOT NULL DEFAULT 0,
                                    `completed_at` timestamp NULL DEFAULT NULL,
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE INDEX `enrollment_id`(`enrollment_id` ASC, `lesson_id` ASC) USING BTREE,
                                    INDEX `lesson_id`(`lesson_id` ASC) USING BTREE,
                                    CONSTRAINT `lesson_progress_ibfk_1` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
                                    CONSTRAINT `lesson_progress_ibfk_2` FOREIGN KEY (`lesson_id`) REFERENCES `lessons` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lesson_progress
-- ----------------------------
INSERT INTO `lesson_progress` VALUES (1, 1, 1, 1, '2025-05-03 21:15:01');
INSERT INTO `lesson_progress` VALUES (2, 1, 2, 0, NULL);
INSERT INTO `lesson_progress` VALUES (3, 2, 1, 0, NULL);
INSERT INTO `lesson_progress` VALUES (4, 3, 1, 0, NULL);

-- ----------------------------
-- Table structure for lessons
-- ----------------------------
DROP TABLE IF EXISTS `lessons`;
CREATE TABLE `lessons`  (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `course_id` int NOT NULL,
                            `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                            `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
                            `youtube_video_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                            `duration_minutes` double NOT NULL,
                            `created_at` timestamp NULL DEFAULT current_timestamp(),
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `course_id`(`course_id` ASC) USING BTREE,
                            CONSTRAINT `lessons_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lessons
-- ----------------------------
INSERT INTO `lessons` VALUES (1, 1, 'Java 01. Vì sao học lập trình Java', '', 'https://www.youtube.com/watch?v=xfOp0izFnu0&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F', 18.27, '2025-05-03 21:10:37');
INSERT INTO `lessons` VALUES (2, 1, 'Java 02. Cài đặt JDK và Eclipse', '', 'https://www.youtube.com/watch?v=ayA1Lz2qEZo&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=2', 16.67, '2025-05-03 21:10:37');
INSERT INTO `lessons` VALUES (3, 1, 'Java 03. Cấu trúc của một lớp Java', '', 'https://www.youtube.com/watch?v=6Gbxt2Sox7k&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=3', 10.72, '2025-05-23 11:58:18');
INSERT INTO `lessons` VALUES (4, 1, 'Java 04. Cách khai báo biến trong Java', '', 'https://www.youtube.com/watch?v=zEbraQ5vIaU&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=4', 10.42, '2025-05-23 12:00:23');
INSERT INTO `lessons` VALUES (5, 1, 'Java 05 . Kiểu dữ liệu trong Java', '', 'https://www.youtube.com/watch?v=S29I8oXEXf8&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=5', 8.98, '2025-05-23 12:01:04');
INSERT INTO `lessons` VALUES (6, 1, 'Java 06. Hằng số trong Java', '', 'https://www.youtube.com/watch?v=IrtwjVY18do&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=6', 4.6, '2025-05-23 12:01:48');
INSERT INTO `lessons` VALUES (7, 1, 'Java 07 . Cách ghi chú trong Java', '', 'https://www.youtube.com/watch?v=jgzgkUbK35M&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=7', 8.92, '2025-05-23 12:02:33');
INSERT INTO `lessons` VALUES (8, 1, 'Java 08. Cách kiểm tra và xử lý lỗi biên dịch', '', 'https://www.youtube.com/watch?v=2Zu17CS3288&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=8', 5.93, '2025-05-23 12:03:21');
INSERT INTO `lessons` VALUES (9, 1, 'Java 09. Cách nhập dữ liệu từ bán phím', '', 'https://www.youtube.com/watch?v=ymFKMQSeodQ&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=9', 11.32, '2025-05-23 12:05:14');
INSERT INTO `lessons` VALUES (12, 1, 'Java 10. Chuyển đổi kiểu dữ liệu trong Java', '', 'https://www.youtube.com/watch?v=BwOt3IeeP64&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=10', 12.18, '2025-05-23 12:06:09');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                          `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                          `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                          `role` enum('USER','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'USER',
                          `is_active` tinyint(1) NULL DEFAULT 1,
                          `created_at` timestamp NULL DEFAULT current_timestamp(),
                          `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (`id`) USING BTREE,
                          UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'Nguyễn Văn A', 'a@example.com', 'hashed_password_1', 'USER', 1, '2025-05-03 20:47:59', NULL);
INSERT INTO `users` VALUES (2, 'Trần Thị B', 'b@example.com', 'hashed_password_2', 'ADMIN', 1, '2025-05-03 20:47:59', NULL);
INSERT INTO `users` VALUES (3, 'Lê Văn C', 'c@example.com', 'hashed_password_3', 'USER', 1, '2025-05-03 20:47:59', NULL);

SET FOREIGN_KEY_CHECKS = 1;
