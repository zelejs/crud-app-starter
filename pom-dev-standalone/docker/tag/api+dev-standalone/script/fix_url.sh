#!/usr/bin/env bash
target=/webapps/config/application-dev.yml

if [ ! ${URL_SHORT} ];then
   exit
fi

if [ ! -f $target ];then
   if [ -f /webapps/config/application.yml ];then
      mv /webapps/config/application.yml $target
   fi
fi

if [ ! -f $target ];then
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
sed -i "s/url: jdbc:mysql:\/\/[0-9\.a-z:]*\/[a-z\_]*/url: jdbc:mysql:\/\/$mysqlurl/" $target
## 
sed -i "s/username:.*/username: $username/" $target
sed -i "s/password:.*/password: $password/" $targets

if [ $GREENFIELD ];then 
   sed -i "s/initialize:.*/initialize: true/" $target
else
   sed -i "s/initialize:.*/initialize: false/" $target
fi
