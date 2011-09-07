#!/bin/bash

# mysqladmin -u root password "root"

mysql -uroot -proot<< EOF
drop database if exists websiteschema;
create database websiteschema;
GRANT ALL ON websiteschema.* TO 'websiteschema'@'%' identified by 'websiteschema';
GRANT ALL ON websiteschema.* TO 'websiteschema'@'localhost' identified by 'websiteschema';
EOF
