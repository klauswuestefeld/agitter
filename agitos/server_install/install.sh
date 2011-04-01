#bash
echo ------------------
echo Agitos Intallation
echo ------------------
echo .
echo ------------------
echo apt install....
echo ------------------
apt-get install git-core
apt-get install java
apt-get install ant
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
echo retreiving build.xml
echo ----------------------
mkdir /agitos_server
mkdir /agitos_server/git_repositories
cd /agitos_server/git_repositories
git clone git@github.com:teamware/agitos.git
git clone git@github.com:bihaiko/sneer.git
echo .
echo ----------------------
echo Tomcat
echo ----------------------
cd /agitos_server
wget http://linorg.usp.br/apache/tomcat/tomcat-7/v7.0.11/bin/apache-tomcat-7.0.11.tar.gz
tar -xf *.tar.gz
rm *.tar.gz
mv *tomcat* tomcat
echo .

