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
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Wrapper`
--

LOCK TABLES `Wrapper` WRITE;
/*!40000 ALTER TABLE `Wrapper` DISABLE KEYS */;
INSERT INTO `Wrapper` VALUES (1,'StartFB=Start\nInitEvent=COLD\n\n[Bean]\nSpringBeans=spring.xml\n\n[Start]\nFBType=websiteschema.fb.E_RESTART\nEO.COLD={\"Timer\":\"INIT\"}\n\n[Timer]\nFBType=websiteschema.fb.Timer\nEO.EO={\"Console\":\"PRINT\",\"Threshold\":\"COMP\"}\nDO.TIMES={\"Threshold\":\"L\"}\nDI.INTERVAL=1000\n\n[Console]\nFBType=websiteschema.fb.STDOUT\nDI.STR=${PRINT}\n\n[Threshold]\nFBType=websiteschema.fb.Compare\nDI.R=1\nEO.EQ={\"Start\":\"STOP\"}','Hello world!','FB','','d6fade0f381c8a7bf07bd66f47bbe565','2012-01-22 23:31:50','yingrui','2012-01-22 23:59:51','yingrui'),(3,'StartFB=Start\nInitEvent=COLD\n\n[Bean]\nSpringBeans=spring-beans.xml\n\n[Start]\nFBType=websiteschema.fb.E_RESTART\nEO.COLD={\"WebsiteschemaFactory\":\"EI\"}\n\n[WebsiteschemaFactory]\nFBType=websiteschema.crawler.fb.FBWebsiteschema\n#启动抓取\nEO.EO={\"CRAWLER\":\"FETCH\"}\nEO.FAIL={\"EXIT\":\"EI1\"}\n#将Websiteschema传到Crawler\nDO.OUT={\"CRAWLER\":\"SCHEMA\"}\nDI.SITE=${SITEID}\n\n[CRAWLER]\nFBType=websiteschema.crawler.fb.FBWebCrawler\nEO.SUC={\"EXTRACTOR\":\"EI\"}\nEO.FAL={\"EXIT\":\"EI2\"}\nDO.DOC={\"EXTRACTOR\":\"IN\"}\nDO.URL={\"EXTRACTOR\":\"URL\"}\nDI.CRAWLER=websiteschema.crawler.SimpleHttpCrawler\nDI.URL=${URL}\n\n[EXTRACTOR]\nFBType=websiteschema.crawler.fb.FBLinksExtractor\nEO.EO={\"SAVE_LINKS\":\"ADD\"}\nEO.FATAL={\"EXIT\":\"EI4\"}\nEO.EMPTY={\"EXIT\":\"EI3\"}\nDI.XPATH = ${XPATH}\nDO.OUT={\"SAVE_LINKS\":\"LINKS\"}\n\n[SAVE_LINKS]\nFBType=websiteschema.crawler.fb.FBURLStorage\nEO.ADD={\"ADD_NEW_TASK\":\"ADD\"}\nDI.JOBNAME=${JOBNAME}\nDI.PARENT=${URL}\nDI.DEPTH=1\nDO.ADDED={\"Console\":\"STR\",\"ADD_NEW_TASK\":\"LINKS\"}\n\n[ADD_NEW_TASK]\nFBType=websiteschema.crawler.fb.FBURLQueue\nEO.EO={\"Console\":\"PRINT\"}\nDI.HOST=localhost\nDI.PORT=5672\nDI.QUEUE=url_queue\nDI.SITEID=${SITEID}\nDI.JOBNAME=${JOBNAME}\nDI.SID=${STARTURLID}\nDI.WID=2\nDI.JID=${JOBID}\nDI.CFG=CLS=${CLS}\nDI.DEPTH=1\n\n[EXIT]\nFBType=websiteschema.fb.common.merge.QuadMerge\nEO.EO={\"Start\":\"STOP\"}\n\n[Console]\nFBType=websiteschema.fb.STDOUT\nEO.EO={\"Start\":\"STOP\"}\nDI.STR=hello world\n\n[EXIT]\nFBType=websiteschema.fb.common.merge.QuadMerge\nEO.EO={\"Start\":\"STOP\"}','简单链接采集','FB','','fe8177004d558394de9f797334a9cf57','2012-01-22 23:37:51','yingrui','2012-02-16 17:51:11','st'),(2,'StartFB=Start\nInitEvent=COLD\n\n[Bean]\nSpringBeans=spring-beans.xml\n\n[Start]\nFBType=websiteschema.fb.E_RESTART\nEO.COLD={\"WebsiteschemaFactory\":\"EI\"}\n\n[WebsiteschemaFactory]\nFBType=websiteschema.crawler.fb.FBWebsiteschema\nEO.EO={\"CRAWLER\":\"FETCH\"}\nEO.FAIL={\"EXIT\":\"EI1\"}\nDO.OUT={\"CRAWLER\":\"SCHEMA\",\"EXTRACTOR\":\"SCHEMA\"}\nDI.SITE=${SITEID}\n\n[CRAWLER]\nFBType=websiteschema.crawler.fb.FBWebCrawler\nEO.SUC={\"EXTRACTOR\":\"EI\"}\nEO.FAL={\"EXIT\":\"EI2\"}\nDO.DOC={\"EXTRACTOR\":\"IN\"}\nDO.URL={\"EXTRACTOR\":\"URL\"}\nDI.CRAWLER=websiteschema.crawler.SimpleHttpCrawler\nDI.URL=${URL}\n\n[EXTRACTOR]\nFBType=websiteschema.crawler.fb.FBDOMExtractor\nEO.EO={\"SAVE_CONTENT\":\"SAVE\"}\nDI.CLS = ${CLS}\nDO.OUT={\"SAVE_CONTENT\":\"DOC\"}\n\n[SAVE_CONTENT]\nFBType=websiteschema.crawler.fb.FBURLStorage\nEO.SAVE={\"Convertor\":\"TRAN\"}\nDI.URL=${URL}\nDO.DOC={\"Convertor\":\"DOC\"}\n\n[EXIT]\nFBType=websiteschema.fb.common.merge.QuadMerge\nEO.EO={\"Start\":\"STOP\"}\n\n[Convertor]\nFBType=websiteschema.crawler.fb.FBXMLToString\nEO.EO={\"Console\":\"PRINT\",\"Start\":\"STOP\"}\nDO.OUT={\"Console\":\"STR\"}\n\n[Console]\nFBType=websiteschema.fb.STDOUT\nDI.STR=hello world\n\n[Console]\nFBType=websiteschema.fb.STDOUT\nDI.STR=hello world','简单内容采集','FB','','90dd1bc7bf0932b3b9cfc9ed20d9ebac','2012-01-22 23:36:40','yingrui','2012-02-01 10:51:15','admin'),(4,'StartFB=Start\nInitEvent=COLD\n\n[Bean]\nSpringBeans=spring-beans.xml\n\n\n[Start]\nFBType=websiteschema.fb.E_RESTART\nEO.COLD={\"WebsiteschemaFactory\":\"EI\"}\n\n[WebsiteschemaFactory]\nFBType=websiteschema.crawler.fb.FBWebsiteschema\n#启动抓取\nEO.EO={\"CRAWLER\":\"FETCH\"}\nEO.FAIL={\"EXIT\":\"EI1\"}\n#将Websiteschema传到Crawler\nDO.OUT={\"CRAWLER\":\"SCHEMA\"}\nDI.SITE=${SITEID}\n\n[CRAWLER]\nFBType=websiteschema.crawler.fb.FBWebCrawler\nEO.SUC={\"EXTRACTOR\":\"EI\"}\nEO.FAL={\"EXIT\":\"EI2\"}\nDO.DOCS={\"EXTRACTOR\":\"DOCS\"}\nDO.URL={\"EXTRACTOR\":\"URL\"}\nDI.CRAWLER=websiteschema.crawler.browser.BrowserWebCrawler\nDI.URL=${URL}\n\n[EXTRACTOR]\nFBType=websiteschema.crawler.fb.FBLinksExtractor\nEO.EO={\"SAVE_LINKS\":\"ADD\"}\nEO.FATAL={\"EXIT\":\"EI4\"}\nEO.EMPTY={\"EXIT\":\"EI3\"}\nDI.XPATH = ${XPATH}\nDO.OUT={\"SAVE_LINKS\":\"LINKS\"}\n\n[SAVE_LINKS]\nFBType=websiteschema.crawler.fb.FBURLStorage\nEO.ADD={\"ADD_NEW_TASK\":\"ADD\"}\nDI.JOBNAME=${JOBNAME}\nDI.PARENT=${URL}\nDI.DEPTH=1\nDO.ADDED={\"Console\":\"STR\",\"ADD_NEW_TASK\":\"LINKS\"}\n\n[ADD_NEW_TASK]\nFBType=websiteschema.crawler.fb.FBURLQueue\nEO.EO={\"Console\":\"PRINT\"}\nDI.HOST=localhost\nDI.PORT=5672\nDI.QUEUE=url_queue\nDI.SITEID=${SITEID}\nDI.JOBNAME=${JOBNAME}\nDI.SID=${STARTURLID}\nDI.WID=2\nDI.JID=${JOBID}\nDI.CFG=CLS=${CLS}\nDI.DEPTH=1\n\n[EXIT]\nFBType=websiteschema.fb.common.merge.QuadMerge\nEO.EO={\"Start\":\"STOP\"}\n\n\n[Console]\nFBType=websiteschema.fb.STDOUT\nEO.EO={\"Start\":\"STOP\"}\nDI.STR=hello world','浏览器链接采集','FB','','823f813e26539651383f2b248cb23d3f','2012-02-15 14:44:43','yingrui','2012-02-17 15:50:17','admin'),(5,'namespace=fb.crawler\n\nStartFB=启动\nInitEvent=COLD\n\n[Bean]\nSpringBeans=spring-beans.xml\n\n[启动]\nFBType=websiteschema.fb.E_RESTART\nEO.COLD={\"Websiteschema工厂\":\"EI\"}\n\n[初始化]\nFBType=websiteschema.fb.common.split.DualSplit\nEO.EO1={\"Websiteschema工厂\":\"EI\"}\nEO.EO2={\"ClusterModel工厂\":\"EI\"}\n\n[Websiteschema工厂]\nFBType=websiteschema.crawler.fb.FBWebsiteschema\nEO.EO={\"ClusterModel工厂\":\"EI\"}\nEO.FAIL={\"终止1\":\"EI1\"}\n#将Websiteschema传到Crawler\nDO.OUT={\"采集\":\"SCHEMA\",\"抽取内容\":\"SCHEMA\",\"分类\":\"SCHEMA\"}\nDI.SITE=${SITEID}\n\n[ClusterModel工厂]\nFBType=websiteschema.crawler.fb.FBClusterModel\nEO.EO={\"采集\":\"FETCH\"}\nEO.FAIL={\"终止1\":\"EI2\"}\nDO.CM={\"分类\":\"CM\"}\nDI.SITE=${SITEID}\nDI.CACHE=true\nDI.TIMEOUT=60000\nDI.LOCAL=cache\n\n[采集]\nFBType=websiteschema.crawler.fb.FBWebCrawler\nEO.SUC={\"分类\":\"EI\"}\nEO.FAL={\"终止1\":\"EI3\"}\nDO.DOCS={\"分类\":\"DOCS\",\"抽取链接\":\"DOCS\",\"抽取内容\":\"DOCS\"}\nDO.URL={\"抽取链接\":\"URL\"}\nDI.CRAWLER=${CRAWLER}\nDI.URL=${URL}\n\n[分类]\nFBType=websiteschema.crawler.fb.FBClassifier\nEO.DOC={\"抽取内容\":\"EI\"}\nEO.LINK={\"汇总链接\":\"EI1\"}\nEO.INV={\"汇总链接\":\"EI2\"}\nDO.CLS={\"抽取内容\":\"CLS\"}\n#DI.CM来自:ClusterModel工厂:DO.CM\n\n[汇总链接]\nFBType=websiteschema.fb.common.merge.DualMerge\nEO.EO={\"抽取链接\":\"EI\"}\n\n#--------------------------\n#  抽取链接\n#--------------------------\n\n[抽取链接]\nFBType=websiteschema.crawler.fb.FBLinksExtractor\nEO.EO={\"保存链接\":\"ADD\"}\nEO.EMPTY={\"终止1\":\"EI4\"}\nDI.XPATH = ${XPATH}\nDO.OUT={\"保存链接\":\"LINKS\"}\n\n[保存链接]\nFBType=websiteschema.crawler.fb.FBURLStorage\nEO.ADD={\"添加新任务\":\"ADD\"}\nDI.JOBNAME=money_163_com_1\nDI.PARENT=${URL}\nDI.DEPTH=1\nDO.ADDED={\"打印输出\":\"STR\",\"添加新任务\":\"LINKS\"}\n\n[添加新任务]\nFBType=websiteschema.crawler.fb.FBURLQueue\nEO.EO={\"打印输出\":\"PRINT\"}\nEO.FATAL={\"终止2\":\"EI4\"}\nDI.HOST=localhost\nDI.PORT=5672\nDI.QUEUE=url_queue\nDI.SITEID=${SITEID}\nDI.JOBNAME=${JOBNAME}\nDI.SID=${STARTURLID}\nDI.WID=${WRAPPERID}\nDI.JID=${JOBID}\nDI.CFG=\nDI.DEPTH=1\n\n#--------------------------\n#  抽取内容\n#--------------------------\n\n[抽取内容]\nFBType=websiteschema.crawler.fb.FBDOMExtractor\nEO.EO={\"过滤抽取结果\":\"EI\"}\n#DI.CLS = 30\nDI.URL = ${URL}\nDO.OUT={\"过滤抽取结果\":\"DOC\"}\n\n[过滤抽取结果]\nFBType=websiteschema.crawler.fb.FBFieldFilter\nEO.EO={\"检验抽取结果\":\"EI\"}\nDI.FILTER={\"SOURCENAME\":\"websiteschema.cluster.analyzer.fields.SourceNameFilter\"}\nDO.DOC={\"保存抽取结果\":\"DOC\",\"检验抽取结果\":\"DOC\"}\n\n[检验抽取结果]\nFBType=websiteschema.crawler.fb.FBValidate\nEO.YES={\"保存抽取结果\":\"SAVE\"}\nEO.NO={\"终止2\":\"EI2\"}\nDI.MUSTHAVE=[\"CONTENT\",\"TITLE\",\"DATE\"]\n\n[保存抽取结果]\nFBType=websiteschema.crawler.fb.FBURLStorage\nEO.SAVE={\"XML2Str\":\"TRAN\"}\nDI.URL=${URL}\nDO.DOC={\"XML2Str\":\"DOC\"}\n\n[终止1]\nFBType=websiteschema.fb.common.merge.QuadMerge\nEO.EO={\"终止2\":\"EI1\"}\n\n[终止2]\nFBType=websiteschema.fb.common.merge.QuadMerge\nEO.EO={\"启动\":\"STOP\"}\n\n[XML2Str]\nFBType=websiteschema.crawler.fb.FBXMLToString\nEO.EO={\"打印输出\":\"PRINT\"}\nDO.OUT={\"打印输出\":\"STR\"}\n\n[打印输出]\nFBType=websiteschema.fb.STDOUT\nEO.EO={\"终止2\":\"EI3\"}\nDI.STR=hello world','标准采集程序','FB','','535250a239067b6e7830fee6191d6f49','2012-02-17 16:03:26','admin','2012-03-05 17:21:35','admin'),(6,'','股吧列表采集','FB','','d41d8cd98f00b204e9800998ecf8427e','2012-02-25 23:17:12','yingrui','2012-02-25 23:38:46','yingrui');
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

-- Dump completed on 2012-03-05 19:44:58
