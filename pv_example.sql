-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.24 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
USE `pv_example`;

-- Dumping data for table example.tb_categories: ~4 rows (approximately)
DELETE FROM `tb_categories`;
/*!40000 ALTER TABLE `tb_categories` DISABLE KEYS */;
INSERT INTO `tb_categories` (`category_id`, `category_img_path`, `category_name`, `created_at`, `created_user`, `updated_at`, `updated_user`) VALUES
	(1, NULL, 'Front End', '2020-12-10 11:16:15', 1, NULL, NULL),
	(2, NULL, 'Back End', '2020-12-10 11:16:51', 1, NULL, NULL),
	(3, NULL, 'Stories', '2020-12-10 11:17:06', 1, NULL, NULL),
	(4, NULL, 'Life', '2020-12-10 11:17:19', 1, NULL, NULL);
/*!40000 ALTER TABLE `tb_categories` ENABLE KEYS */;

-- Dumping data for table example.tb_comments: ~0 rows (approximately)
DELETE FROM `tb_comments`;
/*!40000 ALTER TABLE `tb_comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_comments` ENABLE KEYS */;

-- Dumping data for table example.tb_contact: ~0 rows (approximately)
DELETE FROM `tb_contact`;
/*!40000 ALTER TABLE `tb_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_contact` ENABLE KEYS */;

-- Dumping data for table example.tb_page_info: ~0 rows (approximately)
DELETE FROM `tb_page_info`;
/*!40000 ALTER TABLE `tb_page_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_page_info` ENABLE KEYS */;

-- Dumping data for table example.tb_posts: ~5 rows (approximately)
DELETE FROM `tb_posts`;
/*!40000 ALTER TABLE `tb_posts` DISABLE KEYS */;
INSERT INTO `tb_posts` (`post_id`, `created_at`, `updated_at`, `created_user`, `updated_user`, `content`, `has_images_ontop`, `images_layout`, `language`, `level`, `like_cnt`, `published_at`, `summary`, `times_of_view`, `title`, `category_id`) VALUES
	(2, '2021-08-28 11:55:12', NULL, 1, NULL, '<p>CONTENT 1</p>', b'0', 0, 'VNI', 1, 0, '2021-08-28 00:00:00', 'xxxx', 0, 'TITLE 1', 1),
	(4, '2021-08-28 14:51:03', NULL, 1, NULL, '<p>POST 3</p>', b'0', 0, 'VNI', 1, 0, '2021-08-28 14:51:03', 'POST 3 SUMMARY', 0, 'POST 3', 1),
	(5, '2021-08-28 14:53:31', NULL, 1, NULL, '<p>ÁDASDASDSA</p>\n<p>ĐÂSDASDSA</p>\n<p>SADSADAS</p>', b'0', 0, 'VNI', 1, 0, '2021-08-28 00:00:00', 'ÁDASDASDSA\n\nĐÂSDASDSA\n\nSADSADAS', 0, 'POST 4', 2),
	(6, '2021-08-28 14:55:02', NULL, 1, NULL, '<p>ĐÂSDASDAS</p>', b'0', 0, 'EN-US', 1, 0, '2021-08-28 14:55:02', 'ĐÂSDASDAS', 0, 'SADASDAS', 2),
	(7, '2021-08-28 15:13:42', NULL, 1, NULL, '<p>dsfdsfdsfdsf</p>', b'0', 0, 'EN-US', 1, 0, '2021-08-30 00:00:00', 'dsfsdfdsfsd', 0, 'dfsdfsdf', 2);
/*!40000 ALTER TABLE `tb_posts` ENABLE KEYS */;

-- Dumping data for table example.tb_posts_authors: ~0 rows (approximately)
DELETE FROM `tb_posts_authors`;
/*!40000 ALTER TABLE `tb_posts_authors` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_posts_authors` ENABLE KEYS */;

-- Dumping data for table example.tb_posts_tags: ~8 rows (approximately)
DELETE FROM `tb_posts_tags`;
/*!40000 ALTER TABLE `tb_posts_tags` DISABLE KEYS */;
INSERT INTO `tb_posts_tags` (`tag_id`, `post_id`) VALUES
	(3, 2),
	(4, 2),
	(5, 2),
	(3, 5),
	(10, 5),
	(7, 6),
	(12, 7),
	(13, 7);
/*!40000 ALTER TABLE `tb_posts_tags` ENABLE KEYS */;

-- Dumping data for table example.tb_post_images: ~7 rows (approximately)
DELETE FROM `tb_post_images`;
/*!40000 ALTER TABLE `tb_post_images` DISABLE KEYS */;
INSERT INTO `tb_post_images` (`image_id`, `image_path`, `small_image_path`, `standard_image_path`, `post_id`) VALUES
	(2, '/store/upload/post/2/thumbnail/20210828115512233342162021082811551223334216.png', '/store/upload/post/2/thumbnail/263x175-20210828115512233342162021082811551223334216.png', '/store/upload/post/2/thumbnail/585x390-20210828115512233342162021082811551223334216.png', 2),
	(4, '/store/upload/post/4/thumbnail/20210828145103882856412021082814510388285641.png', '/store/upload/post/4/thumbnail/263x175-20210828145103882856412021082814510388285641.png', '/store/upload/post/4/thumbnail/585x390-20210828145103882856412021082814510388285641.png', 4),
	(5, '/store/upload/post/4/thumbnail/20210828145106702887032021082814510670288703.png', '/store/upload/post/4/thumbnail/263x175-20210828145106702887032021082814510670288703.png', '/store/upload/post/4/thumbnail/585x390-20210828145106702887032021082814510670288703.png', 4),
	(6, '/store/upload/post/5/thumbnail/20210828145332246731902021082814533224673190.png', '/store/upload/post/5/thumbnail/263x175-20210828145332246731902021082814533224673190.png', '/store/upload/post/5/thumbnail/585x390-20210828145332246731902021082814533224673190.png', 5),
	(7, '/store/upload/post/6/thumbnail/20210828145502409809312021082814550240980931.png', '/store/upload/post/6/thumbnail/263x175-20210828145502409809312021082814550240980931.png', '/store/upload/post/6/thumbnail/585x390-20210828145502409809312021082814550240980931.png', 6),
	(8, '/store/upload/post/7/thumbnail/20210828151342951673972021082815134295167397.png', '/store/upload/post/7/thumbnail/263x175-20210828151342951673972021082815134295167397.png', '/store/upload/post/7/thumbnail/585x390-20210828151342951673972021082815134295167397.png', 7),
	(9, '/store/upload/post/7/thumbnail/20210828151345886837002021082815134588683700.png', '/store/upload/post/7/thumbnail/263x175-20210828151345886837002021082815134588683700.png', '/store/upload/post/7/thumbnail/585x390-20210828151345886837002021082815134588683700.png', 7);
/*!40000 ALTER TABLE `tb_post_images` ENABLE KEYS */;

-- Dumping data for table example.tb_roles: ~3 rows (approximately)
DELETE FROM `tb_roles`;
/*!40000 ALTER TABLE `tb_roles` DISABLE KEYS */;
INSERT INTO `tb_roles` (`role_id`, `name`) VALUES
	(1, 'ROLE_SUPPER_ADMIN'),
	(2, 'ROLE_ADMIN'),
	(3, 'ROLE_USER');
/*!40000 ALTER TABLE `tb_roles` ENABLE KEYS */;

-- Dumping data for table example.tb_tags: ~7 rows (approximately)
DELETE FROM `tb_tags`;
/*!40000 ALTER TABLE `tb_tags` DISABLE KEYS */;
INSERT INTO `tb_tags` (`tag_id`, `tag`) VALUES
	(10, 'DD'),
	(7, 'ddd'),
	(13, 'fdsfs'),
	(12, 'fđ'),
	(3, 'xxx'),
	(4, 'yyy'),
	(5, 'zzz');
/*!40000 ALTER TABLE `tb_tags` ENABLE KEYS */;

-- Dumping data for table example.tb_users: ~2 rows (approximately)
DELETE FROM `tb_users`;
/*!40000 ALTER TABLE `tb_users` DISABLE KEYS */;
INSERT INTO `tb_users` (`user_id`, `address`, `avatar_img`, `city`, `company_name`, `country`, `created_at`, `email`, `enabled`, `full_name`, `join_date`, `note`, `occupation`, `password`, `phone`, `provider`, `provider_id`, `social_avatar_url`, `summary`, `type`, `updated_at`, `username`) VALUES
	(1, 'Duy Xuyên, Quảng Nam', '/store/upload/user/tranphucvinh/avatar/2021031110475162423984_abc.jfif', NULL, 'Softone', 'Viet Nam', '2020-12-04 15:14:06', 'vinhtranphuc@gmail.com', b'1', 'Trần Phúc Vinh', '2020-12-04 15:14:45', NULL, NULL, '$2a$10$A7F8CntAh5lojQCnWil6wOEIF4SRDxVcamp.aMB5uR.a0icZ/BtYa', '0382607172', 'local', NULL, NULL, 'FFFFFFSadasdas\ndsadasdsadas', NULL, '2020-12-04 15:14:07', 'tranphucvinh'),
	(4, NULL, NULL, NULL, NULL, NULL, '2020-12-21 09:46:47', NULL, b'1', 'testadmin', '2020-12-21 09:46:47', NULL, NULL, '$2a$10$Fr0bEf46RU676YJ9BkuyvO3c.Qc92YL2sKsJN47hJHzxC4BlwfdwC', NULL, 'local', NULL, NULL, NULL, NULL, '2020-12-21 09:46:47', 'testadmin');
/*!40000 ALTER TABLE `tb_users` ENABLE KEYS */;

-- Dumping data for table example.tb_user_roles: ~2 rows (approximately)
DELETE FROM `tb_user_roles`;
/*!40000 ALTER TABLE `tb_user_roles` DISABLE KEYS */;
INSERT INTO `tb_user_roles` (`user_id`, `role_id`) VALUES
	(1, 1),
	(4, 2);
/*!40000 ALTER TABLE `tb_user_roles` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
