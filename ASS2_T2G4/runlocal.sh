cd /home/miguel/Documents/ij/Assignment2/src/ &
javac -cp /home/miguel/Downloads/genclass.jar */*.java */*/*.java &

sleep 2


xterm  -T "General Repository" -hold -e java -cp .:/home/miguel/Downloads/genclass.jar serverSide.main.GeneralRepositoryMain 22139 &
xterm  -T "Table" -hold -e java -cp .:/home/miguel/Downloads/genclass.jar serverSide.main.TableMain 22132 127.0.0.1 22139 &
xterm  -T "Ktchen" -hold -e java  -cp .:/home/miguel/Downloads/genclass.jar serverSide.main.KitchenMain 22131 127.0.0.1 22139 &
xterm  -T "Bar" -hold -e java -cp .:/home/miguel/Downloads/genclass.jar serverSide.main.BarMain 22130 127.0.0.1 22139 127.0.0.1 22132 &


  
sleep 1
xterm  -T "Chef" -hold -e java -cp .:/home/miguel/Downloads/genclass.jar clientSide.main.ChefMain 127.0.0.1 22130 127.0.0.1 22131 127.0.0.1 22139 &
xterm  -T "Waiter" -hold -e java -cp .:/home/miguel/Downloads/genclass.jar clientSide.main.WaiterMain 127.0.0.1 22130 127.0.0.1 22132 127.0.0.1 22131 127.0.0.1 22139 &
xterm  -T "Student" -hold -e java -cp .:/home/miguel/Downloads/genclass.jar clientSide.main.StudentMain 127.0.0.1 22130 127.0.0.1 22132 127.0.0.1 22139 &

sleep 15
rm -f */*.class */*/*.class & echo "Deleted .class files"

