echo "Transfering data to the kitchen node."
sshpass -f password ssh sd204@l040101-ws03.ua.pt 'mkdir -p p2g4/theRestaurant'
sshpass -f password ssh sd204@l040101-ws03.ua.pt 'rm -rf p2g4/theRestaurant/*'
sshpass -f password scp dirKitchen.zip sd204@l040101-ws03.ua.pt:p2g4/theRestaurant
echo "Decompressing data sent to the table node."
sshpass -f password ssh sd204@l040101-ws03.ua.pt 'cd p2g4/theRestaurant ; unzip -uq dirKitchen.zip'
echo "Executing program at the table node."
sshpass -f password ssh sd204@l040101-ws03.ua.pt 'cd p2g4/theRestaurant/dirKitchen ; java serverSide.main.KitchenMain 22331 l040101-ws08.ua.pt 22339'