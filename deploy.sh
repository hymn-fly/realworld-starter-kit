#!/bin/bash

REPOSITORY=/home/ec2-user/app/zip

echo "> Build 파일 복사"

JAR_NAME=$(ls -rt $REPOSITORY/*.jar | tail -n 1)

chmod +x $JAR_NAME

nohup java -jar $JAR_NAME