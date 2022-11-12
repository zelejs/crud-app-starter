FROM adoptopenjdk:11-jre-hotspot-focal

WORKDIR /webapps

ADD ./dev-dependency-0.0.1-standalone.jar /webapps/app.jar

CMD ["/bin/sh","-c","java","-jar","app.jar"]
