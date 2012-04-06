--有关日期的例子
update ScheduleTask set createTime =  DATE_SUB('2006-05-01',INTERVAL 1 DAY) where id = 22;

