import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Main class
 */
public class Main {
    public static String[] readFile(String path){
        try{
            int i = 0;
            int length = Files.readAllLines(Paths.get(path)).size();
            String[] results = new String[length];
            for(String line : Files.readAllLines(Paths.get(path))){
                results[i++] = line;
            }
            return results;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {

        /** MISSION POWER GRID OPTIMIZATION BELOW **/

        System.out.println("##MISSION POWER GRID OPTIMIZATION##");
        ArrayList<Integer> energyDemands = new ArrayList<>();
        String[] lines = readFile(args[0]);
        if (lines != null) {
            for (String line : lines) {
                String[] parts = line.split(" ");
                for (String part : parts) {
                    energyDemands.add(Integer.parseInt(part));
                }
            }
        } else {
            System.out.println("Failed to read file.");
            return;
        }

        PowerGridOptimization powerGridOptimization = new PowerGridOptimization(energyDemands);

        OptimalPowerGridSolution powerGridSolution = powerGridOptimization.getOptimalPowerGridSolutionDP();

        int totalDemandedGigawatts = 0;
        for (int demand : energyDemands) {
            totalDemandedGigawatts += demand;
        }
        int max  = powerGridSolution.getmaxNumberOfSatisfiedDemands();
        ArrayList<Integer> efficiency = powerGridSolution.getHoursToDischargeBatteriesForMaxEfficiency();

        System.out.println("The total number of demanded gigawatts: " + totalDemandedGigawatts);
        System.out.println("Maximum number of satisfied gigawatts: " + max);
        System.out.println("Hours at which the battery bank should be discharged: " + efficiency);
        System.out.println("The number of unsatisfied gigawatts: " + (totalDemandedGigawatts - max));

        System.out.println("##MISSION POWER GRID OPTIMIZATION COMPLETED##");

        /** MISSION ECO-MAINTENANCE BELOW **/

        System.out.println("##MISSION ECO-MAINTENANCE##");

        String[] fileLines = readFile(args[1]);
        if (lines != null && fileLines.length > 1) {
            String[] esvInfo = fileLines[0].split(" ");
            int numESVs = Integer.parseInt(esvInfo[0]);
            int esvCapacity = Integer.parseInt(esvInfo[1]);

            ArrayList<Integer> maintenanceTasks = new ArrayList<>();
            String[] taskInfo = fileLines[1].split(" ");
            for (String task : taskInfo) {
                maintenanceTasks.add(Integer.parseInt(task));
            }

            OptimalESVDeploymentGP esvDeploymentGP = new OptimalESVDeploymentGP(maintenanceTasks);

            int minNumESVs = esvDeploymentGP.getMinNumESVsToDeploy(numESVs, esvCapacity);

            if (minNumESVs != -1) {
                System.out.println("The minimum number of ESVs to deploy: " + minNumESVs);
                ArrayList<ArrayList<Integer>> tasksAssignedToESVs = esvDeploymentGP.getMaintenanceTasksAssignedToESVs();
                for (int i = 0; i < tasksAssignedToESVs.size(); i++) {
                    System.out.println("ESV " + (i + 1) + " tasks: " + tasksAssignedToESVs.get(i));
                }
            } else {
                System.out.println("Warning: Mission Eco-Maintenance Failed.");
            }
        } else {
            System.out.println("Invalid file format for ESV maintenance data.");
        }

        System.out.println("##MISSION ECO-MAINTENANCE COMPLETED##");
    }
}
