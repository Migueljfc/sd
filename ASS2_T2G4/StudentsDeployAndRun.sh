echo "Transfering data to the students node."
sshpass -f password ssh sd204@l040101-ws07.ua.pt 'mkdir -p p2g4/theRestaurant'
sshpass -f password ssh sd204@l040101-ws07.ua.pt 'rm -rf p2g4/theRestaurant/*'
sshpass -f password scp dirStudents.zip sd204@l040101-ws07.ua.pt:p2g4/theRestaurant
echo "Decompressing data sent to the students node."
sshpass -f password ssh sd204@l040101-ws07.ua.pt 'cd p2g4/theRestaurant ; unzip -uq dirStudents.zip'
echo "Executing program at the students node."
sshpass -f password ssh sd204@l040101-ws07.ua.pt 'cd p2g4/theRestaurant/dirStudents ; java clientSide.main.StudentMain l040101-ws01.ua.pt 22330 l040101-ws10.ua.pt 22332 l040101-ws08.ua.pt 22339'