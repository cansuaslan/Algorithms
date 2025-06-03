import java.util.ArrayList;

public class PowerGridOptimization {
    private ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour;

    public PowerGridOptimization(ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour) {
        this.amountOfEnergyDemandsArrivingPerHour = amountOfEnergyDemandsArrivingPerHour;
    }

    public ArrayList<Integer> getAmountOfEnergyDemandsArrivingPerHour() {
        return amountOfEnergyDemandsArrivingPerHour;
    }

    private int calculateMaxSatisfaction(int j, int[] SOL) {
        int maxSatisfied = 0;
        for (int i = 0; i < j; i++) {
            int minDemand = Math.min(amountOfEnergyDemandsArrivingPerHour.get(j - 1), (j - i) * (j - i));
            int satisfied = SOL[i] + minDemand;
            maxSatisfied = Math.max(satisfied, maxSatisfied);
        }
        return maxSatisfied;
    }

    public OptimalPowerGridSolution getOptimalPowerGridSolutionDP() {
        int N = amountOfEnergyDemandsArrivingPerHour.size();
        int[] SOL = new int[N + 1];
        ArrayList<ArrayList<Integer>> HOURS = new ArrayList<>();
        SOL[0] = 0;
        HOURS.add(new ArrayList<>());

        for (int j = 1; j <= N; j++) {
            int maxSatisfied = calculateMaxSatisfaction(j, SOL);
            SOL[j] = maxSatisfied;

            // Determine best hour list for max satisfaction
            int bestI = -1;
            for (int i = 0; i < j; i++) {
                int minDemand = Math.min(amountOfEnergyDemandsArrivingPerHour.get(j - 1), (j - i) * (j - i));
                if (SOL[i] + minDemand == maxSatisfied) {
                    bestI = i;
                    break;
                }
            }

            ArrayList<Integer> hourList = new ArrayList<>(HOURS.get(bestI));
            hourList.add(j);
            HOURS.add(hourList);
        }

        int maxSatisfiedDemands = SOL[N];
        ArrayList<Integer> hoursToDischarge = HOURS.get(N);
        return new OptimalPowerGridSolution(maxSatisfiedDemands, hoursToDischarge);
    }
}
