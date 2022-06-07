echo "Transfering data to the table node."
sshpass -f password ssh sd204@l040101-ws10.ua.pt 'mkdir -p p2g4/theRestaurant'
sshpass -f password ssh sd204@l040101-ws10.ua.pt 'rm -rf p2g4/theRestaurant/*'
sshpass -f password scp dirTable.zip sd204@l040101-ws10.ua.pt:p2g4/theRestaurant
echo "Decompressing data sent to the table node."
sshpass -f password ssh sd204@l040101-ws10.ua.pt 'cd p2g4/theRestaurant ; unzip -uq dirTable.zip'
echo "Executing program at the table node."
sshpass -f password ssh sd204@l040101-ws10.ua.pt 'cd p2g4/theRestaurant/dirTable ; java serverSide.main.TableMain 22332 l040101-ws08.ua.pt 22339'
