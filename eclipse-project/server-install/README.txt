1) Copiar o script de instalacao pro server:

Estando no diretorio server-install:

   scp -r ../server-install root@agitter.com:/tmp
   
   (Fingerprint do server: 52:15:7f:f6:20:32:1a:e5:bc:14:da:fb:63:0c:84:1b)


2) Rodar o script no server como root:

   cd /tmp/server-install
   chmod a+x install.sh
   ./install.sh


3) Dar reboot no server.

   reboot

