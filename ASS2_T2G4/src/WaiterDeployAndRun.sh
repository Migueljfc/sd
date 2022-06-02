echo "Transfering data to the waiter node."
sshpass -f password ssh sd204@l040101-ws06.ua.pt 'mkdir -p p2g4/theRestaurant'
sshpass -f password ssh sd204@l040101-ws06.ua.pt 'rm -rf p2g4/theRestaurant/*'
sshpass -f password scp dirWaiter.zip sd204@l040101-ws06.ua.pt:p2g4/theRestaurant
echo "Decompressing data sent to the waiter node."
sshpass -f password ssh sd204@l040101-ws06.ua.pt 'cd p2g4/theRestaurant ; unzip -uq dirWaiter.zip'
echo "Executing program at the waiter node."
sshpass -f password ssh sd204@l040101-ws06.ua.pt 'cd p2g4/theRestaurant/dirWaiter ; java clientSide.main.WaiterMain l040101-ws01.ua.pt 22330 l040101-ws10.ua.pt 22332 l040101-ws03.ua.pt 22331 l040101-ws08.ua.pt 22339'