-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.32-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.10.0.7000
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for online_courses
CREATE DATABASE IF NOT EXISTS `online_courses` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;
USE `online_courses`;

-- Dumping structure for table online_courses.categories
CREATE TABLE IF NOT EXISTS `categories` (
                                            `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
    ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table online_courses.categories: ~5 rows (approximately)
INSERT INTO `categories` (`id`, `name`) VALUES
                                            (3, 'Kinh doanh'),
                                            (5, 'Kỹ năng mềm'),
                                            (1, 'Lập trình'),
                                            (4, 'Ngoại ngữ'),
                                            (2, 'Thiết kế');

-- Dumping structure for table online_courses.courses
CREATE TABLE IF NOT EXISTS `courses` (
                                         `id` int(11) NOT NULL AUTO_INCREMENT,
    `title` varchar(255) NOT NULL,
    `description` text DEFAULT NULL,
    `category_id` int(11) NOT NULL,
    `image_path` varchar(255) NOT NULL,
    `number_of_lessons` int(11) NOT NULL DEFAULT 0,
    `difficulty` enum('EASY','MEDIUM','HARD') NOT NULL,
    `created_at` timestamp NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`id`),
    KEY `category_id` (`category_id`),
    CONSTRAINT `courses_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table online_courses.courses: ~5 rows (approximately)
INSERT INTO `courses` (`id`, `title`, `description`, `category_id`, `image_path`, `number_of_lessons`, `difficulty`, `created_at`) VALUES
                                                                                                                                       (1, 'Lập trình Java (Java Core)', 'Khóa học phù hợp cho người mới bắt đầu học lập trình hoặc những ai muốn củng cố kiến thức Java căn bản một cách hệ thống, qua các bài giảng lý thuyết kết hợp thực hành chi tiết.', 1, 'https://titv.vn/wp-content/uploads/2023/01/Lap-trinh-Java-870x440.png', 10, 'MEDIUM', '2025-05-03 13:48:10'),
                                                                                                                                       (2, 'Thiết kế UI/UX nâng cao', 'Khóa học chuyên sâu về thiết kế giao diện.', 2, '/images/thiet-ke-ui-ux-nang-cao.jpg', 8, 'HARD', '2025-05-03 13:48:10'),
                                                                                                                                       (3, 'Kinh doanh online hiệu quả', 'Cách khởi nghiệp kinh doanh trên nền tảng số.', 3, '/images/kinh-doanh-online.jpg', 12, 'EASY', '2025-05-03 13:48:10'),
                                                                                                                                       (5, 'Tiếng anh giao tiếp', 'Học Tiếng Anh giao tiếp cho người mới bắt đầu', 4, '/images/tieng-anh-giao-tiep.jpg', 6, 'EASY', '2025-05-08 01:59:52'),
                                                                                                                                       (6, 'Kỹ năng thuyết trình', 'Kỹ năng thuyết trình hiệu quả giúp chinh phục đám đông', 5, '/images/ky-nang-thuyet-trinh.jpg', 5, 'EASY', '2025-05-08 02:08:05');

-- Dumping structure for table online_courses.enrollments
CREATE TABLE IF NOT EXISTS `enrollments` (
                                             `id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` int(11) DEFAULT NULL,
    `course_id` int(11) NOT NULL,
    `enrolled_at` timestamp NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_id` (`user_id`,`course_id`),
    KEY `course_id` (`course_id`),
    CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table online_courses.enrollments: ~4 rows (approximately)
INSERT INTO `enrollments` (`id`, `user_id`, `course_id`, `enrolled_at`) VALUES
                                                                            (1, 3, 1, '2025-05-03 14:12:17'),
                                                                            (2, 2, 1, '2025-05-03 14:12:17'),
                                                                            (3, 3, 2, '2025-05-03 14:12:17'),
                                                                            (4, 1, 2, '2025-05-23 16:42:52');

-- Dumping structure for table online_courses.lessons
CREATE TABLE IF NOT EXISTS `lessons` (
                                         `id` int(11) NOT NULL AUTO_INCREMENT,
    `course_id` int(11) NOT NULL,
    `title` varchar(255) NOT NULL,
    `content` varchar(255) DEFAULT '',
    `youtube_video_url` varchar(255) DEFAULT NULL,
    `duration_minutes` double NOT NULL,
    `created_at` timestamp NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`id`),
    KEY `course_id` (`course_id`),
    CONSTRAINT `lessons_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table online_courses.lessons: ~9 rows (approximately)
INSERT INTO `lessons` (`id`, `course_id`, `title`, `content`, `youtube_video_url`, `duration_minutes`, `created_at`) VALUES
                                                                                                                         (1, 1, 'Java 01. Vì sao học lập trình Java', '', 'https://www.youtube.com/watch?v=xfOp0izFnu0&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F', 18.27, '2025-05-03 14:10:37'),
                                                                                                                         (2, 1, 'Java 02. Cài đặt JDK và Eclipse', '', 'https://www.youtube.com/watch?v=ayA1Lz2qEZo&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=2', 16.67, '2025-05-03 14:10:37'),
                                                                                                                         (3, 1, 'Java 03. Cấu trúc của một lớp Java', '', 'https://www.youtube.com/watch?v=6Gbxt2Sox7k&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=3', 10.72, '2025-05-23 04:58:18'),
                                                                                                                         (4, 1, 'Java 04. Cách khai báo biến trong Java', '', 'https://www.youtube.com/watch?v=zEbraQ5vIaU&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=4', 10.42, '2025-05-23 05:00:23'),
                                                                                                                         (5, 1, 'Java 05 . Kiểu dữ liệu trong Java', '', 'https://www.youtube.com/watch?v=S29I8oXEXf8&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=5', 8.98, '2025-05-23 05:01:04'),
                                                                                                                         (6, 1, 'Java 06. Hằng số trong Java', '', 'https://www.youtube.com/watch?v=IrtwjVY18do&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=6', 4.6, '2025-05-23 05:01:48'),
                                                                                                                         (7, 1, 'Java 07 . Cách ghi chú trong Java', '', 'https://www.youtube.com/watch?v=jgzgkUbK35M&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=7', 8.92, '2025-05-23 05:02:33'),
                                                                                                                         (8, 1, 'Java 08. Cách kiểm tra và xử lý lỗi biên dịch', '', 'https://www.youtube.com/watch?v=2Zu17CS3288&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=8', 5.93, '2025-05-23 05:03:21'),
                                                                                                                         (9, 1, 'Java 09. Cách nhập dữ liệu từ bán phím', '', 'https://www.youtube.com/watch?v=ymFKMQSeodQ&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=9', 11.32, '2025-05-23 05:05:14'),
                                                                                                                         (12, 1, 'Java 10. Chuyển đổi kiểu dữ liệu trong Java', '', 'https://www.youtube.com/watch?v=BwOt3IeeP64&list=PLyxSzL3F748401hWFgJ8gKMnN6MM8QQ7F&index=10', 12.18, '2025-05-23 05:06:09');

-- Dumping structure for table online_courses.lesson_progress
CREATE TABLE IF NOT EXISTS `lesson_progress` (
                                                 `id` int(11) NOT NULL AUTO_INCREMENT,
    `enrollment_id` int(11) NOT NULL,
    `lesson_id` int(11) NOT NULL,
    `is_completed` tinyint(1) NOT NULL DEFAULT 0,
    `completed_at` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `enrollment_id` (`enrollment_id`,`lesson_id`),
    KEY `lesson_id` (`lesson_id`),
    CONSTRAINT `lesson_progress_ibfk_1` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`id`) ON DELETE CASCADE,
    CONSTRAINT `lesson_progress_ibfk_2` FOREIGN KEY (`lesson_id`) REFERENCES `lessons` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table online_courses.lesson_progress: ~4 rows (approximately)
INSERT INTO `lesson_progress` (`id`, `enrollment_id`, `lesson_id`, `is_completed`, `completed_at`) VALUES
                                                                                                       (1, 1, 5, 1, '2025-05-03 14:15:01'),
                                                                                                       (2, 1, 2, 0, NULL),
                                                                                                       (3, 2, 1, 0, NULL),
                                                                                                       (4, 3, 1, 0, NULL);

-- Dumping structure for table online_courses.users
CREATE TABLE IF NOT EXISTS `users` (
                                       `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    `email` varchar(255) NOT NULL,
    `password` varchar(255) NOT NULL,
    `role` varchar(255) DEFAULT NULL,
    `is_active` tinyint(1) DEFAULT 1,
    `created_at` timestamp NULL DEFAULT current_timestamp(),
    `updated_at` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`)
    ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table online_courses.users: ~4 rows (approximately)
INSERT INTO `users` (`id`, `name`, `email`, `password`, `role`, `is_active`, `created_at`, `updated_at`) VALUES
                                                                                                             (1, 'Nguyễn Văn A', 'a@example.com', 'hashed_password_1', 'USER', 1, '2025-05-03 13:47:59', NULL),
                                                                                                             (2, 'Trần Thị B', 'b@example.com', 'hashed_password_2', 'ADMIN', 1, '2025-05-03 13:47:59', NULL),
                                                                                                             (3, 'Lê Văn C', 'c@example.com', 'hashed_password_3', 'USER', 1, '2025-05-03 13:47:59', NULL),
                                                                                                             (4, 'thuy', 'thuy@gmail.com', '$2a$10$vTaNImCI0hxLK/ci0tRC6uPvdBwU0eHF.wLMjWqDQEUN2ACcfkTZS', 'user', 1, '2025-05-23 14:48:20', '2025-05-23 15:05:40');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
