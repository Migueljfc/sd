cd /home/miguel/Documents/ij/ASS2_T2G4/src/ || return 0
javac -cp /home/miguel/Downloads/genclass.jar */*.java */*/*.java


 gnome-terminal --title="GenReposMain" -- java -cp .:/home/miguel/Documents/ij/ASS2_T2G4/src/genclass.jar serverSide.main.GeneralRepositoryMain 22169
 gnome-terminal --title="BarMain" -- java -cp .:/home/miguel/Documents/ij/ASS2_T2G4/src/genclass.jar serverSide.main.BarMain 22160 127.0.0.1 22169 127.0.0.1 22162
 gnome-terminal --title="KitchenMain" -- java  -cp .:/home/miguel/Documents/ij/ASS2_T2G4/src/genclass.jar serverSide.main.KitchenMain 22161 127.0.0.1 22169
 gnome-terminal --title="TableMain" -- java -cp .:/home/miguel/Documents/ij/ASS2_T2G4/src/genclass.jar serverSide.main.TableMain 22162 127.0.0.1 22169
  
 sleep 1 
 gnome-terminal --title="WaiterMain" -- java -cp .:/home/miguel/Documents/ij/ASS2_T2G4/src/genclass.jar clientSide.main.WaiterMain 127.0.0.1 22160 127.0.0.1 22162 127.0.0.1 22161 127.0.0.1 22169 log.txt
 gnome-terminal --title="ChefMain" -- java -cp .:/home/miguel/Documents/ij/ASS2_T2G4/src/genclass.jar clientSide.main.ChefMain 127.0.0.1 22160 127.0.0.1 22161
 sleep 1 
 gnome-terminal --title="StudentMain" -- java -cp .:/home/miguel/Documents/ij/ASS2_T2G4/src/genclass.jar clientSide.main.StudentMain 127.0.0.1 22160 127.0.0.1 22162
