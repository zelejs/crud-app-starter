DEV=dev-standalone-1.0.0-standalone.jar
HOST=pi@192.168.3.204

unset app
if [ -f target/$DEV ];then 
  app=target/$DEV
fi
if [ -f ../target/$DEV ];then
  app=../target/$DEV
fi

scp $app $HOST:/home/pi/devops/target
ssh $HOST ls -l /home/pi/devops/target/$DEV
