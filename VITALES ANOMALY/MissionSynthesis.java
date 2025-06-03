import java.util.*;

public class MissionSynthesis {

    private final List<MolecularStructure> humanStructures; // Molecular structures for humans
    private final ArrayList<MolecularStructure> diffStructures; // Anomalies in Vitales structures compared to humans

    public MissionSynthesis(List<MolecularStructure> humanStructures, ArrayList<MolecularStructure> diffStructures) {
        this.humanStructures = humanStructures;
        this.diffStructures = diffStructures;
    }

    public List<Bond> synthesizeSerum() {
        List<Bond> serum = new ArrayList<>();
        List<String> serumCheck = new ArrayList<>();
        List<Molecule> selectedMolecules = new ArrayList<>();

        for (MolecularStructure structure : humanStructures) {
            Molecule lowest = structure.getMoleculeWithWeakestBondStrength(); // Assuming such a method exists
            selectedMolecules.add(lowest);
        }
        for (MolecularStructure structure : diffStructures) {
            Molecule lowest = structure.getMoleculeWithWeakestBondStrength(); // Assuming such a method exists
            selectedMolecules.add(lowest);
        }


        for (int i = 0; i < selectedMolecules.size(); i++) {
            double minStrength = Double.MAX_VALUE;
            Molecule bestBond = null;
            Molecule m1 = selectedMolecules.get(i);
            for (int j = 0; j < selectedMolecules.size(); j++) {
                if (i == j){continue;}
                Molecule m2 = selectedMolecules.get(j);
                double strength = ((double) m1.getBondStrength() + m2.getBondStrength()) / 2;
                if (strength < minStrength){
                    minStrength = strength;
                    bestBond = m2;
                }
            }
            if (bestBond != null){
                serumCheck.add(m1.getId() + bestBond.getId());
                if(serumCheck.contains(m1.getId() + bestBond.getId()) && serumCheck.contains(bestBond.getId() + m1.getId())){
                    continue;
                }
                serum.add(new Bond(m1, bestBond, minStrength));
            }
        }
        return serum;
    }



    public void printSynthesis(List<Bond> serum) {

        System.out.println("Synthesizing the serum...");
        double totalStrength = 0.0;
        for (Bond bond : serum) {
            if (bond.getTo().compareTo(bond.getFrom()) > 0){
                System.out.printf("Forming a bond between %s - %s with strength %.2f\n",
                        bond.getFrom().getId(),
                        bond.getTo().getId(),
                        bond.getWeight());
            }else {
                System.out.printf("Forming a bond between %s - %s with strength %.2f\n",
                        bond.getTo().getId(),
                        bond.getFrom().getId(),
                        bond.getWeight());
            }

            totalStrength += bond.getWeight();
        }
        System.out.printf("The total serum bond strength is %.2f\n", totalStrength);

    }
}
