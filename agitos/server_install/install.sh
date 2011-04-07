#bash
echo ------------------
echo Agitos Intallation
echo ------------------
mkdir /agitos_server
echo .
echo ------------------
echo apt-get install....
echo ------------------
apt-get -y install git-core
apt-get -y install openjdk-6-jdk
apt-get -y install ant
echo .
echo ----------------------
echo Tomcat
echo ----------------------
wget http://linorg.usp.br/apache/tomcat/tomcat-7/v7.0.11/bin/apache-tomcat-7.0.11.tar.gz
tar -xf *tomcat*.tar.gz
rm *.tar.gz
mv *tomcat* /agitos_server/tomcat
cp server.xml /agitos_server/tomcat/conf
rm -rf /agitos_server/tomcat/webapps/*
echo .
echo ----------------------
echo SSH Keys...
echo ----------------------
mkdir /root/.ssh
rm /root/.ssh/id_rsa
rm /root/.ssh/id_rsa.pub
rm /root/.ssh/known_hosts
cp id_rsa      /root/.ssh/id_rsa
cp id_rsa.pub  /root/.ssh/id_rsa.pub
cp known_hosts /root/.ssh/known_hosts
chmod 600 /root/.ssh/id_rsa
chmod 600 /root/.ssh/id_rsa.pub
chmod 600 /root/.ssh/known_hosts
echo .
echo ----------------------
echo Git Repositories
echo ----------------------
install_dir=$PWD
mkdir /agitos_server/git_repositories
cd    /agitos_server/git_repositories
git clone git@github.com:teamware/agitos.git
git clone git@github.com:bihaiko/sneer.git
cd agitos/agitos
ant build
ant deploy
cd $install_dir
echo .
echo ----------------------
echo Init Script
echo ----------------------
cp agitos_boot.sh /etc/init.d/
chmod +x /etc/init.d/agitos_boot.sh
update-rc.d agitos_boot.sh defaults 80

