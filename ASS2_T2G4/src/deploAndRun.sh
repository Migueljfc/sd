xterm  -T "General Repository" -hold -e "./GeneralReposDeployAndRun.sh"
xterm  -T "Table" -hold -e "./TableDeployAndRun.sh" &
xterm  -T "Bar" -hold -e "./BarDeployAndRun.sh" &
xterm  -T "Kitchen" -hold -e "./KitchenDeployAndRun.sh" &
sleep 1
xterm  -T "Waiter" -hold -e "./WaiterDeployAndRun.sh" &
xterm  -T "Chef" -hold -e "./ChefDeployAndRun.sh" &
xterm  -T "Student" -hold -e "./StudentsDeployAndRun.sh" &