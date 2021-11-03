#!/usr/bin/env bash
## keep workding_dir from docker-compose.yml
# DEPLOY_OPT: [deploy, restart, dummy]
# deploy   -  deploy the classes, libs, standalone only without restart docker container
# restart  -  deploy the classes, libs, standalone only with restarting docker container
# dummy    -  deploy only without restart, and go on startup dummy api

echo get started .............................
## prepare /webapps/lib, /webapps/classes
if [ -d /var/webapps/classes ];then 
   if [ ! -z $(ls /var/webapps/classes) ];then 
      mv /var/webapps/classes/*.class /webapps/classes
   fi
fi
if [ -d /var/webapps/lib ];then 
   if [ ! -z $(ls /var/webapps/lib) ];then 
      mv /var/webapps/lib/*.jar /webapps/lib
   fi
fi

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


## fix url if require by docker-compose.yml
if [ ${URL_SHORT} ];then
   echo fix_url.sh ...
   bash /usr/local/bin/fix_url.sh
fi

## skip api for level 0
if [ $DEPLOY_OPT -a $DEPLOY_OPT = deploy ];then
   # echo Deploy option= $DEPLOY_OPT ... Done with deploy !
   exit
elif [ $DEPLOY_OPT -a $DEPLOY_OPT = restart ];then
   # echo Deploy option= $DEPLOY_OPT ... Done with restart !
   exit
elif [ $DEPLOY_OPT -a $DEPLOY_OPT = dummy ];then
   echo Deploy option= $DEPLOY_OPT ... Continue ...
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

if [ ! $DUMMY_RUNNER ];then
  DUMMY_RUNNER=dummy
fi
configYml=$CONFIG/application-$DUMMY_RUNNER.yml

## dummy api do not effect current /webapps
if [ ! -f $configYml ];then
   echo cp $config/application.yml $configYml
   cp $config/application.yml $configYml
fi

# logback-spring.xml
if [ ! -f $CONFIG/logback-spring.xml ];then
   echo cp $config/logback-spring.xml $CONFIG/logback-spring.xml
   cp $config/logback-spring.xml $CONFIG/logback-spring.xml
fi

if [ ${DUMMY_URL} ];then
  export RUNNER=${DUMMY_RUNNER}
  export URL_SHORT=${DUMMY_URL}

  echo fix_url.sh ... URL_SHORT=${URL_SHORT}, 
  bash /usr/local/bin/fix_url.sh
fi

echo java $JAVA_OPTS -jar *.jar --spring.profiles.active=${DUMMY_RUNNER} --server.port=8080
java $JAVA_OPTS -jar *.jar --spring.profiles.active=${DUMMY_RUNNER} --server.port=8080
