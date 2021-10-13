#!/usr/bin/env bash
################################
#export ROLLBACK_KEEP_NUM=2
################################
keep=${ROLLBACK_KEEP_NUM}  # keep rollback instances

## global definition
## standalone.jar deploy=> app.jar
## .war deploy=> ROOT.war
app='app.jar'
webapp='ROOT.war'
if [ -f $app -a -f $webapp ];then
  echo both $app and $webapp exists, skip deploy.sh > /dev/stderr
  exit
fi

rollback() {
  app=$1
  rollback=$2

  if [ ! -f $app ];then
     echo mv $rollback $app > /dev/stderr
     mv $rollback $app
     echo 'Done'
     exit
  fi

  ROLLBACK=$app.rollback_$(date "+%m-%d")
  if [ ! -f $ROLLBACK ];then
     echo cp $app $ROLLBACK > /dev/stderr
     cp $app $ROLLBACK
     echo predeploy.sh rollback keep $app.rollback_ $keep > /dev/stderr
     predeploy.sh rollback keep $app.rollback_ $keep
  fi
}

search_one() {
  local pattern=$1

  result=$(ls $pattern 2> /dev/null)
  if [ ${#result} -eq 0 ];then
    echo no $pattern file found ! > /dev/stderr
    exit
  fi

  ## error
  local num=0
  for it in $result;do
     num=$(($num+1))
  done

  if [ $num -eq 1 ];then
     echo $result
  fi
  echo ''
}

get_rollback(){
   local rollback

   if [ -f $webapp ];then
      ## for ROOT.war
      rollback=$(ls *.war *.war.FIX 2> /dev/null)
      rollback=${rollback//$webapp/ }    ## remove ROOT.war from .war result
      if [ ${#rollback} -eq 0 ];then
         echo no *.war to rollback ! >/dev/stderr
         exit
      fi

      ## two, correct
      local selected
      for it in $rollback;do
         if [ ! $it = $webapp ];then
            selected=$it
         fi
      done
      ##
      if [ ${#selected} -eq 0 ];then
         echo no *.war to rollback ! >/dev/stderr
         exit
      fi

      rollback=$selected
   else
      rollback=$(search_one "*-standalone.jar *.war *.jar.FIX")
      if [ ! $rollback ];then
         echo 'no (or multi) -standalone.jar .war .jar.FIX found !' >/dev/stderr
         exit
      fi
   fi

   ## wether app.jar or ROOT.war
   # warornot=${rollback}
   # warornot=${rollback##*.}  ## get ext
   # if [ $warornot = war ];then
   #    app=$webapp
   # fi
   echo $rollback 
}

##
## main
##

## get rollback deploying app
rollback_name=$(get_rollback)
#if rollback.ext =war, app=ROOT.war else app.jar
if [ ! $rollback_name ];then
  exit
fi

rollback_ext=${rollback_name##*.}
if [ $rollback_ext = 'FIX' ];then
  rollback_ext=${rollback_name##*.}
fi
if [ $rollback_ext = 'war' ];then
  app=$webapp  # work as ROOT.war
fi

## rollback first
echo "=> start to rollback: $app $rollback_name" > /dev/stderr
rollback $app $rollback_name

## deploy app.jar or ROOT.war
echo "=> start deploy $rollback_name" > /dev/stderr
ls -l $app > /dev/stderr
mv $rollback_name $app
ls -l $app > /dev/stderr

echo 'Done'



## docker restart 
docker_restart() {
   ## docker to restart
   if [ ${DOCKER_SOCKET} ];then 
      echo curl --unix-socket ${DOCKER_SOCKET} -X POST http://localhost/containers/${DOCKER_CONTAINER}/restart
      curl --unix-socket ${DOCKER_SOCKET} -s -X POST http://localhost/containers/${DOCKER_CONTAINER}/restart
   fi
   if [ ${DOCKER_ENDPOINT} ];then 
      echo DOCKER_ENDPOINT= ${DOCKER_ENDPOINT}, curl -X POST http://${DOCKER_ENDPOINT}/containers/${DOCKER_CONTAINER}/restart
      curl -s -X POST http://${DOCKER_ENDPOINT}/containers/${DOCKER_CONTAINER}/restart
   fi
}

## skip api for level 0
if [ $DEPLOY_OPT -a $DEPLOY_OPT = restart ];then
   # if [ $deploy_result -a $deploy_result = Done ];then
      echo deploy option= $DEPLOY_OPT, restart container ${DOCKER_CONTAINER} ...
      docker_restart
      echo Done with docker restart ${DOCKER_CONTAINER} !
   # fi
fi
