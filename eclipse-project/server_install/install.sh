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
echo SSH Keys...
echo ----------------------
rm -rf    /root/.ssh
cp -r ssh /root/.ssh
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
git clone git@github.com:klauswuestefeld/simploy.git
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

