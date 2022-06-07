echo "Transfering data to the bar node."
sshpass -f password ssh sd204@l040101-ws01.ua.pt 'mkdir -p p2g4/theRestaurant'
sshpass -f password ssh sd204@l040101-ws01.ua.pt 'rm -rf p2g4/theRestaurant/*'
sshpass -f password scp dirBar.zip sd204@l040101-ws01.ua.pt:p2g4/theRestaurant
echo "Decompressing data sent to the bar node."
sshpass -f password ssh sd204@l040101-ws01.ua.pt 'cd p2g4/theRestaurant ; unzip -uq dirBar.zip'
echo "Executing program at the bar node."
sshpass -f password ssh sd204@l040101-ws01.ua.pt 'cd p2g4/theRestaurant/dirBar ; java serverSide.main.BarMain 22330 l040101-ws08.ua.pt 22339 l040101-ws10.ua.pt 22332'