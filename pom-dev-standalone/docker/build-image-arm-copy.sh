#!/bin/sh
docker-compose -f docker+buildimage+arm+copy.yml build --force-rm --no-cache
