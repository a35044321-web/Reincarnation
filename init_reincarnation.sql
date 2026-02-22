CREATE DATABASE  IF NOT EXISTS `reincarnation` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `reincarnation`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: reincarnation
-- ------------------------------------------------------
-- Server version	8.0.44

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
-- Table structure for table `characters`
--

DROP TABLE IF EXISTS `characters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `characters` (
  `characters_id` int unsigned NOT NULL AUTO_INCREMENT,
  `users_id` int unsigned NOT NULL,
  `characters_name` varchar(45) NOT NULL,
  `job_type` varchar(50) DEFAULT '體修',
  `realm` int unsigned NOT NULL,
  `exp` bigint unsigned NOT NULL,
  `spirit_stones` int unsigned NOT NULL,
  `health` int unsigned NOT NULL,
  `stamina` int unsigned NOT NULL,
  `base_atk` int unsigned NOT NULL DEFAULT '10',
  `base_def` int unsigned NOT NULL DEFAULT '5',
  `kill_points` decimal(10,2) NOT NULL DEFAULT '0.00',
  `is_cultivating` tinyint NOT NULL DEFAULT '0',
  `last_save_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `avatar_path` varchar(255) DEFAULT NULL,
  `weapon_id` int unsigned DEFAULT '0',
  `armor_id` int unsigned DEFAULT '0',
  PRIMARY KEY (`characters_id`),
  UNIQUE KEY `characters_name_UNIQUE` (`characters_name`),
  UNIQUE KEY `users_id_UNIQUE` (`users_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `characters`
--

LOCK TABLES `characters` WRITE;
/*!40000 ALTER TABLE `characters` DISABLE KEYS */;
INSERT INTO `characters` VALUES (1,1,'仙劍','體修',2,1476,0,0,5,50,0,0.00,0,'2026-02-22 14:59:54','man_role_piture1.jpg',3,0),(2,2,'天道執行者','天道',9,1000008,99999,9999,100,999,999,0.00,0,'2026-02-22 13:05:16','man_role_piture1.jpg',0,0);
/*!40000 ALTER TABLE `characters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_events`
--

DROP TABLE IF EXISTS `game_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_events` (
  `game_events_id` int unsigned NOT NULL AUTO_INCREMENT,
  `events_name` varchar(100) NOT NULL,
  `description` text NOT NULL,
  `event_type` varchar(45) NOT NULL,
  `effect_value` int NOT NULL,
  `min_realm` int unsigned NOT NULL,
  `event_image` varchar(255) DEFAULT 'monster_common.png',
  PRIMARY KEY (`game_events_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_events`
--

LOCK TABLES `game_events` WRITE;
/*!40000 ALTER TABLE `game_events` DISABLE KEYS */;
INSERT INTO `game_events` VALUES (2,'山野野狼','一隻普通的野狼。','普通',150,1,'wolf_piture3.jpg'),(3,'山野猩猩','一隻略為強壯的猩猩。','菁英',350,1,'monkey_piture2.jpg'),(4,'山野巨蟒','一條不知年歲的巨蟒。','首領',600,1,'boss_piture1.jpg'),(5,'山野狼王','為這一區統領狼群的狼王。','普通',800,2,'wolf_piture3.jpg'),(6,'山野猩猩首領','為這一區的猩猩首領。','菁英',1500,2,'monkey_piture2.jpg'),(7,'山野靈蟒','吸收日月精華的巨蟒，已非一般野獸。','首領',2800,2,'boss_piture1.jpg');
/*!40000 ALTER TABLE `game_events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `items`
--

DROP TABLE IF EXISTS `items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `items` (
  `item_id` int unsigned NOT NULL AUTO_INCREMENT,
  `item_name` varchar(50) NOT NULL,
  `item_type` varchar(20) NOT NULL COMMENT '裝備/消耗品',
  `atk_bonus` int NOT NULL DEFAULT '0',
  `def_bonus` int NOT NULL DEFAULT '0',
  `min_realm` int unsigned NOT NULL DEFAULT '1' COMMENT '最低裝備境界',
  `description` text,
  `image_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`item_id`),
  UNIQUE KEY `item_name_UNIQUE` (`item_name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `items`
--

LOCK TABLES `items` WRITE;
/*!40000 ALTER TABLE `items` DISABLE KEYS */;
INSERT INTO `items` VALUES (1,'鏽蝕鐵劍(下品)','武器',5,0,1,'路邊撿到的生鏽鐵劍。','sword_piture1.jpg'),(2,'精鋼長劍(中品)','武器',15,0,1,'鐵匠精心打造的長劍。','sword_piture1.jpg'),(3,'流光短刃(上品)','武器',35,0,1,'散發淡淡微光的靈器。','sword_piture1.jpg'),(4,'龍吟古劍(極品)','武器',80,0,1,'傳說中藏有龍氣的絕世寶劍。','sword_piture1.jpg'),(10,'玄鐵重鎧(下品)','護甲',0,15,1,'沉重且紮實的玄鐵防具。','armor_piture1.jpg'),(11,'精鋼軟甲(中品)','護甲',0,35,1,'名家打造，輕便且堅固。','armor_piture1.jpg'),(12,'流光戰袍(上品)','護甲',0,80,1,'織入靈絲，隱有流光閃爍。','armor_piture1.jpg'),(13,'龍鱗神鎧(極品)','護甲',0,180,1,'傳說以真龍逆鱗鑄成，萬法不侵。','armor_piture1.jpg');
/*!40000 ALTER TABLE `items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_items`
--

DROP TABLE IF EXISTS `player_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `player_items` (
  `items_id` int unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int NOT NULL,
  `item_id` int NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`items_id`),
  UNIQUE KEY `idx_player_item_unique` (`player_id`,`item_id`),
  UNIQUE KEY `idx_player_item` (`player_id`,`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_items`
--

LOCK TABLES `player_items` WRITE;
/*!40000 ALTER TABLE `player_items` DISABLE KEYS */;
INSERT INTO `player_items` VALUES (1,1,3,1),(2,2,13,1),(3,1,10,2),(5,1,2,1),(6,1,1,1);
/*!40000 ALTER TABLE `player_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `realm_config`
--

DROP TABLE IF EXISTS `realm_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `realm_config` (
  `realm_level` int unsigned NOT NULL,
  `realm_name` varchar(50) NOT NULL,
  `exp_required` bigint unsigned NOT NULL,
  `base_success_rate` decimal(5,2) NOT NULL DEFAULT '40.00',
  `bonus_per_kill_point` decimal(5,2) NOT NULL DEFAULT '0.50',
  PRIMARY KEY (`realm_level`),
  UNIQUE KEY `realm_name_UNIQUE` (`realm_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `realm_config`
--

LOCK TABLES `realm_config` WRITE;
/*!40000 ALTER TABLE `realm_config` DISABLE KEYS */;
INSERT INTO `realm_config` VALUES (0,'凡人',0,100.00,0.50),(1,'練氣前期',1000,80.00,0.50),(2,'練氣中期',3000,70.00,0.50),(3,'練氣後期',8000,60.00,0.50),(4,'築基初期',20000,50.00,0.50),(5,'築基中期',50000,45.00,0.50),(6,'築基後期',120000,40.00,0.50),(7,'金丹大能',300000,30.00,0.50),(8,'元神真君',800000,20.00,0.50),(9,'九天真仙',2000000,10.00,0.50);
/*!40000 ALTER TABLE `realm_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `users_id` int unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `role` varchar(20) DEFAULT 'PLAYER',
  PRIMARY KEY (`users_id`),
  UNIQUE KEY `account_UNIQUE` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'aaa','1234','ethan','12','32','2026-02-22 12:39:25','PLAYER'),(2,'admin','admin123',NULL,NULL,NULL,'2026-02-22 13:00:04','ADMIN');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-22 15:03:02
