#!/bin/sh
docker-compose -f docker+jar.yml build 
docker-compose -f docker+jar.yml up
