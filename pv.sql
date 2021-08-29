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
USE `pv`;

-- Dumping data for table pv.tb_roles: ~3 rows (approximately)
/*!40000 ALTER TABLE `tb_roles` DISABLE KEYS */;
INSERT IGNORE INTO `tb_roles` (`role_id`, `name`) VALUES
	(1, 'ROLE_SUPPER_ADMIN'),
	(2, 'ROLE_ADMIN'),
	(3, 'ROLE_USER');
/*!40000 ALTER TABLE `tb_roles` ENABLE KEYS */;

-- Dumping data for table pv.tb_users: ~2 rows (approximately)
/*!40000 ALTER TABLE `tb_users` DISABLE KEYS */;
INSERT IGNORE INTO `tb_users` (`user_id`, `address`, `avatar_img`, `city`, `company_name`, `country`, `created_at`, `email`, `enabled`, `full_name`, `join_date`, `note`, `occupation`, `password`, `phone`, `provider`, `provider_id`, `social_avatar_url`, `summary`, `type`, `updated_at`, `username`) VALUES
	(1, 'Duy Xuyên, Quảng Nam', '/store/upload/user/tranphucvinh/avatar/2021031110475162423984_abc.jfif', NULL, 'Softone', 'Viet Nam', '2020-12-04 15:14:06', 'vinhtranphuc@gmail.com', b'1', 'Trần Phúc Vinh', '2020-12-04 15:14:45', NULL, NULL, '$2a$10$A7F8CntAh5lojQCnWil6wOEIF4SRDxVcamp.aMB5uR.a0icZ/BtYa', '0382607172', 'local', NULL, NULL, 'FFFFFFSadasdas\ndsadasdsadas', NULL, '2020-12-04 15:14:07', 'tranphucvinh'),
	(4, NULL, NULL, NULL, NULL, NULL, '2020-12-21 09:46:47', NULL, b'1', 'testadmin', '2020-12-21 09:46:47', NULL, NULL, '$2a$10$Fr0bEf46RU676YJ9BkuyvO3c.Qc92YL2sKsJN47hJHzxC4BlwfdwC', NULL, 'local', NULL, NULL, NULL, NULL, '2020-12-21 09:46:47', 'testadmin');
/*!40000 ALTER TABLE `tb_users` ENABLE KEYS */;

-- Dumping data for table pv.tb_user_roles: ~2 rows (approximately)
/*!40000 ALTER TABLE `tb_user_roles` DISABLE KEYS */;
INSERT IGNORE INTO `tb_user_roles` (`user_id`, `role_id`) VALUES
	(1, 1),
	(4, 2);
/*!40000 ALTER TABLE `tb_user_roles` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
