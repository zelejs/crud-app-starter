#!/bin/sh
docker-compose -f docker+buildimage.yml build --force-rm --no-cache
