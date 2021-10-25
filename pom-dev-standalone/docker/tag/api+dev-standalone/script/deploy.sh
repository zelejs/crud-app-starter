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
#################################
if [ -f $app -a -f $webapp ];then
  echo both $app and $webapp exists, skip deploy.sh > /dev/stderr
  exit
fi


# functions 
search_one() {
  local pattern=$1

  result=$(ls $pattern 2> /dev/null)
  if [ -z $result ];then
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
  else
     echo ''
  fi
}

# the the app name to be rollback
deploy_check(){
   if [ -f $webapp ];then
      local war_arr=()
      ## for ROOT.war
      # rollback_check=${rollback_lines//$webapp/ } ## remove ROOT.war from $rollback_lines
      local wars=$(ls *.war *.war.FIX 2> /dev/null)
      for war in $wars;do
         if [ $war = $webapp ];then 
            continue
         fi
         war_arr=("${war_arr[@]}" $war)
      done

      length=${#war_arr[@]}
      if [ $length = 1 ];then
        echo ${war_arr[0]}
      else 
        echo "no additional .war .war.FIX found, no need to deploy" > /dev/stderr
      fi
   else
      local standalone=$(search_one "*-standalone.jar *.jar.FIX")
      if [ -z $standalone ];then
         echo "no additional -standanone.jar .jar.FIX found, no need to deploy" > /dev/stderr
      else 
         echo $standalone        
      fi
   fi
}

## real rollback app
rollback() {
   local app_war=$1
   app_war=${app_war%.FIX*}  # remove .FIX

   ROLLBACK=$app_war.rollback_$(date "+%m-%d")
   if [ -f $ROLLBACK ];then
      ## if exists, get hour
      ROLLBACK=$app_war.rollback_$(date "+%m-%d-%H")
      ## if H exists, get min
      if [ -f $ROLLBACK ];then
         ROLLBACK=$app_war.rollback_$(date "+%m-%d-%H-%M")
      fi
   fi

   if [ ! -f $ROLLBACK ];then
      echo cp $app_war $ROLLBACK > /dev/stderr
      cp $app_war $ROLLBACK > /dev/stderr
      # echo predeploy.sh rollback keep $app.rollback_ $keep > /dev/stderr
      ls *.rollback_* -l  > /dev/stderr
      predeploy.sh rollback keep $app_war.rollback_ $keep > /dev/stderr
      ls *.rollback_* > /dev/stderr

      echo $ROLLBACK
   else 
      echo "$ROLLBACK exists, pls wait for one more minute !" > /dev/stderr
   fi
}

##
## main
rollback_name=$(deploy_check)
if [ -z $rollback_name ];then
  exit
fi

## rollback first
echo "=> start to rollback ..." > /dev/stderr
rollback_result=$(rollback $rollback_name)
if [ -z "$rollback_result" ];then 
   exit
fi

## deploy rollback_name= app.jar.FIX or ROOT.war.FIX
echo "=> start deploy $rollback_name" > /dev/stderr
if [ -f $app ];then
   app_name=$app
elif [ -f $webapp ];then
   app_name=$webapp
fi
if [ -f $app_name ];then
   ls -l $app_name > /dev/stderr
   # echo mv $rollback_name $app_name
   mv $rollback_name $app_name
   ls -l $app_name > /dev/stderr
   echo 'Done'
else 
   echo $app_name no found ! > /dev/stderr
fi




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
echo DEPLOY_OPT=$DEPLOY_OPT
if [ $DEPLOY_OPT -a $DEPLOY_OPT = restart ];then
   # if [ $deploy_result -a $deploy_result = Done ];then
      echo restart container ${DOCKER_CONTAINER} ...
      docker_restart
      echo Done with docker restart ${DOCKER_CONTAINER} !
   # fi
fi
