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
EO.EO={"CRAWLER":"FETCH"}
EO.FAIL={"EXIT":"EI1"}
DO.OUT={"CRAWLER":"SCHEMA","EXTRACTOR":"SCHEMA"}
#DI.SITE=www_163_com_1
DI.SITE=www_qq_com_4845

[CRAWLER]
FBType=websiteschema.crawler.fb.FBWebCrawler
EO.SUC={"EXTRACTOR":"EI"}
EO.FAL={"EXIT":"EI2"}
DO.DOC={"EXTRACTOR":"IN"}
DO.URL={"EXTRACTOR":"URL"}
DI.CRAWLER=websiteschema.crawler.SimpleHttpCrawler
#RowKey=http://moc.361.yenom//12/0121/13/7OA1HC8J00253B0H.html
#DI.URL=http://money.163.com/12/0121/13/7OA1HC8J00253B0H.html
DI.URL=http://finance.qq.com/a/20120110/002908.htm

[EXTRACTOR]
FBType=websiteschema.crawler.fb.FBDOMExtractor
EO.EO={"FIELD_FILTER":"EI"}
#DI.CLS = 8
DI.CLS = 30
DO.OUT={"FIELD_FILTER":"DOC"}

[FIELD_FILTER]
FBType=websiteschema.crawler.fb.FBFieldFilter
EO.EO={"VALIDATE":"EI"}
DI.FILTER={"SOURCENAME":"websiteschema.cluster.analyzer.fields.SourceNameFilter"}
DO.DOC={"SAVE_CONTENT":"DOC","VALIDATE":"DOC"}

[VALIDATE]
FBType=websiteschema.crawler.fb.FBValidate
EO.YES={"SAVE_CONTENT":"SAVE"}
EO.NO={"EXIT":"EI3"}
DI.MUSTHAVE=["CONTENT","TITLE","DATE"]

[SAVE_CONTENT]
FBType=websiteschema.crawler.fb.FBURLStorage
EO.SAVE={"Convertor":"TRAN"}
DI.URL=http://money.163.com/12/0121/13/7OA1HC8J00253B0H.html
DO.DOC={"Convertor":"DOC"}

[EXIT]
FBType=websiteschema.fb.common.merge.QuadMerge
EO.EO={"Start":"STOP"}

[Convertor]
FBType=websiteschema.crawler.fb.FBXMLToString
EO.EO={"Console":"PRINT","Start":"STOP"}
DO.OUT={"Console":"STR"}

[Console]
FBType=websiteschema.fb.STDOUT
DI.STR=hello world