#!/bin/sh

## keep workding_dir from docker-compose.yml

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



### start dummy:api
dummy=/usr/local/dummy
if [ ! -d $dummy ];then 
   mkdir -p $dummy
fi

cd $dummy
echo ${pwd}

APP=app.jar
app=/usr/local/bin/app.jar
if [ ! -f $APP ];then
   cp $app $APP
fi


CONFIG=config
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
