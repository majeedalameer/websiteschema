namespace=fb.crawler

StartFB=启动
InitEvent=COLD

[Bean]
SpringBeans=spring-beans.xml

[启动]
FBType=websiteschema.fb.E_RESTART
EO.COLD={"Websiteschema工厂类":"EI"}

[Websiteschema工厂类]
FBType=websiteschema.crawler.fb.FBWebsiteschema
EO.EO={"采集器":"FETCH"}
EO.FAIL={"退出":"EI1"}
DO.OUT={"采集器":"SCHEMA","抽取器":"SCHEMA"}
DI.SITE=${SITEID}

[采集器]
FBType=websiteschema.crawler.fb.FBWebCrawler
EO.SUC={"抽取器":"EI"}
EO.FAL={"退出":"EI2"}
DO.DOC={"抽取器":"IN"}
DO.URL={"抽取器":"URL"}
DI.CRAWLER=${CRAWLER}
DI.URL=${URL}

[抽取器]
FBType=websiteschema.crawler.fb.FBDOMExtractor
EO.EO={"保存结果":"SAVE"}
DI.CLS = ${CLS}
DO.OUT={"保存结果":"DOC"}

[保存结果]
FBType=websiteschema.crawler.fb.FBURLStorage
EO.SAVE={"转换成Str":"TRAN"}
DI.URL=${URL}
DO.DOC={"转换成Str":"DOC"}

[退出]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"启动":"STOP"}

[转换成Str]
FBType=websiteschema.crawler.fb.FBXMLToString
EO.EO={"打印":"PRINT","启动":"STOP"}
DO.OUT={"打印":"STR"}

[打印]
FBType=websiteschema.fb.STDOUT
DI.STR=hello world