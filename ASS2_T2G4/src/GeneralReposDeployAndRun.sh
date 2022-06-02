echo "Transfering data to the general repository node."
sshpass -f password ssh sd204@l040101-ws08.ua.pt 'mkdir -p p2g4/theRestaurant'
sshpass -f password ssh sd204@l040101-ws08.ua.pt 'rm -rf p2g4/theRestaurant/*'
sshpass -f password scp dirGeneralRepos.zip sd204@l040101-ws08.ua.pt:p2g4/theRestaurant
echo "Decompressing data sent to the general repository node."
sshpass -f password ssh sd204@l040101-ws08.ua.pt 'cd p2g4/theRestaurant ; unzip -uq dirGeneralRepos.zip'
echo "Executing program at the server general repository."
sshpass -f password ssh sd204@l040101-ws08.ua.pt 'cd p2g4/theRestaurant/dirGeneralRepos ; java serverSide.main.GeneralRepositoryMain 22339'
echo "Server shutdown."
sshpass -f password ssh sd204@l040101-ws08.ua.pt 'cd p2g4/theRestaurant/dirGeneralRepos ; less logger'