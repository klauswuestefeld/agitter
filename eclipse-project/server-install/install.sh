#!/bin/sh

echo ---- Linux Package Upgrades
aptitude -y upgrade
aptitude -y safe-update

echo
echo
echo ---- Timezone
# Choose your timezone: http://en.wikipedia.org/wiki/List_of_tz_database_time_zones
echo "America/Sao_Paulo" > /etc/timezone
dpkg-reconfigure -f noninteractive tzdata

echo ----------------------
echo Directories...
echo ----------------------
mkdir /agitter
mkdir /agitter/production
mkdir /agitter/staging

echo ----------------------
echo Java...
echo ----------------------
# Apparently sun/oracle jdk packages were removed from all Ubuntu repositories so we have to wget from Oracle. (April 2011)
wget http://www.java.net/download/jdk7u4/archive/b18/binaries/jdk-7u4-ea-bin-b18-linux-i586-29_mar_2012.tar.gz
mv jdk* jdk.tar.gz
tar -xf jdk.tar.gz
rm jdk.tar.gz
mv jdk* /agitter/jdk

echo ----------------------
echo Ant...
echo ----------------------
# Simply using "apt-get install ant" would bring the openjdk as a dependency so we wget.
wget http://archive.apache.org/dist/ant/ant-current-bin.zip
mv ant* ant.zip
unzip ant.zip
rm ant.zip
mv *ant* /agitter/ant

echo ----------------------
echo git...
echo ----------------------
apt-get -y install git-core

echo ----------------------
echo SSH Keys...
echo ----------------------
rm -rf    /root/.ssh
cp -r ssh /root/.ssh
chmod 600 /root/.ssh/*

rm -rf /etc/ssh
cp -r etc-ssh /etc/ssh
chmod 600 /etc/ssh/*

echo ----------------------
echo Git Repositories
echo ----------------------
install_dir=$PWD
mkdir /agitter/git_repositories
cd    /agitter/git_repositories
git clone git@github.com:klauswuestefeld/agitter.git
git clone git@github.com:klauswuestefeld/sneer.git
cd $install_dir

echo ----------------------
echo Init Script
echo ----------------------
cat agitter_env_variables.sh >> /etc/profile
cp agitter*.sh /etc/init.d/
chmod +x /etc/init.d/agitter*.sh
update-rc.d agitter_boot.sh defaults 80

