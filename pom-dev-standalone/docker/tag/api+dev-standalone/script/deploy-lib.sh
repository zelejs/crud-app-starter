#!/bin/sh
################################
## define your var for lib deploy
# export DL_ROLLBACK='env-test-saas-1.0.0-standalone.jar'
# export DL_DOCKERNAME='env-api'
################################
rollback=${DL_ROLLBACK}
dockername=${DL_DOCKERNAME}
app='app.jar'
keep=7 ## one week
webapps='/webapps'
#
#
## check environemnt
if [ ! $rollback ];then
  echo environment DL_ROLLBACK has not yet exported
  exit
fi


## check files
cd $webapps
if [ ! -d ./lib ];then
  echo './lib dir not found !'  >/dev/stderr  ## stderr
  exit
fi
if [ -z $(ls ./lib) ];then
  echo 'no lib found under ./lib !' >/dev/stderr  ## stderr
  exit
fi

## check app.jar, if no app.jar, then app=*-standalone.jar
if [ ! -f $app ];then 
   app=$(ls *-standalone.jar)
   ## found
   if [ $#==0 ];then
      echo 'no file matches pattern *-standalone.jar found !' >/dev/stderr
      exit
   fi
fi

## start fix
fixapp='app-fix.jar'
if [ ! -f lib/$fixapp ];then
   echo cp $app lib/$fixapp
   cp $app lib/$fixapp
fi


##
## iterate lib
cd lib
num=0
#
for lib in $(ls)
do
   num=$(($num+1))
   inf=$(jar tf $fixapp | grep $lib)
   if [ $inf ];then
     ## update lib
     echo $inf
     inf_dir=$(dirname $inf)
     if [ ! -d $inf_dir ];then
        mkdir -p $inf_dir
     fi
     echo mv $lib $inf_dir
     mv $lib $inf_dir
   fi
done

##
if [ $num -eq 0 ];then
   echo 'no lib to deploy !' >/dev/stderr
   exit
fi


## deploy lib
if [ -d BOOT-INF ];then
 for lib in $(ls BOOT-INF/lib)
 do
   inf=$(jar tf $fixapp | grep $lib)
   if [ $inf ];then
      echo jar 0uf $fixapp $inf
      jar 0uf $fixapp $inf
   fi
 done
fi
## working dir
cd ..
pwd


##
## rollback
rollback_issue=$rollback.rollback_$(date "+%m-%d")
if [ ! -f $rollback_issue ];then
   echo mv $app $rollback_issue
   mv $app $rollback_issue

   # Usage: predeploy.sh rollback keep <pattern> <num>
   if [ -f ./predeploy.sh ];then
      bash ./predeploy.sh rollback keep $rollback.rollback_ $keep
   fi
fi

##
#deploy
echo mv lib/$fixapp $app
if [ ! ${DEBUG} ];then
   mv lib/$fixapp $app
fi


#-------
#cleanup 
echo rm -rf lib/*
if [ ! ${DEBUG} ];then
   rm -rf lib/*
fi


#------
#restart docker container only when dockername is defined
if [ $dockername ];then
  echo docker-compose restart $dockername
  if [ ! ${DEBUG} ];then
     docker-compose restart $dockername
     #docker-compose -f docker-compose.yml restart redis
  fi
fi

echo success

