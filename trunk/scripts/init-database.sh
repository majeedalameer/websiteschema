#!/bin/bash

mysqladmin -u root password "root"

mysql << EOF
drop database if exists websiteschema;
create database websiteschema  character set utf8mb4;
GRANT ALL ON websiteschema.* TO 'websiteschema'@'%' identified by 'websiteschema';
GRANT ALL ON websiteschema.* TO 'websiteschema'@'localhost' identified by 'websiteschema';
EOF

SQL_FILE="init-model-mysql.sql"

TMP_FILE=`mktemp`
echo "mysql -uwebsiteschema -pwebsiteschema -Dwebsiteschema<< EOF" > $TMP_FILE
cat $SQL_FILE >> $TMP_FILE
echo "EOF" >> $TMP_FILE
sh $TMP_FILE
rm $TMP_FILE
