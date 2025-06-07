/*
 Navicat Premium Dump SQL

 Source Server         : host
 Source Server Type    : MariaDB
 Source Server Version : 100432 (10.4.32-MariaDB)
 Source Host           : localhost:3306
 Source Schema         : online_courses

 Target Server Type    : MariaDB
 Target Server Version : 100432 (10.4.32-MariaDB)
 File Encoding         : 65001

 Date: 07/06/2025 09:35:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
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
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `category_id` int(11) NOT NULL,
  `image_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `number_of_lessons` int(11) NOT NULL DEFAULT 0,
  `difficulty` enum('EASY','MEDIUM','HARD') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`category_id`) USING BTREE,
  CONSTRAINT `courses_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of courses
-- ----------------------------
INSERT INTO `courses` VALUES (1, 'Khóa học Java cơ bản', 'Học lập trình Java từ đầu cho người mới bắt đầu.', 1, '/course_img/8e740122-5a43-410f-9c58-6575015e5a72_upload_17492634129458079190607838501815.tmp', 10, 'MEDIUM', '2025-05-03 20:48:10');
INSERT INTO `courses` VALUES (2, 'Thiết kế UI/UX nâng cao', 'Khóa học chuyên sâu về thiết kế giao diện.', 2, '/course_img/d6584274-7eb1-4ad3-bf55-bab3d8a37d3e_upload_17492634432926699120295887833994.tmp', 8, 'HARD', '2025-05-03 20:48:10');
INSERT INTO `courses` VALUES (3, 'Kinh doanh online hiệu quả', 'Cách khởi nghiệp kinh doanh trên nền tảng số.', 3, '/course_img/5a7d986f-d079-4722-b82e-c13dc481c764_upload_17492633555213621460789893190758.tmp', 12, 'EASY', '2025-05-03 20:48:10');
INSERT INTO `courses` VALUES (5, 'Tiếng anh giao tiếp', 'Học Tiếng Anh giao tiếp cho người mới bắt đầu', 4, '/course_img/7532a468-dab9-4ca0-8b8f-6f7a24a6e596_upload_17492634303881082772335386291200.tmp', 6, 'EASY', '2025-05-08 08:59:52');
INSERT INTO `courses` VALUES (6, 'Kỹ năng thuyết trình', 'Kỹ năng thuyết trình hiệu quả giúp chinh phục đám đông', 5, '/course_img/961c5093-b52f-488c-9329-83781ffee887_upload_17492633762522139238414798398311.tmp', 5, 'EASY', '2025-05-08 09:08:05');

-- ----------------------------
-- Table structure for enrollments
-- ----------------------------
DROP TABLE IF EXISTS `enrollments`;
CREATE TABLE `enrollments`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `enrolled_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`, `course_id`) USING BTREE,
  INDEX `course_id`(`course_id`) USING BTREE,
  CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enrollments
-- ----------------------------
INSERT INTO `enrollments` VALUES (1, 1, 1, '2025-05-03 21:12:17');
INSERT INTO `enrollments` VALUES (2, 2, 1, '2025-05-03 21:12:17');
INSERT INTO `enrollments` VALUES (3, 3, 2, '2025-05-03 21:12:17');
INSERT INTO `enrollments` VALUES (4, 6, 2, '2025-05-25 13:40:01');
INSERT INTO `enrollments` VALUES (5, 6, 5, '2025-05-25 13:40:12');
INSERT INTO `enrollments` VALUES (6, 8, 1, '2025-05-25 16:01:57');

-- ----------------------------
-- Table structure for lesson_progress
-- ----------------------------
DROP TABLE IF EXISTS `lesson_progress`;
CREATE TABLE `lesson_progress`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `enrollment_id` int(11) NOT NULL,
  `lesson_id` int(11) NOT NULL,
  `is_completed` tinyint(1) NOT NULL DEFAULT 0,
  `completed_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `enrollment_id`(`enrollment_id`, `lesson_id`) USING BTREE,
  INDEX `lesson_id`(`lesson_id`) USING BTREE,
  CONSTRAINT `lesson_progress_ibfk_1` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `lesson_progress_ibfk_2` FOREIGN KEY (`lesson_id`) REFERENCES `lessons` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lesson_progress
-- ----------------------------
INSERT INTO `lesson_progress` VALUES (1, 1, 1, 1, '2025-05-03 21:15:01');
INSERT INTO `lesson_progress` VALUES (2, 1, 2, 0, NULL);
INSERT INTO `lesson_progress` VALUES (3, 2, 1, 0, NULL);
INSERT INTO `lesson_progress` VALUES (4, 3, 1, 0, NULL);
INSERT INTO `lesson_progress` VALUES (11, 6, 1, 1, '2025-05-27 02:50:03');
INSERT INTO `lesson_progress` VALUES (12, 6, 2, 1, '2025-05-27 02:50:04');

-- ----------------------------
-- Table structure for lessons
-- ----------------------------
DROP TABLE IF EXISTS `lessons`;
CREATE TABLE `lessons`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `youtube_video_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `duration_minutes` double NOT NULL DEFAULT 0,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `course_id`(`course_id`) USING BTREE,
  CONSTRAINT `lessons_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

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
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_active` int(11) NULL DEFAULT NULL,
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'Nguyễn Văn Sinh', 'a@example.com', 'hashed_password_1', 'USER', 0, NULL, '2025-05-03 20:47:59', '2025-05-30 14:13:40');
INSERT INTO `users` VALUES (2, 'Trần Binh', 'b@example.com', 'hashed_password_2', 'USER', 0, NULL, '2025-05-03 20:47:59', '2025-05-30 14:13:11');
INSERT INTO `users` VALUES (3, 'Lê Văn C', 'c@example.com', 'hashed_password_3', 'USER', 1, NULL, '2025-05-03 20:47:59', '2025-05-30 11:53:01');
INSERT INTO `users` VALUES (4, 'kieu_', 'thuykieu20040@gmail.com', '$2a$10$ywYvAbWfUI4.Dr4/bZFtZORNZKz3/g2VqQ.MRO5WnSg7mDaIQfxXS', 'USER', 1, NULL, '2025-05-25 08:20:59', '2025-05-30 14:05:20');
INSERT INTO `users` VALUES (6, 'kieu', '22130137@st.hcmuaf.edu.vn', '$2a$10$gk/Ei9h6ajZB4kLLSVdpX.oVLykZyFjsWg8XyCklLMsBMM/aRtRXi', 'USER', 1, NULL, '2025-05-25 08:25:34', '2025-05-25 08:26:05');
INSERT INTO `users` VALUES (7, 'kieu', 'kieu36830@gmail.com', '$2a$10$NAOM85zk/i7lB8BZAEjTAeJGlFPPgNjqw8IVMHdqfNwxxVcOWpPn2', 'USER', 1, NULL, '2025-05-25 15:56:24', '2025-05-25 15:56:38');
INSERT INTO `users` VALUES (8, 'Thuy Kieu', 'tvu686021@gmail.com', '$2a$10$m8duEKLxBT7Rx5PsBXW0Ve8/vVnbCVmGquBPZehJGHBps6wMXPfxm', 'ADMIN', 1, '/user_img/e9e511f5-ee42-4bc6-a273-1a349fe7d2b7_upload_17484499704591782483056.tmp', '2025-05-25 15:59:07', '2025-05-28 15:06:54');
INSERT INTO `users` VALUES (10, 'thuy', 'thuy@gmail.com', '$2a$10$vTaNImCI0hxLK/ci0tRC6uPvdBwU0eHF.wLMjWqDQEUN2ACcfkTZS', 'USER', 1, NULL, '2025-05-23 21:48:20', '2025-05-26 19:48:29');
INSERT INTO `users` VALUES (14, 'Binh Anh', 'anh@gmail.com', '$2a$10$g7IoWzF7hxFNgmHQ.UMGmOMyygAL8GrPqplBcINYFbCbW4lfUPwEK', 'USER', 1, NULL, '2025-05-30 12:50:14', '2025-05-30 15:21:14');

SET FOREIGN_KEY_CHECKS = 1;
