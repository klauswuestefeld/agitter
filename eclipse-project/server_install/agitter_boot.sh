#!/bin/sh

JAVA_HOME=/agitter/jdk1.6
export JAVA_HOME
PATH=$PATH:$JAVA_HOME/bin
export PATH

ANT_HOME=/agitter/ant
export ANT_HOME
PATH=$PATH:$ANT_HOME/bin
export PATH

cd /agitter/git_repositories/agitos/eclipse-project
ant simploy
