#!/bin/sh
## go to /webapps
cd /webapps
##

APP=/webapps/app.jar
app=/usr/local/bin/app.jar
if [ ! -f $APP ];then
   cp $app $APP
fi

echo deploy-lib.sh ...
deploy-lib.sh

if [ ! -z $(ls lib/*.jar 2>/dev/null) ];then
  echo fail to deploy lib: > /dev/stderr
  for it in $(ls lib);do
     echo $it > /dev/stderr
  done
fi

echo deploy.sh ...
deploy.sh

### config
CONFIG=/webapps/config
config=/var/local
if [ ! -d $CONFIG ];then 
   mkdir $CONFIG
fi

## aplication-dev.yml
if [ ! -f $CONFIG/application-dev.yml ];then
   if [ -f /webapps/config/application.yml ];then
      mv /webapps/config/application.yml $target
   else
      echo cp $config/application.yml $CONFIG/application-dev.yml
      cp $config/application.yml $CONFIG/application-dev.yml
   fi
fi

## logback-spring.xml
if [ ! -f $CONFIG/logback-spring.xml ];then
   echo cp $config/logback-spring.xml $CONFIG/logback-spring.xml
   cp $config/logback-spring.xml $CONFIG/logback-spring.xml
fi

echo fix_url.sh ...
fix_url.sh

echo java $JAVA_OPTS -jar *.jar --sprint.profiles.active=dev --server.port=8080
java $JAVA_OPTS -jar *.jar --sprint.profiles.active=dev --server.port=8080
