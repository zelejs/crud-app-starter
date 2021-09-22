#!/bin/sh
################################
## define your var for lib deploy
################################
app='app.jar'

JAR_BIN=$(which jar)

putlocaljars() {
  app=$1
  libroot=$2

  num=0
  for lib in $(ls $libroot)
  do
     num=$(($num+1))
     jar=$libroot/$lib

     jarlib=$(basename $lib)
     jarok=$("$JAR_BIN" tf $app | grep $jarlib)
     echo jarlib=$jarlib, jarok=$jarok
     if [ ! $jarok ];then
         ## means new jar
         echo "+ BOOT-INF/lib/$jarlib"
         jarok="BOOT-INF/lib/$jarlib"
     fi

     if [ $jarok ];then
       ## update lib
       echo $jarok
       jardir=$(dirname $jarok)
       if [ ! -d $jardir ];then
          echo mkdir -p $jardir
          mkdir -p $jardir
       fi

       # core
       echo mv $jar $jardir
       mv $jar $jardir
       echo jar 0uf $app $jarok
       "$JAR_BIN" 0uf $app $jarok

       ## rm after jar updated
       echo rm -f $jarok
       rm -f $jarok
     fi
  done
}

search_one() {
  pattern=$1

  result=$(ls $pattern 2> /dev/null)
  if [[ -z $result ]];then
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


## main
if [ ! -d lib ];then
  mkdir lib
fi
if [ -z $(ls lib) ];then
   echo 'no lib to deploy !' >/dev/stderr
   exit
fi

fixapp='app-fix-standalone.jar'
if [ ! -f $fixapp ];then
  ## check standalone.jar
  if [ ! -f $app ];then
    standalone=$(search_one "*-standalone.jar")
    if [ ! $standalone ];then
       echo 'no (or multi) -standalone.jar !' >/dev/stderr
       exit
    fi

    ## start
    echo mv $standalone $app
    mv $standalone $app
  fi

   echo cp $app $fixapp
   cp $app $fixapp

   echo putlocaljars $fixapp lib
   putlocaljars $fixapp lib
fi

## clean up
if [ -d BOOT-INF ];then
  rm -rf BOOT-INF/
fi

