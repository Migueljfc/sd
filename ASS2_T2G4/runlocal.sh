cd /home/miguel/Documents/ij/ASS2_T2G4/src/ || return 0
javac -cp /home/miguel/Downloads/genclass.jar */*.java */*/*.java


 gnome-terminal --title="GenReposMain" -- java -cp .:/home/rodrigo/Documents/genclass.jar serverSide.main.GeneralRepositoryMain 22139
 gnome-terminal --title="TableMain" -- java -cp .:/home/rodrigo/Documents/genclass.jar serverSide.main.TableMain 22132 127.0.0.1 22139
 gnome-terminal --title="BarMain" -- java -cp .:/home/rodrigo/Documents/genclass.jar serverSide.main.BarMain 22130 127.0.0.1 22139 127.0.0.1 22132
 gnome-terminal --title="KitchenMain" -- java  -cp .:/home/rodrigo/Documents/genclass.jar serverSide.main.KitchenMain 22131 127.0.0.1 22139

  
 sleep 1 
 gnome-terminal --title="WaiterMain" -- java -cp .:/home/rodrigo/Documents/genclass.jar clientSide.main.WaiterMain 127.0.0.1 22130 127.0.0.1 22132 127.0.0.1 22131 127.0.0.1 22139
 gnome-terminal --title="ChefMain" -- java -cp .:/home/rodrigo/Documents/genclass.jar clientSide.main.ChefMain 127.0.0.1 22130 127.0.0.1 22131 127.0.0.1 22139
 sleep 1 
 gnome-terminal --title="StudentMain" -- java -cp .:/home/rodrigo/Documents/genclass.jar clientSide.main.StudentMain 127.0.0.1 22130 127.0.0.1 22132 127.0.0.1 22139
