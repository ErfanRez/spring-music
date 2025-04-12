CREATE DATABASE  IF NOT EXISTS `spring_music` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `spring_music`;
-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: spring_music
-- ------------------------------------------------------
-- Server version	9.2.0

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
-- Table structure for table `album`
--

DROP TABLE IF EXISTS `album`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `album` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cover_key` varchar(255) DEFAULT NULL,
  `cover_url` varchar(255) DEFAULT NULL,
  `genre` enum('ALTERNATIVE','BLUES','CLASSICAL','COUNTRY','DISCO','ELECTRONIC','FOLK','FUNK','HIP_HOP','INDIE','JAZZ','LATIN','METAL','OPERA','POP','PUNK','RAP','REGGAE','ROCK','R_AND_B','SOUL') DEFAULT NULL,
  `like_count` bigint NOT NULL,
  `price` double DEFAULT NULL,
  `release_date` date DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `artist_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh59c3v9j1b151tfxaricgynvb` (`artist_id`),
  CONSTRAINT `FKh59c3v9j1b151tfxaricgynvb` FOREIGN KEY (`artist_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `album`
--

LOCK TABLES `album` WRITE;
/*!40000 ALTER TABLE `album` DISABLE KEYS */;
INSERT INTO `album` VALUES (10,'2025-04-12 22:27:32.223582','2025-04-12 23:05:37.862158','covers/1744484235451_The_Weeknd_-_Hurry_Up_Tomorrow.png','https://storage.c2.liara.space/spring/covers/1744484235451_The_Weeknd_-_Hurry_Up_Tomorrow.png','R_AND_B',1,36,'2025-04-30','Hurry Up Tomorrow',8),(11,'2025-04-13 00:21:05.110048',NULL,'covers/1744491030569_artworks-000009870142-0jk009-t500x500.jpg','https://storage.c2.liara.space/spring/covers/1744491030569_artworks-000009870142-0jk009-t500x500.jpg','CLASSICAL',0,105,'2025-04-21','Golchin',10);
/*!40000 ALTER TABLE `album` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `song`
--

DROP TABLE IF EXISTS `song`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `song` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `audio_key` varchar(255) DEFAULT NULL,
  `audio_url` varchar(255) DEFAULT NULL,
  `cover_key` varchar(255) DEFAULT NULL,
  `cover_url` varchar(255) DEFAULT NULL,
  `genre` enum('ALTERNATIVE','BLUES','CLASSICAL','COUNTRY','DISCO','ELECTRONIC','FOLK','FUNK','HIP_HOP','INDIE','JAZZ','LATIN','METAL','OPERA','POP','PUNK','RAP','REGGAE','ROCK','R_AND_B','SOUL') DEFAULT NULL,
  `like_count` bigint NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `album_id` bigint DEFAULT NULL,
  `artist_id` bigint NOT NULL,
  `duration` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKg94alqytq5u18plid5w17sjby` (`audio_key`),
  UNIQUE KEY `UK81e9y03vwry04r8592hk2cnsl` (`audio_url`),
  KEY `FKrcjmk41yqj3pl3iyii40niab0` (`album_id`),
  KEY `FK7oarm0rg2cmcostoym9tovqtk` (`artist_id`),
  CONSTRAINT `FK7oarm0rg2cmcostoym9tovqtk` FOREIGN KEY (`artist_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKrcjmk41yqj3pl3iyii40niab0` FOREIGN KEY (`album_id`) REFERENCES `album` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `song`
--

LOCK TABLES `song` WRITE;
/*!40000 ALTER TABLE `song` DISABLE KEYS */;
INSERT INTO `song` VALUES (32,'2025-04-12 22:27:32.246635','2025-04-12 23:05:05.142477','songs/1744484235676_The-Weeknd-Timeless-192.mp3','https://storage.c2.liara.space/spring/songs/1744484235676_The-Weeknd-Timeless-192.mp3','covers/1744484235451_The_Weeknd_-_Hurry_Up_Tomorrow.png','https://storage.c2.liara.space/spring/covers/1744484235451_The_Weeknd_-_Hurry_Up_Tomorrow.png','R_AND_B',2,'Timeless',10,8,'4:16'),(33,'2025-04-12 22:27:32.253736',NULL,'songs/1744484239792_The-Weeknd-Given-Up-On-Me-320.mp3','https://storage.c2.liara.space/spring/songs/1744484239792_The-Weeknd-Given-Up-On-Me-320.mp3','covers/1744484235451_The_Weeknd_-_Hurry_Up_Tomorrow.png','https://storage.c2.liara.space/spring/covers/1744484235451_The_Weeknd_-_Hurry_Up_Tomorrow.png','R_AND_B',0,'Given Up on Me',10,8,'5:55'),(34,'2025-04-12 22:27:32.255519',NULL,'songs/1744484243301_The-Weeknd-Without-a-Warning-320.mp3','https://storage.c2.liara.space/spring/songs/1744484243301_The-Weeknd-Without-a-Warning-320.mp3','covers/1744484235451_The_Weeknd_-_Hurry_Up_Tomorrow.png','https://storage.c2.liara.space/spring/covers/1744484235451_The_Weeknd_-_Hurry_Up_Tomorrow.png','R_AND_B',0,'Without a Warning',10,8,'4:58'),(35,'2025-04-12 22:27:32.256776',NULL,'songs/1744484246130_The-Weeknd-Red-Terror-320.mp3','https://storage.c2.liara.space/spring/songs/1744484246130_The-Weeknd-Red-Terror-320.mp3','covers/1744484235451_The_Weeknd_-_Hurry_Up_Tomorrow.png','https://storage.c2.liara.space/spring/covers/1744484235451_The_Weeknd_-_Hurry_Up_Tomorrow.png','R_AND_B',0,'Red Terror',10,8,'3:52'),(36,'2025-04-12 22:27:32.257938',NULL,'songs/1744484248818_The-Weeknd-The-Abyss-320.mp3','https://storage.c2.liara.space/spring/songs/1744484248818_The-Weeknd-The-Abyss-320.mp3','covers/1744484235451_The_Weeknd_-_Hurry_Up_Tomorrow.png','https://storage.c2.liara.space/spring/covers/1744484235451_The_Weeknd_-_Hurry_Up_Tomorrow.png','R_AND_B',0,'The Abyss',10,8,'4:43'),(37,'2025-04-12 22:46:35.376015','2025-04-12 23:05:46.405496','tracks/1744485390770_Swedish-House-Mafia-The-Weeknd-Moth-To-A-Flame-320.mp3','https://storage.c2.liara.space/spring/tracks/1744485390770_Swedish-House-Mafia-The-Weeknd-Moth-To-A-Flame-320.mp3','covers/1744485395078_ab67616d0000b273ade87e5f9c3764f0a1e5df64.jpeg','https://storage.c2.liara.space/spring/covers/1744485395078_ab67616d0000b273ade87e5f9c3764f0a1e5df64.jpeg','R_AND_B',2,'Moth to a Flame',NULL,8,'3:54'),(39,'2025-04-12 23:10:31.657810','2025-04-12 23:11:23.869047','tracks/1744486824285_The-Weeknd-Less-Than-Zero-320.mp3','https://storage.c2.liara.space/spring/tracks/1744486824285_The-Weeknd-Less-Than-Zero-320.mp3','covers/1744486831317_b7bc8e153398123.Y3JvcCwxMDgwLDg0NCwwLDExNw.jpg','https://storage.c2.liara.space/spring/covers/1744486831317_b7bc8e153398123.Y3JvcCwxMDgwLDg0NCwwLDExNw.jpg','R_AND_B',1,'Less than Zero',NULL,8,'3:32'),(40,'2025-04-12 23:21:00.078580','2025-04-12 23:21:17.930168','tracks/1744487456929_Gesaffelstein-The-Weeknd-Lost-In-The-Fire-320.mp3','https://storage.c2.liara.space/spring/tracks/1744487456929_Gesaffelstein-The-Weeknd-Lost-In-The-Fire-320.mp3','covers/1744487459903_ab67616d0000b27396cc1a57cf2989cdd0151d25.jpeg','https://storage.c2.liara.space/spring/covers/1744487459903_ab67616d0000b27396cc1a57cf2989cdd0151d25.jpeg','R_AND_B',1,'Lost in the Fire',NULL,8,'3:22'),(41,'2025-04-12 23:39:22.136170','2025-04-12 23:44:19.579405','tracks/1744488560695_Moein_Elahe_naz_Noyan_Remix.mp3','https://storage.c2.liara.space/spring/tracks/1744488560695_Moein_Elahe_naz_Noyan_Remix.mp3','covers/1744488561969_Elaheye-Naz_Moein.jpg','https://storage.c2.liara.space/spring/covers/1744488561969_Elaheye-Naz_Moein.jpg','POP',1,'Elahe Naz',NULL,9,'0:00'),(42,'2025-04-12 23:43:39.343240','2025-04-12 23:44:17.994213','tracks/1744488816265_Moein - Khooneye Arezoo [320].mp3','https://storage.c2.liara.space/spring/tracks/1744488816265_Moein - Khooneye Arezoo [320].mp3','covers/1744488819253_hs-moein-khooneye-arezoo.jpg','https://storage.c2.liara.space/spring/covers/1744488819253_hs-moein-khooneye-arezoo.jpg','CLASSICAL',1,'Khaneye Arezoo',NULL,9,'3:33'),(43,'2025-04-12 23:55:32.057469','2025-04-12 23:55:44.818087','tracks/1744489527318_Moein_–_Kabeh[1].mp3','https://storage.c2.liara.space/spring/tracks/1744489527318_Moein_–_Kabeh[1].mp3','covers/1744489531826_Kabeh_Moein.jpg','https://storage.c2.liara.space/spring/covers/1744489531826_Kabeh_Moein.jpg','CLASSICAL',1,'Kabeh',NULL,9,'8:21'),(44,'2025-04-13 00:21:05.145062',NULL,'songs/1744491031053_Hayedeh_-_Nargese_Shiraz[1].mp3','https://storage.c2.liara.space/spring/songs/1744491031053_Hayedeh_-_Nargese_Shiraz[1].mp3','covers/1744491030569_artworks-000009870142-0jk009-t500x500.jpg','https://storage.c2.liara.space/spring/covers/1744491030569_artworks-000009870142-0jk009-t500x500.jpg','CLASSICAL',0,'Narges Shiraz',11,10,'5:09'),(45,'2025-04-13 00:21:05.162879',NULL,'songs/1744491036162_Hayedeh_-_Bahaneh[1].mp3','https://storage.c2.liara.space/spring/songs/1744491036162_Hayedeh_-_Bahaneh[1].mp3','covers/1744491030569_artworks-000009870142-0jk009-t500x500.jpg','https://storage.c2.liara.space/spring/covers/1744491030569_artworks-000009870142-0jk009-t500x500.jpg','CLASSICAL',0,'Bahaneh',11,10,'5:48'),(46,'2025-04-13 00:21:05.165347',NULL,'songs/1744491041745_Hayedeh-_Nagoo_Nemiam[1].mp3','https://storage.c2.liara.space/spring/songs/1744491041745_Hayedeh-_Nagoo_Nemiam[1].mp3','covers/1744491030569_artworks-000009870142-0jk009-t500x500.jpg','https://storage.c2.liara.space/spring/covers/1744491030569_artworks-000009870142-0jk009-t500x500.jpg','CLASSICAL',0,'Nagoo Nemiam',11,10,'6:30'),(47,'2025-04-13 00:21:05.167384',NULL,'songs/1744491047661_Hayedeh_-_Shanehayat[1].mp3','https://storage.c2.liara.space/spring/songs/1744491047661_Hayedeh_-_Shanehayat[1].mp3','covers/1744491030569_artworks-000009870142-0jk009-t500x500.jpg','https://storage.c2.liara.space/spring/covers/1744491030569_artworks-000009870142-0jk009-t500x500.jpg','CLASSICAL',0,'Shanehayat',11,10,'8:51'),(48,'2025-04-13 00:21:05.168458',NULL,'songs/1744491057842_Hayedeh-Saghar-Hasti[1].mp3','https://storage.c2.liara.space/spring/songs/1744491057842_Hayedeh-Saghar-Hasti[1].mp3','covers/1744491030569_artworks-000009870142-0jk009-t500x500.jpg','https://storage.c2.liara.space/spring/covers/1744491030569_artworks-000009870142-0jk009-t500x500.jpg','CLASSICAL',0,'Saghar Hasti',11,10,'6:17');
/*!40000 ALTER TABLE `song` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `profile_key` varchar(255) DEFAULT NULL,
  `profile_url` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','ARTIST','USER') DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`),
  UNIQUE KEY `UKn4swgcf30j6bmtb4l4cjryuym` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'2025-03-10 02:37:01.524339',NULL,'admin@example.com',NULL,NULL,NULL,'$2a$10$SIvIJZKZAqCKW2T0/fAh9OfvAJ3jv0abx3gE2fAq0wOSaFU/zWtMi',NULL,NULL,'ADMIN','admin'),(2,'2025-03-10 02:37:01.638478',NULL,'test@example.com',NULL,NULL,NULL,'$2a$10$Qw1R6/qisW/jL0LRjWO/k.4JWeiQADKi7MqV3AZHnD7WT190sOuiq',NULL,NULL,'USER','test'),(3,'2025-03-10 02:37:53.287730',NULL,'user@gmail.com',NULL,NULL,NULL,'$2a$10$X5HKiJkbF5dmIwZu32lVBuRDc/7hG23Y/a4GvKBO7yflUpwgXGTiK',NULL,NULL,'USER','user'),(8,'2025-04-12 22:23:06.595612',NULL,'test@gmail.com','Abel','Makkonen','The Weeknd','$2a$10$5LisxozN9YKAqW.clJ4ndOw/4DKonHh36RMMBFsNcJkvWADr.srWy','profiles/1744483986057_the-weeknds-new-album-v0-m20al2dplmge1.webp','https://storage.c2.liara.space/spring/profiles/1744483986057_the-weeknds-new-album-v0-m20al2dplmge1.webp','ARTIST','weeknd'),(9,'2025-04-12 23:36:46.538310',NULL,'moein@gmail.com','Nasrollah','Moein','Moein','$2a$10$a7oMPr86UhKCaEIZbylEMeW5tgmn2XqCyRDePEh3JTwKRoWLbABeS','profiles/1744488406137_1500051_570.jpg','https://storage.c2.liara.space/spring/profiles/1744488406137_1500051_570.jpg','ARTIST','moein'),(10,'2025-04-13 00:10:27.262427',NULL,'hayedeh@gmail.com','Masoumeh','Dadehbala ','Hayedeh','$2a$10$I.xli.JuPXvgrjLaVJhVieNhqaUi8P/8k28iLxsL/eLX6n7LGR4E6','profiles/1744490426606_img-20220824-164101.jpeg','https://storage.c2.liara.space/spring/profiles/1744490426606_img-20220824-164101.jpeg','ARTIST','hayedeh');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_favorite_albums`
--

DROP TABLE IF EXISTS `user_favorite_albums`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_favorite_albums` (
  `user_id` bigint NOT NULL,
  `album_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`album_id`),
  KEY `FKk2y74hys7yr4m0a155mikf4x4` (`album_id`),
  CONSTRAINT `FKk2y74hys7yr4m0a155mikf4x4` FOREIGN KEY (`album_id`) REFERENCES `album` (`id`),
  CONSTRAINT `FKlycsokmgrpoitpwobnl5uyt2v` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_favorite_albums`
--

LOCK TABLES `user_favorite_albums` WRITE;
/*!40000 ALTER TABLE `user_favorite_albums` DISABLE KEYS */;
INSERT INTO `user_favorite_albums` VALUES (1,10);
/*!40000 ALTER TABLE `user_favorite_albums` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_favorite_songs`
--

DROP TABLE IF EXISTS `user_favorite_songs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_favorite_songs` (
  `user_id` bigint NOT NULL,
  `song_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`song_id`),
  KEY `FKb8898sg2ydyiv8ecogx02d36r` (`song_id`),
  CONSTRAINT `FKb8898sg2ydyiv8ecogx02d36r` FOREIGN KEY (`song_id`) REFERENCES `song` (`id`),
  CONSTRAINT `FKr3nij89jinsscylfwmqutfk4t` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_favorite_songs`
--

LOCK TABLES `user_favorite_songs` WRITE;
/*!40000 ALTER TABLE `user_favorite_songs` DISABLE KEYS */;
INSERT INTO `user_favorite_songs` VALUES (1,32),(8,32),(1,37),(8,37),(8,39),(8,40),(9,41),(9,42),(9,43);
/*!40000 ALTER TABLE `user_favorite_songs` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-13  1:07:33
