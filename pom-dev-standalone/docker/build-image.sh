#!/bin/sh

#docker image rm zelejs/api:dummy
#docker image rm api:dummy

docker-compose -f docker+buildimage.yml build --force-rm --no-cache
