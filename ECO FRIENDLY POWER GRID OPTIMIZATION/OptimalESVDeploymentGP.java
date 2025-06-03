import java.util.ArrayList;
import java.util.Collections;

public class OptimalESVDeploymentGP {
    private ArrayList<Integer> maintenanceTaskEnergyDemands;
    private ArrayList<ArrayList<Integer>> maintenanceTasksAssignedToESVs = new ArrayList<>();

    public OptimalESVDeploymentGP(ArrayList<Integer> maintenanceTaskEnergyDemands) {
        this.maintenanceTaskEnergyDemands = maintenanceTaskEnergyDemands;
    }

    public ArrayList<ArrayList<Integer>> getMaintenanceTasksAssignedToESVs() {
        return maintenanceTasksAssignedToESVs;
    }

    public ArrayList<Integer> getMaintenanceTaskEnergyDemands() {
        return maintenanceTaskEnergyDemands;
    }

    private void initializeRemainingCapacities(int[] remainingCapacities, int maxESVCapacity) {
        for (int i = 0; i < remainingCapacities.length; i++) {
            remainingCapacities[i] = maxESVCapacity;
        }
    }

    private boolean assignTasksToESVs(int maxNumberOfAvailableESVs, int maxESVCapacity, int[] remainingEnergyCapacities) {
        for (int task : maintenanceTaskEnergyDemands) {
            boolean taskAssigned = false;
            for (int i = 0; i < maxNumberOfAvailableESVs; i++) {
                if (remainingEnergyCapacities[i] >= task) {
                    if (maintenanceTasksAssignedToESVs.size() <= i) {
                        maintenanceTasksAssignedToESVs.add(new ArrayList<>());
                    }
                    maintenanceTasksAssignedToESVs.get(i).add(task);
                    remainingEnergyCapacities[i] -= task;
                    taskAssigned = true;
                    break;
                }
            }
            if (!taskAssigned) {
                return false;
            }
        }
        return true;
    }

    public int getMinNumESVsToDeploy(int maxNumberOfAvailableESVs, int maxESVCapacity) {
        Collections.sort(maintenanceTaskEnergyDemands, Collections.reverseOrder());

        int[] remainingEnergyCapacities = new int[maxNumberOfAvailableESVs];
        initializeRemainingCapacities(remainingEnergyCapacities, maxESVCapacity);

        if (!assignTasksToESVs(maxNumberOfAvailableESVs, maxESVCapacity, remainingEnergyCapacities)) {
            return -1;
        }

        return maintenanceTasksAssignedToESVs.size();
    }
}
