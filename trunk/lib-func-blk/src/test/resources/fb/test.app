namespace=fb

StartFB=Start
InitEvent=COLD

[Bean]
SpringBeans=spring.xml

[Start]
FBType=websiteschema.fb.E_RESTART
EO.COLD={"Timer":"INIT"}

[Timer]
FBType=websiteschema.fb.Timer
EO.EO={"Console":"PRINT","Threshold":"COMP"}
DO.TIMES={"Console":"STR","Threshold":"L"}
DI.INTERVAL=1000

[Console]
FBType=websiteschema.fb.STDOUT
DI.STR=hello world

[Cease]
FBType=websiteschema.fb.CEASE

[Threshold]
FBType=websiteschema.fb.IF_GREAT
DI.R=10
EO.GT={"Cease":"STOP"}