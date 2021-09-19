#!/bin/sh
docker-compose -f docker+buildjar.yml build 
docker-compose -f docker+buildjar.yml up
