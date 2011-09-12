/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2011-9-12 17:54:30                           */
/*==============================================================*/


drop table if exists User;

/*==============================================================*/
/* Table: User                                                  */
/*==============================================================*/
create table User
(
   USER_ID              bigint not null auto_increment,
   ID                   varchar(30),
   NAME                 varchar(30),
   PASSWD               char(32),
   EMAIL                varchar(30),
   ROLE                 varchar(100),
   primary key (USER_ID)
);

insert into User(id,name,passwd,email,role) values ('rod','rod','a564de63c2d0da68cf47586ee05984d7','rod@test.com','ROLE_ADMIN, ROLE_USER, ROLE_CRAWLER');
insert into User(id,name,passwd,email,role) values ('admin','yingrui','21232f297a57a5a743894a0e4a801fc3','yingrui.f@gmail.com','ROLE_ADMIN, ROLE_USER, ROLE_CRAWLER');

