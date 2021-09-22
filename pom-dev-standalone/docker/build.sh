#!/bin/sh
DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker-compose -f docker+buildjar.yml build
docker-compose -f docker+buildjar.yml up
