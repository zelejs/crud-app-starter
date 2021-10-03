#!/usr/bin/env bash
target='/webapps/config/application-dev.yml'

## TEST
ii='-i'
if [ ${TEST} ];then
   unset ii
   target=${TEST}
fi
## end TEST


## main

if [ ! ${URL_SHORT} ];then
   exit
fi

if [ ! -f $target ];then
   if [ -f /webapps/config/application.yml ];then
      mv /webapps/config/application.yml $target
   fi
fi

if [ ! -f $target ];then
   echo $target not exists!
   exit
fi

#export URL_SHORT='mysqlserver:3307\/db_name'
#export USERNAME=root
#export PASSWORD=root

username=${USERNAME}
password=${PASSWORD}
mysqlurl=${URL_SHORT/\//\\\/}
if [ ! $username ];then
   username=root 
fi
if [ ! $password ];then
   password=root  
fi


#jdbc:mysql://127.0.0.1:3306/test?
sed $ii "s/url: jdbc:mysql:\/\/[0-9\.a-z:]*\/[a-z\_]*/url: jdbc:mysql:\/\/$mysqlurl/" $target

## 
sed $ii "s/username:.*/username: $username/" $target
sed $ii "s/password:.*/password: $password/" $target

if [ ${GREENFIELD} ];then 
   sed $ii "s/initialize:.*/initialize: true/" $target
else
   sed $ii "s/initialize:.*/initialize: false/" $target
fi
