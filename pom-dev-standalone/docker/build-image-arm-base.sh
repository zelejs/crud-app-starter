#!/bin/sh
docker-compose -f docker+buildimage+arm+base.yml build --force-rm --no-cache
