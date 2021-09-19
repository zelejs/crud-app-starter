#!/bin/sh

################################
## define your var for lib deploy
#export DL_ROLLBACK='env-test-api-1.0.0-standalone.jar'
#export DL_DOCKERNAME='api'
################################
dockername=${DL_DOCKERNAME}

## rollback
rollback=${DL_ROLLBACK}
keep=7  # keep 7 rollback instances

## main
app='app.jar'



# function get_rollback() {
## get from /var/tmp
standalone=''
if [ ! $rollback ];then
   if [ -f /var/tmp/.rollback ];then
      rollback=$(cat /var/tmp/.rollback)
   fi
fi

## get from standalone
if [ ! $rollback ];then
  #echo "standalone=$(ls *-standalone.jar)"
  standalone=$(ls *-standalone.jar)
  if [ $standalone ];then
     echo $standalone > /var/tmp/.rollback
  fi
fi

if [ ! $rollback ];then
  echo DL_ROLLBACK not yet defined
  exit
fi

rollback=$rollback.rollback_$(date "+%m-%d")
# }


# function check_app() {
## backup app for rollback
if [ ! -f $app ];then
  #echo "app=$(ls *-standalone.jar)"
  if [ -f $standalone ];then
     echo mv $standalone $app
     mv $standalone $app
  fi
  #i=$((i+1))
fi

## check app.jar
if [ ! -f $app ];then
   echo $app not found, fail to deploy !
   exit
fi
## }


# function rollback() {
if [ ! -f $rollback ];then
   echo cp $app $rollback
   cp $app $rollback
   echo predeploy.sh rollback keep ${rollback}.rollback_ $keep
   bash /usr/local/bin/predeploy.sh rollback keep ${rollback}.rollback_ $keep
fi
# }




# function restart docker for outside 
## if defined DL_DOCKERNAME, restart image container
## restart docker container
if [ $dockername ];then
  echo '# restart docker container required'
  echo docker-compose restart $dockername
  #docker-compose restart $dockername
else
  echo DL_DOCKERNAME not yet defined
  exit
fi
