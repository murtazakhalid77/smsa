-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: smsa
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `custom`
--

DROP TABLE IF EXISTS `custom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `custom` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `custom` varchar(255) DEFAULT NULL,
  `custom_port` varchar(255) DEFAULT NULL,
  `is_present` bit(1) NOT NULL,
  `smsa_fee_vat` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom`
--

LOCK TABLES `custom` WRITE;
/*!40000 ALTER TABLE `custom` DISABLE KEYS */;
insert into custom(id, custom, custom_port, smsa_fee_vat, is_Present) values
(1, 'United Arab Emirates Custom', 'Dubai, AE', 5, 1),
(2, 'United Arab Emirates Custom', 'Abu Dhabi, AE', 5, 1),
(3, 'Saudi Custom', 'Riyadh, Sa', 15, 1),
(4, 'Saudi Arabian Custom', 'Dammam, Sa', 15, 1),
(5, 'Saudi Arabian Custom', 'Jeddah, SA', 15, 1),
(6, 'Saudi Arabian Custom', 'Hinakiya, SA', 15, 1),
(7, 'Bahrain Custom', 'Manama, BH', 0, 1),
(8, 'Jordan Custom', 'Amman, Jordan', 0, 1),
(9, 'Kuwait Custom', 'Kuwait, KW', 15, 1),
(10, 'Qatar Custom', 'Doha, QA', 0, 1),
(11, 'Oman Custom', 'Muscat, OM', 0, 1),
(12, 'United Arab Emirates Custom (JAFZA)', 'Jafaza, AE', 5, 1);
/*!40000 ALTER TABLE `custom` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-05  1:31:53
