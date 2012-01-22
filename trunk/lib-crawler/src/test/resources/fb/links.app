namespace=fb.crawler

StartFB=Start
InitEvent=COLD

[Bean]
SpringBeans=spring-beans.xml

[Start]
FBType=websiteschema.fb.E_RESTART
EO.COLD={"WebsiteschemaFactory":"EI"}

[WebsiteschemaFactory]
FBType=websiteschema.crawler.fb.FBWebsiteschema
#启动抓取
EO.EO={"CRAWLER":"FETCH"}
EO.FAIL={"EXIT":"EI1"}
#将Websiteschema传到Crawler
DO.OUT={"CRAWLER":"SCHEMA"}
DI.SITE=www_163_com_1

[CRAWLER]
FBType=websiteschema.crawler.fb.FBWebCrawler
EO.SUC={"EXTRACTOR":"EI"}
EO.FAL={"EXIT":"EI2"}
DO.DOC={"EXTRACTOR":"IN"}
DO.URL={"EXTRACTOR":"URL"}
DI.CRAWLER=websiteschema.crawler.SimpleHttpCrawler
DI.URL=http://money.163.com/special/00252G50/macroNew.html

[EXTRACTOR]
FBType=websiteschema.crawler.fb.FBLinksExtractor
EO.EO={"SAVE_LINKS":"ADD"}
EO.EMPTY={"EXIT":"EI3"}
DI.XPATH = html/body/div[@class='area clearfix']/div[@class='colLM']/ul[@class='newsList dotted']/li/span[@class='article']/a
DO.OUT={"SAVE_LINKS":"LINKS"}

[SAVE_LINKS]
FBType=websiteschema.crawler.fb.FBURLStorage
EO.ADD={"ADD_NEW_TASK":"ADD"}
DI.JOBNAME=money_163_com_1
DI.PARENT=http://money.163.com/special/00252G50/macroNew.html
DI.DEPTH=1
DO.ADDED={"Console":"STR","ADD_NEW_TASK":"LINKS"}

[ADD_NEW_TASK]
FBType=websiteschema.crawler.fb.FBURLQueue
EO.EO={"Console":"PRINT"}
DI.HOST=localhost
DI.PORT=5672
DI.QUEUE=url_queue
DI.SITEID=www_163_com_1
DI.JOBNAME=money_163_com_1
DI.SID=1
DI.WID=7
DI.JID=5
DI.CFG=CLS=0
DI.DEPTH=1

[EXIT]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"Start":"STOP"}


[Console]
FBType=websiteschema.fb.STDOUT
EO.EO={"Start":"STOP"}
DI.STR=hello world
