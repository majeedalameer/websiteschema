/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2011-9-5 23:34:31                            */
/*==============================================================*/


drop table if exists User;

/*==============================================================*/
/* Table: User                                                  */
/*==============================================================*/
create table User
(
   USER_ID              bigint not null auto_increment,
   NAME                 varchar(30),
   PASSWD               char(30),
   EMAIL                varchar(30),
   primary key (USER_ID)
);

