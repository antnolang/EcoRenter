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
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES (100,0,'admin1@gmail.com','https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/dossier/5e6b9e535cafe8decee46032/fe-lix-rodri-guez-de-la-fuente.jpg','Name cero','Surname cero','+34 694567234',NULL,32),(101,0,'admin2@gmail.com','','Name uno','Surname uno','+34 694567235',NULL,33),(102,0,'admin3@gmail.com','','Name dos','Surname dos','+34 694567236',NULL,34);
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (600,0,'Text 1','2020-03-05 13:00:00.000000',500),(601,0,'Text 2','2020-03-05 14:00:00.000000',500),(602,0,'Text 3','2020-03-05 15:00:00.000000',500),(603,0,'Text 4','2020-03-05 15:01:00.000000',500),(604,0,'Text 5','2020-03-05 16:30:00.000000',500),(605,0,'Text 6','2020-03-05 17:07:00.000000',500),(606,0,'Text 7','2020-02-20 18:08:00.000000',501),(607,0,'Text 8','2020-02-20 19:09:00.000000',501),(608,0,'Text 9','2020-01-21 10:00:00.000000',504),(609,0,'Text 10','2020-01-21 10:05:00.000000',504);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `customization`
--

LOCK TABLES `customization` WRITE;
/*!40000 ALTER TABLE `customization` DISABLE KEYS */;
INSERT INTO `customization` VALUES (1,0,'','eco_truki uno','ecoRenter@gmail.com',12,3);
/*!40000 ALTER TABLE `customization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (1);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `owner`
--

LOCK TABLES `owner` WRITE;
/*!40000 ALTER TABLE `owner` DISABLE KEYS */;
INSERT INTO `owner` VALUES (200,0,'owner1@hotmail.com','https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/dossier/5e6b9e535cafe8decee46032/fe-lix-rodri-guez-de-la-fuente.jpg','Name uno','Surname uno','654873021',NULL,41,12,'ES9000246912501234567891'),(201,0,'owner2@hotmail.com','','Name dos','Surname dos','+34 654873022',NULL,42,0,''),(202,0,'owner3@hotmail.com','','Name tres','Surname tres','+34 654873023',NULL,43,0,'ES7100302053091234567895'),(203,0,'owner4@hotmail.com','','Name cuatro','Surname cuatro','+34 654873024',NULL,44,0,''),(204,0,'owner5@hotmail.com','','Name cinco','Surname cinco','+34 654873025',NULL,45,0,'ES1000492352082414205416'),(205,0,'owner6@hotmail.com','','Name seis','Surname seis','+34 654873026',NULL,46,0,'');
/*!40000 ALTER TABLE `owner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `photo`
--

LOCK TABLES `photo` WRITE;
/*!40000 ALTER TABLE `photo` DISABLE KEYS */;
/*!40000 ALTER TABLE `photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `rent_out`
--

LOCK TABLES `rent_out` WRITE;
/*!40000 ALTER TABLE `rent_out` DISABLE KEYS */;
INSERT INTO `rent_out` VALUES (500,0,_binary '',1,'2020-02-21 00:00:00.000000',300,400,NULL),(501,0,_binary '',1,'2020-02-20 00:00:00.000000',300,402,NULL),(502,0,_binary '',7,'2019-08-07 00:00:00.000000',302,404,700),(503,0,_binary '',2,'2020-01-21 00:00:00.000000',304,405,701),(504,0,_binary '',1,'2020-02-27 00:00:00.000000',304,407,NULL),(505,0,_binary '\0',6,'2018-04-15 00:00:00.000000',304,403,NULL),(506,0,_binary '\0',6,'2019-05-20 00:00:00.000000',300,400,NULL),(507,0,_binary '\0',6,'2019-05-20 00:00:00.000000',300,400,NULL);
/*!40000 ALTER TABLE `rent_out` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `renter`
--

LOCK TABLES `renter` WRITE;
/*!40000 ALTER TABLE `renter` DISABLE KEYS */;
INSERT INTO `renter` VALUES (300,0,'renter1@yahoo.com','https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/dossier/5e6b9e535cafe8decee46032/fe-lix-rodri-guez-de-la-fuente.jpg','Name uno','Surname uno','+34 680043654',NULL,35,'ES6621000418401234567891'),(301,0,'renter2@yahoo.com','','Name dos','Surname dos','+34 680043655',NULL,36,''),(302,0,'renter3@yahoo.com','','Name tres','Surname tres','+34 680043656',NULL,37,'ES6000491500051234567892'),(303,0,'renter4@yahoo.com','','Name cuatro','Surname cuatro','+34 680043657',NULL,38,''),(304,0,'renter5@yahoo.com','','Name cinco','Surname cinco','+34 680043658',NULL,39,'ES9420805801101234567891'),(305,0,'renter6@yahoo.com','','Name seis','Surname seis','+34 680043659',NULL,40,'');
/*!40000 ALTER TABLE `renter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `smallholding`
--

LOCK TABLES `smallholding` WRITE;
/*!40000 ALTER TABLE `smallholding` DISABLE KEYS */;
INSERT INTO `smallholding` VALUES (400,0,'Address 1','description 1','frutales','https://estaticos.muyinteresante.es/media/cache/253x190_thumb/uploads/images/pyr/55520750c0ea197b3fd51123/correr-lluvia1_0.jpg,https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/article/5e60d8485bafe8798a0afd47/vph-vacuna-1_0.jpg',_binary '\0','40.7127837','Morón','-74.0059413',10,'41710',99.99,'Sevilla',100,'ALQUILADA','Title 1',200),(401,0,'Address 2','description 2','olivar','https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/article/5e46996c5bafe8fb241ac4dd/medusa-picarte.jpg,https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/gallery/5d9208eb5cafe81a0f3c986a/delfin0.jpg',_binary '','37.1824646','Utrera','-5.7817506',3,'41710',199.99,'Sevilla',200,'NO ALQUILADA','Title 2',200),(402,0,'Address 3','description 3','Frutos secos','https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/article/5e5e4a3b5cafe82459780259/nino-mochila.jpg,https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/article/5e60d8485bafe8798a0afd47/vph-vacuna-1_0.jpg',_binary '\0','36.2757882','Conil','-6.0889112',1,'42000',199.99,'Malaga',200,'ALQUILADA','Title 3',200),(403,0,'Address 4','description 4','olivar','https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/dossier/5d821ae55cafe8af888ea771/plastico.jpg,https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/article/5e5668815cafe887f4dd01f9/pajaros-tv.jpg',_binary '','37.1824646','Getafe','-3.7302679',2,'45678',499.99,'Madrid',250,'NO ALQUILADA','Title 4',200),(404,0,'Address 5','description 5','hortalizas','https://estaticos.muyinteresante.es/media/cache/760x570_thumb/uploads/images/article/5e5767525bafe82274623981/nieve-sangre.jpg',_binary '\0','43.4620412','Santander','-3.8099719',10,'21344',100,'Santander',50,'ALQUILADA','Title 5',200),(405,0,'Address 6','description 6','viñedos','https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/article/5e54ff935cafe897d809909a/serpiente-arcoiris.jpg',_binary '','37.1824646','Utrera','-5.7817516',3,'41710',1000,'Sevilla',75,'NO ALQUILADA','Title 6',200),(406,0,'Address 7','description 7','olivar','https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/article/5e531b2c5bafe8bf90a4d1e8/caracol-greta-thunberg_0.jpg',_binary '\0','39.4699014','Villarejo','-0.3759513',2,'54666',199.99,'Valencia',200,'ALQUILADA','Title 7',202),(407,0,'Address 8','description 8','cultivos industriales','https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/article/5e4d23485cafe84e4e985ba6/craneo-cabra.jpg,https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/test/58592ed55bafe81b218b4580/lemur.jpg',_binary '\0','36.8414197','Almería','-2.4628135',1,'11710',199.99,'Malaga',139,'ALQUILADA','Title 8',202),(408,0,'Address 9','description 9','olivar','https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/article/5e4a4d405cafe8b615ea4554/presa-gigante1.jpg,https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/gallery/5e4aa5135bafe890e3ddb12d/google-earth.jpg',_binary '','37.1830237','Punta umbría','-6.9662203',9,'36730',499.99,'Madrid',240,'NO ALQUILADA','Title 9',204),(409,0,'Address 10','description 10','hortalizas','https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/article/5e46996c5bafe8fb241ac4dd/medusa-picarte.jpg,https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/gallery/5d9208eb5cafe81a0f3c986a/delfin0.jpg',_binary '\0','43.4620412','Santander','-3.8099719',5,'44330',100,'Santander',150,'DISPUTA','Title 10',204);
/*!40000 ALTER TABLE `smallholding` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `smallholding_photos`
--

LOCK TABLES `smallholding_photos` WRITE;
/*!40000 ALTER TABLE `smallholding_photos` DISABLE KEYS */;
/*!40000 ALTER TABLE `smallholding_photos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_account`
--

LOCK TABLES `user_account` WRITE;
/*!40000 ALTER TABLE `user_account` DISABLE KEYS */;
INSERT INTO `user_account` VALUES (32,0,_binary '\0','$2a$10$q5OZwNtohHEkrT20sGZzf.gyBAsk4FNMQqG7l/kU9izH7YpTuhboC','admin1'),(33,0,_binary '\0','$2a$10$7lwwAC0BX/37MuBccgJsPuxwO9GXp21xyNoec10rxGBOyMktTBjn6','admin2'),(34,0,_binary '\0','$2a$10$Ez2hYkhlLajYnR0DB59W/OZkj3vrmzURdO9LTEtYXP.WRgO9igETG','admin3'),(35,0,_binary '\0','$2a$10$hbraWUSu7ZVlctEawPJbnucb0MuDe5aRmjJZTvGrHPMj0EjyPs./i','renter1'),(36,0,_binary '\0','$2a$10$TX0QNOt7NJOdFSmtFgjWu..VyINldEaHQ2gbyJTJw5GM0FI9gvs2S','renter2'),(37,0,_binary '\0','$2a$10$Vilv0pho9OqrH5erMZtxeepPBKJvYACp6h23MVRlQRj.mIvcAxVXC','renter3'),(38,0,_binary '\0','$2a$10$9lewxta1slxeW.PCeUqbYuFataOGEFlxgitU8E.pxTJ5sz66c9CSm','renter4'),(39,0,_binary '\0','$2a$10$7a3HSXBaFkDvQVDTb9JyNe8RYoKtqtEhsOOFecjHOwSFu.Mvv63Ki','renter5'),(40,0,_binary '\0','$2a$10$FHMZSvdagObRsfwtUQyHbO5R27g7M3b3xO75kASGwCAwbmBxwcmZS','renter6'),(41,0,_binary '\0','$2a$10$EtIpYXqkcUU3vbgY2VdpQuf.ffJF.2IgQCTFlirhCurET/9T83zA.','owner1'),(42,0,_binary '\0','$2a$10$hsJvO30uUedIy1KxqwNrrOU3XD3pzAYaFe6phpLjxZ6pKUYLBRAPO','owner2'),(43,0,_binary '\0','$2a$10$iUzP9.MZCyzqd5j7IV3i0O399ZcMKHSwjV6horDbJQCYfslxL5d7C','owner3'),(44,0,_binary '\0','$2a$10$iPJ8YXVqGaSv3Nh3LKh4refLCGaM9ZtCbXwo.5OW8WUkDBuctihH.','owner4'),(45,0,_binary '\0','$2a$10$iOdvn6s3xA0zDo1ddQKRPuiGEPkPhtz3FuJoJZS88XovqyR75sNJy','owner5'),(46,0,_binary '','$2a$10$mxQumlyoRxDeY2reiJyqUeIigdnl1YSehqa3mL6QatxKe/Go/fVti','owner6');
/*!40000 ALTER TABLE `user_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_account_authorities`
--

LOCK TABLES `user_account_authorities` WRITE;
/*!40000 ALTER TABLE `user_account_authorities` DISABLE KEYS */;
INSERT INTO `user_account_authorities` VALUES (32,'ADMIN'),(33,'ADMIN'),(34,'ADMIN'),(35,'RENTER'),(36,'RENTER'),(37,'RENTER'),(38,'RENTER'),(39,'RENTER'),(40,'RENTER'),(41,'OWNER'),(42,'OWNER'),(43,'OWNER'),(44,'OWNER'),(45,'OWNER'),(46,'OWNER');
/*!40000 ALTER TABLE `user_account_authorities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `valuation`
--

LOCK TABLES `valuation` WRITE;
/*!40000 ALTER TABLE `valuation` DISABLE KEYS */;
INSERT INTO `valuation` VALUES (700,0,3,'2019-08-09 13:13:00.000000'),(701,0,5,'2020-01-21 18:20:00.000000'),(702,0,4,'2018-07-15 17:13:00.000000'),(703,0,5,'2019-05-20 22:20:00.000000');
/*!40000 ALTER TABLE `valuation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-15 16:18:06
