-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: eco_renter
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrator` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `telephone_number` varchar(255) DEFAULT NULL,
  `photo_id` int(11) DEFAULT NULL,
  `user_account_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_2a5vcjo3stlfcwadosjfq49l1` (`user_account_id`),
  UNIQUE KEY `UK_jj3mmcc0vjobqibj67dvuwihk` (`email`),
  KEY `FK_q8xtaapphhoue8f1skpjv81p7` (`photo_id`),
  CONSTRAINT `FK_2a5vcjo3stlfcwadosjfq49l1` FOREIGN KEY (`user_account_id`) REFERENCES `user_account` (`id`),
  CONSTRAINT `FK_q8xtaapphhoue8f1skpjv81p7` FOREIGN KEY (`photo_id`) REFERENCES `photo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `written_moment` datetime(6) NOT NULL,
  `rent_out_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjei8pnwif8vhbu7n0t1nkf5tf` (`rent_out_id`),
  CONSTRAINT `FKjei8pnwif8vhbu7n0t1nkf5tf` FOREIGN KEY (`rent_out_id`) REFERENCES `rent_out` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customization`
--

DROP TABLE IF EXISTS `customization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customization` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `discount_codes` varchar(255) DEFAULT NULL,
  `eco_truki` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gold_level` int(11) NOT NULL,
  `silver_level` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_kwvi1eq3k3nnn2jc10wyci9jv` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `owner`
--

DROP TABLE IF EXISTS `owner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `owner` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `telephone_number` varchar(255) DEFAULT NULL,
  `photo_id` int(11) DEFAULT NULL,
  `user_account_id` int(11) NOT NULL,
  `accumulated_months` int(11) NOT NULL,
  `iban` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hc0nwk401f7t7pohcq2vounjc` (`user_account_id`),
  UNIQUE KEY `UK_kcaoebbgb82ro5cw9nqhw19qb` (`email`),
  KEY `FK_r4iqvt4tk4jpapqdqb9rsfy07` (`photo_id`),
  CONSTRAINT `FK_hc0nwk401f7t7pohcq2vounjc` FOREIGN KEY (`user_account_id`) REFERENCES `user_account` (`id`),
  CONSTRAINT `FK_r4iqvt4tk4jpapqdqb9rsfy07` FOREIGN KEY (`photo_id`) REFERENCES `photo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `photo`
--

DROP TABLE IF EXISTS `photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `photo` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `structure` longblob,
  `suffix` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rent_out`
--

DROP TABLE IF EXISTS `rent_out`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rent_out` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `is_active` bit(1) NOT NULL,
  `month` int(11) NOT NULL,
  `start_date` datetime(6) NOT NULL,
  `renter_id` int(11) NOT NULL,
  `smallholding_id` int(11) NOT NULL,
  `valuation_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2w4idcn2jnkaxbh3r2pebf5ie` (`renter_id`),
  KEY `FKi1vsr0uw536n5xewcq6hupv0b` (`smallholding_id`),
  KEY `FKbw5w7l7d1djjia82bwqjkjndr` (`valuation_id`),
  CONSTRAINT `FK2w4idcn2jnkaxbh3r2pebf5ie` FOREIGN KEY (`renter_id`) REFERENCES `renter` (`id`),
  CONSTRAINT `FKbw5w7l7d1djjia82bwqjkjndr` FOREIGN KEY (`valuation_id`) REFERENCES `valuation` (`id`),
  CONSTRAINT `FKi1vsr0uw536n5xewcq6hupv0b` FOREIGN KEY (`smallholding_id`) REFERENCES `smallholding` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `renter`
--

DROP TABLE IF EXISTS `renter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `renter` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `telephone_number` varchar(255) DEFAULT NULL,
  `photo_id` int(11) DEFAULT NULL,
  `user_account_id` int(11) NOT NULL,
  `iban` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_32merbwan7h6a2mgwcxpyxlts` (`user_account_id`),
  UNIQUE KEY `UK_8lx5melb9aiqsx6uaw8ssbb5r` (`email`),
  KEY `FK_2mp2pw1716eb4hf20jtowwx3s` (`photo_id`),
  CONSTRAINT `FK_2mp2pw1716eb4hf20jtowwx3s` FOREIGN KEY (`photo_id`) REFERENCES `photo` (`id`),
  CONSTRAINT `FK_32merbwan7h6a2mgwcxpyxlts` FOREIGN KEY (`user_account_id`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `smallholding`
--

DROP TABLE IF EXISTS `smallholding`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smallholding` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `farming_type` varchar(255) DEFAULT NULL,
  `images` varchar(255) DEFAULT NULL,
  `is_available` bit(1) NOT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `locality` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `max_duration` int(11) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `province` varchar(255) DEFAULT NULL,
  `size` int(11) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `owner_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt41w5r3e6kec8wt0oq2r20cs1` (`owner_id`),
  CONSTRAINT `FKt41w5r3e6kec8wt0oq2r20cs1` FOREIGN KEY (`owner_id`) REFERENCES `owner` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `smallholding_photos`
--

DROP TABLE IF EXISTS `smallholding_photos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smallholding_photos` (
  `smallholding_id` int(11) NOT NULL,
  `photos_id` int(11) NOT NULL,
  UNIQUE KEY `UK_33kylxybbnpwcxp2myq46wk2w` (`photos_id`),
  KEY `FK19104dxu3xkf40ux1yah94y3x` (`smallholding_id`),
  CONSTRAINT `FK19104dxu3xkf40ux1yah94y3x` FOREIGN KEY (`smallholding_id`) REFERENCES `smallholding` (`id`),
  CONSTRAINT `FKb78d844pinbt9yblhjx7svm5d` FOREIGN KEY (`photos_id`) REFERENCES `photo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_account`
--

DROP TABLE IF EXISTS `user_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_account` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `is_banned` bit(1) NOT NULL,
  `password` varchar(60) DEFAULT NULL,
  `username` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_castjbvpeeus0r8lbpehiu0e4` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_account_authorities`
--

DROP TABLE IF EXISTS `user_account_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_account_authorities` (
  `user_account_id` int(11) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  KEY `FKqg5yuqktw7kjmodb7k1rg3f2o` (`user_account_id`),
  CONSTRAINT `FKqg5yuqktw7kjmodb7k1rg3f2o` FOREIGN KEY (`user_account_id`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `valuation`
--

DROP TABLE IF EXISTS `valuation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `valuation` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `mark` int(11) NOT NULL,
  `valuation_moment` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-15 16:18:21
