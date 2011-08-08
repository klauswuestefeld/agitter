1) Copiar o script de instalacao pro server:

Estando no diretorio server-install:

   scp -r ../server-install root@agitter.com:/tmp
   

2) Rodar o script no server como root:

   cd /tmp/server-install
   chmod a+x install.sh
   ./install.sh


3) Dar reboot no server.

   reboot

