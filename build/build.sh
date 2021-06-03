#!/bin/sh

# 1. service dir
SERVICE_DIR="$PWD"
echo "LOGGER SERVICE_DIR: $SERVICE_DIR"

# 2. build
mvn clean package

# 3. docker build
echo "build docker build"
docker build -t fuhui/openjdk8-ms:1.0 .
echo "docker build success"
