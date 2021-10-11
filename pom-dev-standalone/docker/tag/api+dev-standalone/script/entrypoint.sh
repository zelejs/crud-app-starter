#!/usr/bin/env bash
## keep workding_dir from docker-compose.yml
opt=$1

echo deploy-classes.sh ...
bash /usr/local/bin/deploy-classes.sh

if [ ! -z $(ls classes/*.class 2>/dev/null) ];then
  echo fail to deploy classes: > /dev/stderr
  for it in $(ls classes);do
     echo $it > /dev/stderr
  done
fi

echo deploy-lib.sh ...
bash /usr/local/bin/deploy-lib.sh

if [ ! -z $(ls lib/*.jar 2>/dev/null) ];then
  echo fail to deploy lib: > /dev/stderr
  for it in $(ls lib);do
     echo $it > /dev/stderr
  done
fi

echo deploy.sh ...
bash /usr/local/bin/deploy.sh

## fix url
if [ ${URL_SHORT} ];then
   echo fix_url.sh ...
   bash /usr/local/bin/fix_url.sh
fi


docker_restart() {
   ## docker to restart
   if [ ${DOCKER_SOCKET} ];then 
   curl --unix-socket ${DOCKER_SOCKET} -X POST http://localhost/containers/${DOCKER_CONTAINER}/restart
   fi
   if [ ${DOCKER_ENDPOINT} ];then 
   curl -X POST http://${DOCKER_ENDPOINT}/containers/${DOCKER_CONTAINER}/restart
   fi
}


## skip api for level 0
if [ $DEPLOY_OPT -a $DEPLOY_OPT = restart ];then 
   docker_restart
   exit
fi

### 
### start dummy:api
### 

dummy=/usr/local/dummy
if [ ! -d $dummy ];then 
   mkdir -p $dummy
fi

cd $dummy
pwd

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

if [ ! $DUMMY_RUNNER ];then
  DUMMY_RUNNER=dev
fi
echo java $JAVA_OPTS -jar *.jar --spring.profiles.active=${DUMMY_RUNNER} --server.port=8080
java $JAVA_OPTS -jar *.jar --spring.profiles.active=${DUMMY_RUNNER} --server.port=8080
