-- MySQL dump 10.13  Distrib 5.1.53, for suse-linux-gnu (x86_64)
--
-- Host: localhost    Database: websiteschema
-- ------------------------------------------------------
-- Server version	5.1.53-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Wrapper`
--

DROP TABLE IF EXISTS `Wrapper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Wrapper` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application` text COLLATE utf8_unicode_ci,
  `name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `wrapperType` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `visualConfig` text COLLATE utf8_unicode_ci,
  `checksum` char(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `createUser` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `lastUpdateUser` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`name`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Wrapper`
--

LOCK TABLES `Wrapper` WRITE;
/*!40000 ALTER TABLE `Wrapper` DISABLE KEYS */;
INSERT INTO `Wrapper` VALUES (1,'StartFB=Start\nInitEvent=COLD\n\n[Bean]\nSpringBeans=spring.xml\n\n[Start]\nFBType=websiteschema.fb.E_RESTART\nEO.COLD={\"Timer\":\"INIT\"}\n\n[Timer]\nFBType=websiteschema.fb.Timer\nEO.EO={\"Console\":\"PRINT\",\"Threshold\":\"COMP\"}\nDO.TIMES={\"Threshold\":\"L\"}\nDI.INTERVAL=1000\n\n[Console]\nFBType=websiteschema.fb.STDOUT\nDI.STR=${PRINT}\n\n[Threshold]\nFBType=websiteschema.fb.Compare\nDI.R=1\nEO.EQ={\"Start\":\"STOP\"}','Hello world!','FB','','d6fade0f381c8a7bf07bd66f47bbe565','2012-01-22 23:31:50','yingrui','2012-01-22 23:59:51','yingrui'),(3,'StartFB=Start\nInitEvent=COLD\n\n[Bean]\nSpringBeans=spring-beans.xml\n\n[Start]\nFBType=websiteschema.fb.E_RESTART\nEO.COLD={\"WebsiteschemaFactory\":\"EI\"}\n\n[WebsiteschemaFactory]\nFBType=websiteschema.crawler.fb.FBWebsiteschema\n#启动抓取\nEO.EO={\"CRAWLER\":\"FETCH\"}\nEO.FAIL={\"EXIT\":\"EI1\"}\n#将Websiteschema传到Crawler\nDO.OUT={\"CRAWLER\":\"SCHEMA\"}\nDI.SITE=${SITEID}\n\n[CRAWLER]\nFBType=websiteschema.crawler.fb.FBWebCrawler\nEO.SUC={\"EXTRACTOR\":\"EI\"}\nEO.FAL={\"EXIT\":\"EI2\"}\nDO.DOC={\"EXTRACTOR\":\"IN\"}\nDO.URL={\"EXTRACTOR\":\"URL\"}\nDI.CRAWLER=websiteschema.crawler.SimpleHttpCrawler\nDI.URL=${URL}\n\n[EXTRACTOR]\nFBType=websiteschema.crawler.fb.FBLinksExtractor\nEO.EO={\"SAVE_LINKS\":\"ADD\"}\nEO.EMPTY={\"EXIT\":\"EI3\"}\nDI.XPATH = ${XPATH}\nDO.OUT={\"SAVE_LINKS\":\"LINKS\"}\n\n[SAVE_LINKS]\nFBType=websiteschema.crawler.fb.FBURLStorage\nEO.ADD={\"ADD_NEW_TASK\":\"ADD\"}\nDI.JOBNAME=${JOBNAME}\nDI.PARENT=${URL}\nDI.DEPTH=1\nDO.ADDED={\"Console\":\"STR\",\"ADD_NEW_TASK\":\"LINKS\"}\n\n[ADD_NEW_TASK]\nFBType=websiteschema.crawler.fb.FBURLQueue\nEO.EO={\"Console\":\"PRINT\"}\nDI.HOST=localhost\nDI.PORT=5672\nDI.QUEUE=url_queue\nDI.SITEID=${SITEID}\nDI.JOBNAME=${JOBNAME}\nDI.SID=${STARTURLID}\nDI.WID=2\nDI.JID=${JOBID}\nDI.CFG=CLS=${CLS}\nDI.DEPTH=1\n\n[EXIT]\nFBType=websiteschema.fb.common.merge.QuadMerge\nEO.EO={\"Start\":\"STOP\"}\n\n[Console]\nFBType=websiteschema.fb.STDOUT\nEO.EO={\"Start\":\"STOP\"}\nDI.STR=hello world\n\n[EXIT]\nFBType=websiteschema.fb.common.merge.QuadMerge\nEO.EO={\"Start\":\"STOP\"}','简单链接采集','FB','','6f67af9ea7df154c80cab546aa83e317','2012-01-22 23:37:51','yingrui','2012-01-22 23:39:02','yingrui'),(2,'StartFB=Start\nInitEvent=COLD\n\n[Bean]\nSpringBeans=spring-beans.xml\n\n[Start]\nFBType=websiteschema.fb.E_RESTART\nEO.COLD={\"WebsiteschemaFactory\":\"EI\"}\n\n[WebsiteschemaFactory]\nFBType=websiteschema.crawler.fb.FBWebsiteschema\nEO.EO={\"CRAWLER\":\"FETCH\"}\nEO.FAIL={\"EXIT\":\"EI1\"}\nDO.OUT={\"CRAWLER\":\"SCHEMA\",\"EXTRACTOR\":\"SCHEMA\"}\nDI.SITE=${SITEID}\n\n[CRAWLER]\nFBType=websiteschema.crawler.fb.FBWebCrawler\nEO.SUC={\"EXTRACTOR\":\"EI\"}\nEO.FAL={\"EXIT\":\"EI2\"}\nDO.DOC={\"EXTRACTOR\":\"IN\"}\nDO.URL={\"EXTRACTOR\":\"URL\"}\nDI.CRAWLER=websiteschema.crawler.SimpleHttpCrawler\nDI.URL=${URL}\n\n[EXTRACTOR]\nFBType=websiteschema.crawler.fb.FBDOMExtractor\nEO.EO={\"SAVE_CONTENT\":\"SAVE\"}\nDI.CLS = ${CLS}\nDO.OUT={\"SAVE_CONTENT\":\"DOC\"}\n\n[SAVE_CONTENT]\nFBType=websiteschema.crawler.fb.FBURLStorage\nEO.SAVE={\"Convertor\":\"TRAN\"}\nDI.URL=${URL}\nDO.DOC={\"Convertor\":\"DOC\"}\n\n[Convertor]\nFBType=websiteschema.crawler.fb.FBXMLToString\nEO.EO={\"Console\":\"PRINT\",\"Start\":\"STOP\"}\nDO.OUT={\"Console\":\"STR\"}\n\n[Console]\nFBType=websiteschema.fb.STDOUT\nDI.STR=hello world','简单内容采集','FB','','62ff11e2523ba4661ff357253460c3b8','2012-01-22 23:36:40','yingrui','2012-01-25 19:40:59','yingrui');
/*!40000 ALTER TABLE `Wrapper` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-01-25 19:51:21
