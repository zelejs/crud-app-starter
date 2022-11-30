FROM adoptopenjdk:11-jre-hotspot-focal

WORKDIR /webapps

ADD ./dev-assembly-0.0.1-standalone.jar /webapps/app.jar

CMD ["/bin/sh","-c","java -jar /webapps/app.jar --server.port=8080"]

