#!/bin/sh

. /etc/init.d/agitter_env_variables.sh

cd /agitter/git_repositories/agitter/eclipse-project
ant -Dbuild=/agitter/simploy-build simploy
