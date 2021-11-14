#mvn org.apache.maven.plugins:maven-dependency-plugin:3.1.2:copy \
#  -DrepoUrl=http://120.79.49.72:8081 \
#  -Dtransitive=false \
#  -Dartifact="com.jfeat:jar-dependency-api:1.0.0" -Dmdep.stripVersion -DoutputDirectory=/tmp/


mvn dependency:get \
    -DremoteRepositories=http://120.79.49.72:8081 \
        -Dartifact=com.jfeat:jar-dependency-api:1.0.0 \
        -Dtransitive=false \
        -DoutputDirectory=/tmp
