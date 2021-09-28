#!/bin/sh
################################
## define your var for lib deploy
################################
app='app.jar'

JAR_BIN=$(which jar)
JAVA_BIN=$(which java)

checkdependency() {
  app=$1
  jar=$2

  jarlibroot=/usr/local/lib
  ## for unit test
  if [ ${JAR_D_ROOT} ];then
    jarlibroot=${JAR_D_ROOT}
  fi

  result=$("$JAVA_BIN" -jar $jarlibroot/jar-dependency.jar -cm  $app $jar)
  echo $result
}

putlocaljars() {
  app=$1
  libroot=$2

  num=0
  for lib in $(ls $libroot)
  do
     jar=$libroot/$lib
     # only .jar get jar
     libext=${lib##*.}
     if [ ! $libext = 'jar' ];then
        continue
     fi

     num=$(($num+1))

     ## check dependency, if required new dependencies, skip
     dependencies=$(checkdependency $app $jar)
     if [ ${#dependencies} -gt 0 ];then
        echo fail to depoy lib for dependencies: >/dev/stderr
        for it in $dependencies;do
          echo $it >/dev/stderr
        done
        continue
     fi

     jarlib=$(basename $lib)
     jarok=$("$JAR_BIN" tf $app | grep $jarlib)
     #echo jarlib=$jarlib, jarok=$jarok
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

