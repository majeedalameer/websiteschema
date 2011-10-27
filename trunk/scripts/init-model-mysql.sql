/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2011-10-25 10:41:45                          */
/*==============================================================*/


drop table if exists ConcernedWeibo;

drop table if exists Follow;

drop table if exists Job;

drop table if exists Keyword;

drop table if exists Scheduler;

drop table if exists Site;

drop table if exists StartURL;

drop table if exists User;

drop table if exists Weibo;

drop table if exists Wrapper;

/*==============================================================*/
/* Table: ConcernedWeibo                                        */
/*==============================================================*/
create table ConcernedWeibo
(
   id                   bigint not null auto_increment,
   name                 varchar(300),
   objectType           int,
   title                varchar(300),
   siteId               varchar(100),
   weiboURL             varchar(1000),
   org                  varchar(300),
   fans                 int,
   follow               int,
   weibo                int,
   notes                varchar(1000),
   certification        varchar(4000),
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id)
);

insert into ConcernedWeibo(name,objectType,title,siteId,weiboURL,org,fans,follow,weibo,notes,certification,createTime,createUser,updateTime,lastUpdateUser) 
values ('yingrui',0,'','www_weibo_com_7','http://weibo.com/yingruif','nyapc','0','0','0','','',now(),'system',now(),'system');

/*==============================================================*/
/* Table: Follow                                                */
/*==============================================================*/
create table Follow
(
   id                   bigint not null auto_increment,
   wid                  bigint,
   cwid                 bigint,
   status               int,
   createTime           datetime,
   primary key (id)
);

insert into Follow(wid,cwid,status,createTime) 
values (1,1,0,now());

/*==============================================================*/
/* Table: Job                                                   */
/*==============================================================*/
create table Job
(
   id                   bigint not null auto_increment,
   jobType              varchar(30),
   configure            varchar(4000),
   wrapperId            bigint,
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id)
);

/*==============================================================*/
/* Table: Keyword                                               */
/*==============================================================*/
create table Keyword
(
   id                   bigint not null auto_increment,
   keywords             varchar(300),
   referrer             varchar(300),
   status               int,
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id)
);

/*==============================================================*/
/* Table: Scheduler                                             */
/*==============================================================*/
create table Scheduler
(
   id                   bigint not null auto_increment,
   startURLId           bigint,
   jobId                bigint,
   schedule             varchar(1000),
   scheduleType         int,
   createTime           datetime,
   primary key (id)
);

/*==============================================================*/
/* Table: Site                                                  */
/*==============================================================*/
create table Site
(
   id                   bigint not null auto_increment,
   siteId               varchar(100),
   siteDomain           varchar(100),
   siteName             varchar(100),
   siteType             varchar(30),
   parentId             bigint,
   url                  varchar(120),
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id)
);

insert into Site(siteId,siteDomain,siteName,siteType,parentId,url,createTime,createUser,updateTime,lastUpdateUser) 
values ('www_163_com_1','www.163.com','netease','portal','0','http://www.163.com/',now(),'system',now(),'system');

insert into Site(siteId,siteDomain,siteName,siteType,parentId,url,createTime,createUser,updateTime,lastUpdateUser) 
values ('localhost:8080_2','localhost','tomcat','document','0','http://localhost:8080/',now(),'system',now(),'system');

insert into Site(siteId,siteDomain,siteName,siteType,parentId,url,createTime,createUser,updateTime,lastUpdateUser) 
values ('www_baidu_com_3','www.baidu.com','baidu','search engine','0','http://www.baidu.com/',now(),'system',now(),'system');

insert into Site(siteId,siteDomain,siteName,siteType,parentId,url,createTime,createUser,updateTime,lastUpdateUser) 
values ('baike_baidu_com_4','baike.baidu.com','baidu baike','wiki','3','http://baike.baidu.com/',now(),'system',now(),'system');

insert into Site(siteId,siteDomain,siteName,siteType,parentId,url,createTime,createUser,updateTime,lastUpdateUser) 
values ('www_sina_com_cn_5','www.sina.com.cn','sina','portal','0','http://www.sina.com.cn/',now(),'system',now(),'system');

insert into Site(siteId,siteDomain,siteName,siteType,parentId,url,createTime,createUser,updateTime,lastUpdateUser) 
values ('www_sohu_com_6','www.sohu.com','sohu','portal','0','http://www.sohu.com/',now(),'system',now(),'system');

insert into Site(siteId,siteDomain,siteName,siteType,parentId,url,createTime,createUser,updateTime,lastUpdateUser) 
values ('www_weibo_com_7','www.weibo.com','sina weibo','weibo','5','http://www.weibo.com/',now(),'system',now(),'system');

/*==============================================================*/
/* Table: StartURL                                              */
/*==============================================================*/
create table StartURL
(
   id                   bigint not null auto_increment,
   siteId               varchar(100),
   startURL             varchar(8192),
   jobname              varchar(100) not null,
   status               int,
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id, jobname)
);

/*==============================================================*/
/* Table: User                                                  */
/*==============================================================*/
create table User
(
   ID                   bigint not null auto_increment,
   USER_ID              varchar(30),
   NAME                 varchar(30),
   PASSWD               char(32),
   EMAIL                varchar(30),
   ROLE                 varchar(100),
   primary key (ID)
);


insert into User(user_id,name,passwd,email,role) values ('admin','admin','21232f297a57a5a743894a0e4a801fc3','yingrui.f@gmail.com','ROLE_ADMIN, ROLE_USER, ROLE_CRAWLER');
insert into User(user_id,name,passwd,email,role) values ('system','system','21232f297a57a5a743894a0e4a801fc3','yingrui.f@gmail.com','ROLE_ADMIN, ROLE_USER, ROLE_CRAWLER');
insert into User(user_id,name,passwd,email,role) values ('yingrui','yingrui','21232f297a57a5a743894a0e4a801fc3','yingrui.f@gmail.com','ROLE_ADMIN, ROLE_USER, ROLE_CRAWLER');

/*==============================================================*/
/* Table: Weibo                                                 */
/*==============================================================*/
create table Weibo
(
   id                   bigint not null auto_increment,
   userId               varchar(30),
   siteId               varchar(100),
   passwd               varchar(30),
   status               int,
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id)
);

insert into Weibo(userId,siteId,passwd,status,createTime,createUser,updateTime,lastUpdateUser) 
values ('websiteschema@gmail.com','www_weibo_com_7','websiteschema','0',now(),'system',now(),'system');

/*==============================================================*/
/* Table: Wrapper                                               */
/*==============================================================*/
create table Wrapper
(
   id                   bigint not null auto_increment,
   application          text,
   name                 varchar(100) not null,
   wrapperType          varchar(30),
   visualConfig         text,
   createTime           datetime,
   createUser           varchar(30),
   updateTime           datetime,
   lastUpdateUser       varchar(30),
   primary key (id, name)
);

