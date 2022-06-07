echo "Compiling source code."
javac -source 8 -target 8 -cp /home/miguel/Downloads/genclass.jar */*.java */*/*.java
echo "Distributing intermediate code to the different execution environments."
echo "  General Repository of Information"
rm -rf dirGeneralRepos
mkdir -p dirGeneralRepos dirGeneralRepos/serverSide dirGeneralRepos/serverSide/main dirGeneralRepos/serverSide/entities dirGeneralRepos/serverSide/sharedRegions dirGeneralRepos/clientSide dirGeneralRepos/clientSide/entities dirGeneralRepos/commInfra
cp serverSide/main/GeneralRepositoryMain.class dirGeneralRepos/serverSide/main
cp serverSide/entities/*.class dirGeneralRepos/serverSide/entities
cp serverSide/sharedRegions/*.class dirGeneralRepos/serverSide/sharedRegions
cp clientSide/entities/States.class dirGeneralRepos/clientSide/entities
cp commInfra/*.class dirGeneralRepos/commInfra
# cp serverSide/main/ExecConst.class serverSide/main/ServerRestaurantGeneralRepos.class dirGeneralRepos/serverSide/main
# cp serverSide/entities/GeneralReposClientProxy.class dirGeneralRepos/serverSide/entities
# cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/GeneralRepos.class dirGeneralRepos/serverSide/sharedRegions
# cp clientSide/entities/ChefState.class clientSide/entities/StudentState.class clientSide/entities/WaiterState.class dirGeneralRepos/clientSide/entities
# cp commInfra/Message.class commInfra/MessageType.class commInfra/MessageException.class commInfra/ServerCom.class dirGeneralRepos/commInfra
echo "  Table"
rm -rf dirTable
mkdir -p dirTable dirTable/serverSide dirTable/serverSide/main dirTable/serverSide/entities dirTable/serverSide/sharedRegions dirTable/clientSide dirTable/clientSide/entities dirTable/clientSide/stubs dirTable/commInfra
cp serverSide/main/TableMain.class dirTable/serverSide/main
cp serverSide/entities/*.class dirTable/serverSide/entities
cp serverSide/sharedRegions/*.class dirTable/serverSide/sharedRegions
cp clientSide/stubs/*.class dirTable/clientSide/stubs
cp clientSide/entities/States.class clientSide/entities/ChefCloning.class clientSide/entities/WaiterCloning.class clientSide/entities/StudentCloning.class dirTable/clientSide/entities
cp commInfra/*.class dirTable/commInfra
# cp serverSide/main/ExecConst.class serverSide/main/ServerRestaurantTable.class dirTable/serverSide/main
# cp serverSide/entities/TableClientProxy.class dirTable/serverSide/entities
# cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/TableInterface.class serverSide/sharedRegions/Table.class dirTable/serverSide/sharedRegions
# cp clientSide/entities/ChefState.class clientSide/entities/StudentState.class clientSide/entities/WaiterState.class clientSide/entities/ChefCloning.class \
#    clientSide/entities/StudentCloning.class clientSide/entities/WaiterCloning.class dirTable/clientSide/entities
# cp clientSide/stubs/GeneralReposStub.class dirTable/clientSide/stubs
# cp commInfra/*.class dirTable/commInfra
echo "  Bar"
rm -rf dirBar
mkdir -p dirBar dirBar/serverSide dirBar/serverSide/main dirBar/serverSide/entities dirBar/serverSide/sharedRegions dirBar/clientSide dirBar/clientSide/entities dirBar/clientSide/stubs dirBar/commInfra
cp serverSide/main/BarMain.class dirBar/serverSide/main
cp serverSide/entities/*.class dirBar/serverSide/entities
cp serverSide/sharedRegions/*.class dirBar/serverSide/sharedRegions
cp clientSide/stubs/*.class dirBar/clientSide/stubs
cp clientSide/entities/States.class clientSide/entities/ChefCloning.class clientSide/entities/WaiterCloning.class clientSide/entities/StudentCloning.class dirBar/clientSide/entities
cp commInfra/*.class dirBar/commInfra
# cp serverSide/main/ExecConst.class serverSide/main/ServerRestaurantBar.class dirBar/serverSide/main
# cp serverSide/entities/BarClientProxy.class dirBar/serverSide/entities
# cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/BarInterface.class serverSide/sharedRegions/Bar.class dirBar/serverSide/sharedRegions
# cp clientSide/entities/ChefState.class clientSide/entities/StudentState.class clientSide/entities/WaiterState.class clientSide/entities/ChefCloning.class \
#    clientSide/entities/StudentCloning.class clientSide/entities/WaiterCloning.class dirBar/clientSide/entities
# cp clientSide/stubs/GeneralReposStub.class dirBar/clientSide/stubs
# cp commInfra/*.class dirBar/commInfra
echo "  Kitchen"
rm -rf dirKitchen
mkdir -p dirKitchen dirKitchen/serverSide dirKitchen/serverSide/main dirKitchen/serverSide/entities dirKitchen/serverSide/sharedRegions dirKitchen/clientSide dirKitchen/clientSide/entities dirKitchen/clientSide/stubs dirKitchen/commInfra
cp serverSide/main/KitchenMain.class dirKitchen/serverSide/main
cp serverSide/entities/*.class dirKitchen/serverSide/entities
cp serverSide/sharedRegions/*.class dirKitchen/serverSide/sharedRegions
cp clientSide/stubs/*.class dirKitchen/clientSide/stubs
cp clientSide/entities/States.class clientSide/entities/ChefCloning.class clientSide/entities/WaiterCloning.class clientSide/entities/StudentCloning.class dirKitchen/clientSide/entities
cp commInfra/*.class dirKitchen/commInfra
# cp serverSide/main/ExecConst.class serverSide/main/ServerRestaurantKitchen.class dirKitchen/serverSide/main
# cp serverSide/entities/KitchenClientProxy.class dirKitchen/serverSide/entities
# cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/KitchenInterface.class serverSide/sharedRegions/Kitchen.class dirKitchen/serverSide/sharedRegions
# cp clientSide/entities/ChefState.class clientSide/entities/StudentState.class clientSide/entities/WaiterState.class clientSide/entities/ChefCloning.class \
#    clientSide/entities/StudentCloning.class clientSide/entities/WaiterCloning.class dirKitchen/clientSide/entities
# cp clientSide/stubs/GeneralReposStub.class dirKitchen/clientSide/stubs
# cp commInfra/*.class dirKitchen/commInfra
echo "  Waiter"
rm -rf dirWaiter
mkdir -p dirWaiter dirWaiter/serverSide dirWaiter/serverSide/main dirWaiter/clientSide dirWaiter/clientSide/main dirWaiter/clientSide/entities dirWaiter/clientSide/stubs dirWaiter/commInfra
cp clientSide/main/WaiterMain.class dirWaiter/clientSide/main
cp clientSide/entities/Waiter.class clientSide/entities/States.class dirWaiter/clientSide/entities
cp clientSide/stubs/*.class dirWaiter/clientSide/stubs
cp commInfra/*.class dirWaiter/commInfra
# cp serverSide/main/ExecConst.class dirWaiter/serverSide/main
# cp clientSide/main/WaiterMain.class dirWaiter/clientSide/main
# cp clientSide/entities/Waiter.class clientSide/entities/WaiterState.class dirWaiter/clientSide/entities
# cp clientSide/stubs/GeneralReposStub.class clientSide/stubs/BarStub.class clientSide/stubs/KitchenStub.class clientSide/stubs/TableStub.class dirChef/clientSide/stubs
# cp commInfra/Message.class commInfra/MessageType.class commInfra/MessageException.class commInfra/ClientCom.class dirWaiter/commInfra
echo "  Chef"
rm -rf dirChef
mkdir -p dirChef dirChef/serverSide dirChef/serverSide/main dirChef/clientSide dirChef/clientSide/main dirChef/clientSide/entities dirChef/clientSide/stubs dirChef/commInfra
cp clientSide/main/ChefMain.class dirChef/clientSide/main
cp clientSide/entities/Chef.class clientSide/entities/States.class dirChef/clientSide/entities
cp clientSide/stubs/*.class dirChef/clientSide/stubs
cp commInfra/*.class dirChef/commInfra
# cp serverSide/main/ExecConst.class dirChef/serverSide/main
# cp clientSide/main/ChefMain.class dirChef/clientSide/main
# cp clientSide/entities/Chef.class clientSide/entities/ChefState.class dirChef/clientSide/entities
# cp clientSide/stubs/GeneralReposStub.class clientSide/stubs/BarStub.class clientSide/stubs/KitchenStub.class clientSide/stubs/TableStub.class dirChef/clientSide/stubs
# cp commInfra/Message.class commInfra/MessageType.class commInfra/MessageException.class commInfra/ClientCom.class dirChef/commInfra
echo "  Students"
rm -rf dirStudents
mkdir -p dirStudents dirStudents/serverSide dirStudents/serverSide/main dirStudents/clientSide dirStudents/clientSide/main dirStudents/clientSide/entities dirStudents/clientSide/stubs dirStudents/commInfra
cp clientSide/main/StudentMain.class dirStudents/clientSide/main
cp clientSide/entities/Student.class clientSide/entities/States.class dirStudents/clientSide/entities
cp clientSide/stubs/*.class dirStudents/clientSide/stubs
cp commInfra/*.class dirStudents/commInfra
# cp serverSide/main/ExecConst.class dirStudents/serverSide/main
# cp clientSide/main/StudentMain.class dirStudents/clientSide/main
# cp clientSide/entities/Student.class clientSide/entities/StudentState.class dirStudents/clientSide/entities
# cp clientSide/stubs/GeneralReposStub.class clientSide/stubs/BarStub.class clientSide/stubs/KitchenStub.class clientSide/stubs/TableStub.class dirChef/clientSide/stubs
# cp commInfra/Message.class commInfra/MessageType.class commInfra/MessageException.class commInfra/ClientCom.class dirStudents/commInfra


echo "Compressing execution environments."
echo "  General Repository of Information"
rm -f  dirGeneralRepos.zip
zip -rq dirGeneralRepos.zip dirGeneralRepos
echo "  Bar"
rm -f  dirBar.zip
zip -rq dirBar.zip dirBar
echo "  Table"
rm -f  dirTable.zip
zip -rq dirTable.zip dirTable
echo "  Kitchen"
rm -f  dirKitchen.zip
zip -rq dirKitchen.zip dirKitchen
echo "  Chef"
rm -f  dirChef.zip
zip -rq dirChef.zip dirChef
echo "  Waiter"
rm -f  dirWaiter.zip
zip -rq dirWaiter.zip dirWaiter
echo "  Students"
rm -f  Students.zip
zip -rq dirStudents.zip dirStudents
echo "Deploying and decompressing execution environments."
mkdir -p /home/miguel/Documents/test_Assigment2
rm -rf /home/miguel/Documents/test_Assigment2/*
cp dirGeneralRepos.zip /home/miguel/Documents/test_Assigment2
cp dirBar.zip /home/miguel/Documents/test_Assigment2
cp dirTable.zip /home/miguel/Documents/test_Assigment2
cp dirKitchen.zip /home/miguel/Documents/test_Assigment2
cp dirChef.zip /home/miguel/Documents/test_Assigment2
cp dirWaiter.zip /home/miguel/Documents/test_Assigment2
cp dirStudents.zip /home/miguel/Documents/test_Assigment2
cd /home/miguel/Documents/test_Assigment2
unzip -q dirGeneralRepos.zip
unzip -q dirBar.zip
unzip -q dirTable.zip
unzip -q dirKitchen.zip
unzip -q dirChef.zip
unzip -q dirWaiter.zip
unzip -q dirStudents.zip
rm -f  dirGeneralRepos.zip
rm -f  dirBar.zip
rm -f  dirTable.zip
rm -f  dirKitchen.zip
rm -f  dirChef.zip
rm -f  dirWaiter.zip
rm -f  dirStudents.zip
