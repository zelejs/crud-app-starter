#!/bin/sh
docker-compose -f docker+buildimage+arm.yml build --force-rm --no-cache
