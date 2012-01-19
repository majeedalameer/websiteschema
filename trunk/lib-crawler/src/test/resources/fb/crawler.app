namespace=fb.crawler

StartFB=Start
InitEvent=COLD

[Bean]
SpringBeans=spring-beans.xml

[Start]
FBType=websiteschema.fb.E_RESTART
EO.COLD={"WebsiteschemaFactory":"INIT"}

[WebsiteschemaFactory]
FBType=websiteschema.crawler.fb.FBWebsiteschema
EO.SUC={"CRAWLER":"FETCH"}
DO.OUT={"CRAWLER":"SCHEMA","EXTRACTOR":"SCHEMA"}
DI.SITE=www_163_com_1

[CRAWLER]
FBType=websiteschema.crawler.fb.FBWebCrawler
EO.SUC={"EXTRACTOR":"EXTRACT"}
EO.FAL={"Start":"STOP"}
DO.DOC={"EXTRACTOR":"IN"}
#DI.CRAWLER=websiteschema.crawler.SimpleHttpCrawler
DI.URL=http://news.163.com/12/0117/11/7NVE580G00014JB5.html?from=index

[EXTRACTOR]
FBType=websiteschema.crawler.fb.FBDOMExtractor
EO.EO={"Convertor":"TRAN"}
DI.CLS = 1
DO.OUT={"Convertor":"DOC"}

[Convertor]
FBType=websiteschema.crawler.fb.FBXMLToString
EO.EO={"Console":"PRINT","Start":"STOP"}
DO.OUT={"Console":"STR"}

[Console]
FBType=websiteschema.fb.STDOUT
DI.STR=hello world