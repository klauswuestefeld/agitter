#!/bin/sh

/agitos_server/tomcat/bin/startup.sh

cd /agitos_server/git_repositories/agitos/agitos
nohup ant simploy
