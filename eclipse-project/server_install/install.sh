#!/bin/sh
mkdir /agitter

echo ----------------------
echo Java...
echo ----------------------
# Apparently sun/oracle jdk packages were removed from all Ubuntu repositories so we have to wget from Oracle. (April 2011)
wget http://download.oracle.com/otn-pub/java/jdk/6u25-b06/jdk-6u25-linux-i586.bin
mv jdk* jdk6.bin
chmod a+x jdk6.bin
./jdk6.bin
rm jdk6.bin
mv jdk* /agitter/jdk1.6

echo ----------------------
echo Ant...
echo ----------------------
# Simply using "apt-get install ant" would bring the openjdk as a dependency so we wget.
wget http://www.apache.org/dist/ant/ant-current-bin.zip
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
mv -r ssh /root/.ssh
chmod 600 /root/.ssh/id_rsa
chmod 600 /root/.ssh/id_rsa.pub
chmod 600 /root/.ssh/known_hosts

echo ----------------------
echo Git Repositories
echo ----------------------
install_dir=$PWD
mkdir /agitter/git_repositories
cd    /agitter/git_repositories
git clone git@github.com:teamware/agitos.git
git clone git@github.com:bihaiko/sneer.git
cd $install_dir

echo ----------------------
echo Init Script
echo ----------------------
mv agitter_boot.sh /etc/init.d/
chmod +x /etc/init.d/agitter_boot.sh
update-rc.d agitter_boot.sh defaults 80

rm install.sh