1) Copiar o script de instalacao pro server:

Estando no diretorio server_install:

   scp -r ../server_install root@agitter.com:/tmp


2) Rodar o script no server como root:

   cd /tmp/server_install
   chmod a+x install.sh
   ./install.sh


3) Dar reboot no server.

   cd /
   reboot

