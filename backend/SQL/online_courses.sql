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
DELETE FROM `categories`;
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table online_courses.courses: ~5 rows (approximately)
DELETE FROM `courses`;
INSERT INTO `courses` (`id`, `title`, `description`, `category_id`, `image_path`, `number_of_lessons`, `difficulty`, `created_at`) VALUES
	(1, 'Khóa học Java cơ bản', 'Học lập trình Java từ đầu cho người mới bắt đầu.', 1, '/images/java-co-ban.png', 10, 'MEDIUM', '2025-05-03 13:48:10'),
	(2, 'Thiết kế UI/UX nâng cao', 'Khóa học chuyên sâu về thiết kế giao diện.', 2, '/images/thiet-ke-ui-ux-nang-cao.jpg', 8, 'HARD', '2025-05-03 13:48:10'),
	(3, 'Kinh doanh online hiệu quả', 'Cách khởi nghiệp kinh doanh trên nền tảng số.', 3, '/images/kinh-doanh-online.jpg', 12, 'EASY', '2025-05-03 13:48:10'),
	(5, 'Tiếng anh giao tiếp', 'Học Tiếng Anh giao tiếp cho người mới bắt đầu', 4, '/images/tieng-anh-giao-tiep.jpg', 6, 'EASY', '2025-05-08 01:59:52'),
	(6, 'Kỹ năng thuyết trình', 'Kỹ năng thuyết trình hiệu quả giúp chinh phục đám đông', 5, '/images/ky-nang-thuyet-trinh.jpg', 5, 'EASY', '2025-05-08 02:08:05');

-- Dumping structure for table online_courses.enrollments
CREATE TABLE IF NOT EXISTS `enrollments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `enrolled_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`course_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table online_courses.enrollments: ~6 rows (approximately)
DELETE FROM `enrollments`;
INSERT INTO `enrollments` (`id`, `user_id`, `course_id`, `enrolled_at`) VALUES
	(1, 1, 1, '2025-05-03 14:12:17'),
	(2, 2, 1, '2025-05-03 14:12:17'),
	(3, 3, 2, '2025-05-03 14:12:17'),
	(4, 6, 2, '2025-05-25 06:40:01'),
	(5, 6, 5, '2025-05-25 06:40:12'),
	(6, 8, 1, '2025-05-25 09:01:57');

-- Dumping structure for table online_courses.lessons
CREATE TABLE IF NOT EXISTS `lessons` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `content` varchar(255) NOT NULL DEFAULT '',
  `youtube_video_url` varchar(255) DEFAULT NULL,
  `duration_minutes` double NOT NULL DEFAULT 0,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`) USING BTREE,
  KEY `course_id` (`course_id`) USING BTREE,
  CONSTRAINT `lessons_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- Dumping data for table online_courses.lessons: ~2 rows (approximately)
DELETE FROM `lessons`;
INSERT INTO `lessons` (`id`, `course_id`, `title`, `content`, `youtube_video_url`, `duration_minutes`, `created_at`) VALUES
	(1, 1, 'Giới thiệu về Java', 'lap_trinh/java_intro.html', 'https://www.youtube.com/watch?v=VQttXb6qE6k', 14.37, '2025-05-03 14:10:37'),
	(2, 1, 'Cài đặt môi trường Java', 'lap_trinh/java_setup.text', 'https://youtu.be/def456', 20, '2025-05-03 14:10:37');

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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table online_courses.lesson_progress: ~6 rows (approximately)
DELETE FROM `lesson_progress`;
INSERT INTO `lesson_progress` (`id`, `enrollment_id`, `lesson_id`, `is_completed`, `completed_at`) VALUES
	(1, 1, 1, 1, '2025-05-03 14:15:01'),
	(2, 1, 2, 0, NULL),
	(3, 2, 1, 0, NULL),
	(4, 3, 1, 0, NULL),
	(11, 6, 1, 1, NULL),
	(12, 6, 2, 1, NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table online_courses.users: ~7 rows (approximately)
DELETE FROM `users`;
INSERT INTO `users` (`id`, `name`, `email`, `password`, `role`, `is_active`, `created_at`, `updated_at`) VALUES
	(1, 'Nguyễn Văn A', 'a@example.com', 'hashed_password_1', 'USER', 1, '2025-05-03 13:47:59', NULL),
	(2, 'Trần Thị B', 'b@example.com', 'hashed_password_2', 'ADMIN', 1, '2025-05-03 13:47:59', NULL),
	(3, 'Lê Văn C', 'c@example.com', 'hashed_password_3', 'USER', 1, '2025-05-03 13:47:59', NULL),
	(4, 'kieu', 'thuykieu20040@gmail.com', '$2a$10$ywYvAbWfUI4.Dr4/bZFtZORNZKz3/g2VqQ.MRO5WnSg7mDaIQfxXS', 'user', 0, '2025-05-25 01:20:59', '2025-05-25 01:20:59'),
	(6, 'kieu', '22130137@st.hcmuaf.edu.vn', '$2a$10$gk/Ei9h6ajZB4kLLSVdpX.oVLykZyFjsWg8XyCklLMsBMM/aRtRXi', 'user', 1, '2025-05-25 01:25:34', '2025-05-25 01:26:05'),
	(7, 'kieu', 'kieu36830@gmail.com', '$2a$10$NAOM85zk/i7lB8BZAEjTAeJGlFPPgNjqw8IVMHdqfNwxxVcOWpPn2', 'user', 1, '2025-05-25 08:56:24', '2025-05-25 08:56:38'),
	(8, 'thanh', 'tvu686021@gmail.com', '$2a$10$m8duEKLxBT7Rx5PsBXW0Ve8/vVnbCVmGquBPZehJGHBps6wMXPfxm', 'user', 1, '2025-05-25 08:59:07', '2025-05-25 08:59:28');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
