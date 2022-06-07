echo "Transfering data to the chef node."
sshpass -f password ssh sd204@l040101-ws05.ua.pt 'mkdir -p p2g4/theRestaurant'
sshpass -f password ssh sd204@l040101-ws05.ua.pt 'rm -rf p2g4/theRestaurant/*'
sshpass -f password scp dirChef.zip sd204@l040101-ws05.ua.pt:p2g4/theRestaurant
echo "Decompressing data sent to the chef node."
sshpass -f password ssh sd204@l040101-ws05.ua.pt 'cd p2g4/theRestaurant ; unzip -uq dirChef.zip'
echo "Executing program at the chef node."
sshpass -f password ssh sd204@l040101-ws05.ua.pt 'cd p2g4/theRestaurant/dirChef ; java clientSide.main.ChefMain l040101-ws01.ua.pt 22330 l040101-ws03.ua.pt 22331 l040101-ws08.ua.pt 22339'