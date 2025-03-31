-- MySQL dump 10.13  Distrib 8.0.41, for Linux (x86_64)
--
-- Host: localhost    Database: college_diaries
-- ------------------------------------------------------
-- Server version	8.0.41-0ubuntu0.24.10.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Announcements`
--

DROP TABLE IF EXISTS `Announcements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Announcements` (
  `Announcement_id` int NOT NULL AUTO_INCREMENT,
  `Gymkhana_name` varchar(100) DEFAULT NULL,
  `Announcement_message` text NOT NULL,
  `Timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`Announcement_id`),
  KEY `Gymkhana_name` (`Gymkhana_name`),
  CONSTRAINT `Announcements_ibfk_1` FOREIGN KEY (`Gymkhana_name`) REFERENCES `Gymkhana` (`Gymkhana_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Bazaar`
--

DROP TABLE IF EXISTS `Bazaar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Bazaar` (
  `Item_id` int NOT NULL AUTO_INCREMENT,
  `User_id` int NOT NULL,
  `Item_name` varchar(100) NOT NULL,
  `Count` int DEFAULT '1',
  `Price` int NOT NULL,
  PRIMARY KEY (`Item_id`),
  KEY `User_id` (`User_id`),
  CONSTRAINT `Bazaar_ibfk_1` FOREIGN KEY (`User_id`) REFERENCES `User` (`User_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Gymkhana`
--

DROP TABLE IF EXISTS `Gymkhana`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Gymkhana` (
  `Gymkhana_name` varchar(100) NOT NULL,
  `Pres_id` int DEFAULT NULL,
  `Faculty_id` int DEFAULT NULL,
  `Vice_pres_id` int DEFAULT NULL,
  `Member_count` int DEFAULT '0',
  `Funds` int DEFAULT '0',
  PRIMARY KEY (`Gymkhana_name`),
  KEY `Pres_id` (`Pres_id`),
  KEY `Faculty_id` (`Faculty_id`),
  KEY `Vice_pres_id` (`Vice_pres_id`),
  CONSTRAINT `Gymkhana_ibfk_1` FOREIGN KEY (`Pres_id`) REFERENCES `User` (`User_id`),
  CONSTRAINT `Gymkhana_ibfk_2` FOREIGN KEY (`Faculty_id`) REFERENCES `User` (`User_id`),
  CONSTRAINT `Gymkhana_ibfk_3` FOREIGN KEY (`Vice_pres_id`) REFERENCES `User` (`User_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Story_Section`
--

DROP TABLE IF EXISTS `Story_Section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Story_Section` (
  `User_id` int NOT NULL,
  `User_type` varchar(20) NOT NULL,
  `Posts` text NOT NULL,
  `Like_count` int DEFAULT '0',
  `Dislike_count` int DEFAULT '0',
  `Timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`User_id`,`User_type`,`Timestamp`),
  CONSTRAINT `Story_Section_ibfk_1` FOREIGN KEY (`User_id`, `User_type`) REFERENCES `User` (`User_id`, `User_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User` (
  `User_id` int NOT NULL AUTO_INCREMENT,
  `User_type` varchar(20) NOT NULL,
  `User_name` varchar(100) NOT NULL,
  `Phone` varchar(12) DEFAULT NULL,
  `Email_id` varchar(100) NOT NULL,
  `Department` varchar(100) DEFAULT NULL,
  `Batch` varchar(20) DEFAULT NULL,
  `Branch` varchar(100) DEFAULT NULL,
  `Field` varchar(100) DEFAULT NULL,
  `Announcements_pos` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`User_id`,`User_type`),
  UNIQUE KEY `Email_id` (`Email_id`)
) ENGINE=InnoDB AUTO_INCREMENT=123457 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-31 21:58:04
