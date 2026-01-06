-- MySQL dump 10.13  Distrib 8.4.7, for Linux (aarch64)
--
-- Host: host.docker.internal    Database: iot_store
-- ------------------------------------------------------
-- Server version	8.4.7

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
-- Current Database: `iot_store`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `iot_store` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `iot_store`;

--
-- Table structure for table `Aisle`
--

DROP TABLE IF EXISTS `Aisle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Aisle` (
  `aisle_id` int NOT NULL,
  `floor` int NOT NULL,
  `store_uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `aisle_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`aisle_id`,`floor`),
  KEY `Asile_Store_FK` (`store_uuid`),
  CONSTRAINT `Asile_Store_FK` FOREIGN KEY (`store_uuid`) REFERENCES `Store` (`UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Aisle`
--

LOCK TABLES `Aisle` WRITE;
/*!40000 ALTER TABLE `Aisle` DISABLE KEYS */;
INSERT INTO `Aisle` VALUES (1,1,'2f234454-cf6d-4a0f-adf2-f4911ba9ffa6','Dairy products'),(2,1,'2f234454-cf6d-4a0f-adf2-f4911ba9ffa6','Taco supplies'),(3,1,'2f234454-cf6d-4a0f-adf2-f4911ba9ffa6','Snacks');
/*!40000 ALTER TABLE `Aisle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Product`
--

DROP TABLE IF EXISTS `Product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Product` (
  `product_id` int NOT NULL,
  `product_name` varchar(100) NOT NULL,
  `product_description` text,
  `product_image` varchar(255) DEFAULT NULL,
  `aisle_id` int NOT NULL,
  `floor` int NOT NULL,
  `product_location` int DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `fk_products_asile` (`aisle_id`,`floor`),
  CONSTRAINT `fk_products_asile` FOREIGN KEY (`aisle_id`, `floor`) REFERENCES `Aisle` (`aisle_id`, `floor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Product`
--

LOCK TABLES `Product` WRITE;
/*!40000 ALTER TABLE `Product` DISABLE KEYS */;
INSERT INTO `Product` VALUES (1,'Milk','Fresh dairy milk, rich in calcium and perfect for everyday use.',NULL,1,1,1001),(2,'Butter','Creamy dairy butter made from fresh cream.',NULL,1,1,1002),(3,'Cream','Versatile cooking cream ideal for sauces, soups, and desserts.',NULL,1,1,1003),(4,'Yogurt','Creamy yogurt with a mild, fresh taste and smooth texture.',NULL,1,1,1004),(5,'Taco sauce','Classic taco sauce perfect for tacos, wraps, and nachos.',NULL,2,1,2001),(6,'Taco spice','Dry taco seasoning mix for easy and tasty taco meals.',NULL,2,1,2002),(7,'Tortilla','Soft wheat tortillas perfect for wraps and tacos.',NULL,2,1,2003),(8,'Nacho cheese dip','Smooth nacho cheese dip perfect for chips and snacks.',NULL,2,1,2004),(9,'Chips','Crispy potato chips with a classic salty taste.',NULL,3,1,3001),(10,'Popcorn','Freshly popped popcorn with a light, crunchy texture.',NULL,3,1,3002),(11,'Nuts','Crunchy mixed nuts, perfect for snacking.',NULL,3,1,3003),(12,'Cheese balls','Crunchy cheese balls with a rich cheesy flavor.',NULL,3,1,3004);
/*!40000 ALTER TABLE `Product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Promotion`
--

DROP TABLE IF EXISTS `Promotion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Promotion` (
  `promotion_id` int NOT NULL AUTO_INCREMENT,
  `discount` varchar(20) DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`promotion_id`),
  KEY `promotions_products_FK` (`product_id`),
  CONSTRAINT `promotions_products_FK` FOREIGN KEY (`product_id`) REFERENCES `Product` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Promotion`
--

LOCK TABLES `Promotion` WRITE;
/*!40000 ALTER TABLE `Promotion` DISABLE KEYS */;
INSERT INTO `Promotion` VALUES (1,'2 for 1',1),(2,'10%',2),(3,'10 kr off',5),(4,'3 for 2',9);
/*!40000 ALTER TABLE `Promotion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Store`
--

DROP TABLE IF EXISTS `Store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Store` (
  `UUID` varchar(255) NOT NULL,
  PRIMARY KEY (`UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Store`
--

LOCK TABLES `Store` WRITE;
/*!40000 ALTER TABLE `Store` DISABLE KEYS */;
INSERT INTO `Store` VALUES ('2f234454-cf6d-4a0f-adf2-f4911ba9ffa6');
/*!40000 ALTER TABLE `Store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'iot_store'
--

--
-- Dumping routines for database 'iot_store'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-17 12:59:15
