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
  echo both $app and $webapp exists, skip deploy.sh
  exit
fi

rollback() {
  app=$1
  rollback=$2

  if [ ! -f $app ];then
     echo mv $rollback $app
     mv $rollback $app
     echo done!
     exit
  fi

  ROLLBACK=$app.rollback_$(date "+%m-%d")
  if [ ! -f $ROLLBACK ];then
     echo cp $app $ROLLBACK
     cp $app $ROLLBACK
     echo predeploy.sh rollback keep $app.rollback_ $keep
     predeploy.sh rollback keep $app.rollback_ $keep
  fi
}

search_one() {
  pattern=$1

  result=$(ls $pattern 2> /dev/null)
  if [ ${#result} -eq 0 ];then
    echo no $pattern files found ! > /dev/stderr
    exit
  fi

  ## error
  num=0
  for it in $result;do
     num=$(($num+1))
  done

  if [ $num -eq 1 ];then
     echo $result
  fi
  echo ''
}

get_rollback(){
   unset rollback

   if [ -f $webapp ];then
      ## for ROOT.war
      rollback=$(ls *.war *.war.FIX 2> /dev/null)
      rollback=${rollback//$webapp/ }    ## remove ROOT.war from .war result
      if [ ${#rollback} -eq 0 ];then
         echo no *.war to rollback ! >/dev/stderr
         exit
      fi

      ## two, correct
      unset selected
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
      ## for app.jar
      rollback=$(ls *-standalone.jar *.jar.FIX 2> /dev/null)
      if [ ${#rollback} -eq 0 ];then
         echo no *-standalone.jar *.war to rollback ! >/dev/stderr
         exit
      fi

      rollback=$(search_one "*-standalone.jar *.jar.FIX")
      if [ ! $rollback ];then
         echo 'no (or multi) -standalone.jar found !' >/dev/stderr
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
echo "=> start to rollback: $app $rollback_name"
rollback $app $rollback_name

## deploy app.jar or ROOT.war
echo "=> start deploy $rollback_name"
ls -l $app
mv $rollback_name $app
ls -l $app
