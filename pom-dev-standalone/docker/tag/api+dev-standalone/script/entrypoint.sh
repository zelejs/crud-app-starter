#!/usr/bin/env bash
## keep workding_dir from docker-compose.yml
# DEPLOY_OPT: [deploy, restart, dummy]
# deploy   -  deploy the classes, libs, standalone only without restart docker container
# restart  -  deploy the classes, libs, standalone only with restarting docker container
# dummy    -  deploy only without restart, and go on startup dummy api
## real rollback app
checksame(){
   app1=$1
   app2=$2
   
   app1_sum=$(sha256sum $app1)
   app1_sum=${app1_sum%%\ *}

   app2_sum=$(sha256sum $app2)
   app2_sum=${app2_sum%%\ *}

   if [[ $app1_sum = $app2_sum ]];then
       echo $app1_sum 
   fi
}


rollback() {
   local app_war=$1
   app_dir=${app_war%\/*}
   app_name=${app_war##*\/}
   app_sum=$(sha256sum $app_war)
   app_sum=${stand_sum%%\ *}

   ##### dayly
   ROLLBACK=$app_name.rollback_$(date "+%m-%d")
   if [ ! -f $app_dir/$ROLLBACK ];then
       echo cp $app_war $app_dir/$ROLLBACK > /dev/stderr
       cp $app_war $app_dir/$ROLLBACK > /dev/stderr
       echo $ROLLBACK
       return
   fi
   ## ROLLBACK exists, check if sha256 the same
   shasum=$(checksame $app_war $app_dir/$ROLLBACK)
   if [ $shasum ];then
       # is the same jar, just return 
       return 
   fi


   ##### hourly
   ## means not the same file, get the hourly ROLLBACK
   ROLLBACK=$app_name.rollback_$(date "+%m-%d-%H")
   if [ ! -f $app_dir/$ROLLBACK ];then
       echo cp $app_war $app_dir/$ROLLBACK > /dev/stderr
       cp $app_war $app_dir/$ROLLBACK > /dev/stderr
       echo $ROLLBACK
       return
   fi
   ## ROLLBACK exists, check if sha256 the same
   shasum=$(checksame $app_war $app_dir/$ROLLBACK)
   if [ $shasum ];then
      # is the same jar, just return 
       return 
   fi


   ##### minutely
   ## means not the same file, get the minutely ROLLBACK
   ROLLBACK=$app_name.rollback_$(date "+%m-%d-%H-%M")
   if [ ! -f $app_dir/$ROLLBACK ];then
       echo cp $app_war $app_dir/$ROLLBACK > /dev/stderr
       cp $app_war $app_dir/$ROLLBACK > /dev/stderr
       echo $ROLLBACK
       return
   fi
   ## ROLLBACK exists, check if sha256 the same
   shasum=$(checksame $app_war $app_dir/$ROLLBACK)
   if [ $shasum ];then
      # is the same jar, just return 
       return 
   fi

   ##### still the same
   echo "$ROLLBACK exists, pls wait for one more minute !" > /dev/stderr
}



echo get started .............................
## prepare /webapps/lib, /webapps/classes
if [ -d /var/webapps/classes ];then 
   if [ ! -z $(ls /var/webapps/classes/*.class 2> /dev/null) ];then 
      mv /var/webapps/classes/*.class /webapps/classes
   fi
fi
if [ -d /var/webapps/lib ];then 
   if [ ! -z $(ls /var/webapps/lib/*.jar 2> /dev/null) ];then 
      mv /var/webapps/lib/*.jar /webapps/lib
   fi
fi
if [ -d /var/webapps/app ];then 
   if [ ! -z $(ls /var/webapps/app/*-standalone.jar 2> /dev/null) ];then
      # check filename the same
      standalones=$(ls /var/webapps/app/*-standalone.jar)
      apps=$(ls /webapps/*-standalone.jar)
      standalonel=$standalones
      appl=$apps
      standalone_name=${standalonel##*\/}  ##basename
      app_name=${appl##*\/}  ##basename

      if [[ $standalone_name = $app_name ]];then
         shasum=$(checksame $standalonel $appl)
         if [ $shasum ];then
            echo "the sample jar: $standalone_name ... do nothing !" > /dev/stderr
         else 
            ## difference checksum sha256, roll it back
            rollback $appl

            ## copy the standalone to /webapps
            sha256sum $appl
            echo cp $standalonel $appl
            cp $standalonel $appl
            sha256sum $appl
         fi
      else
         ## not the same file name, just move the jar file
         echo mv /var/webapps/app/*-standalone.jar /webapps > /dev/stderr
         mv /var/webapps/app/*-standalone.jar /webapps
      fi
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
