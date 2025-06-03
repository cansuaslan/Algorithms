import java.util.*;
import java.util.stream.Collectors;

public class MolecularData {

    private final List<Molecule> molecules; // List of molecules

    public MolecularData(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    public List<Molecule> getMolecules() {
        return molecules;
    }
    public List<MolecularStructure> identifyMolecularStructures() {
        List<MolecularStructure> structures = new ArrayList<>();
        Set<Molecule> visited = new HashSet<>();
        for (Molecule molecule : molecules){
            for (String bondID : molecule.getBonds()){
                if (!visited.contains(molecule) || !visited.contains(findMoleculeById(bondID))){
                    MolecularStructure structure = new MolecularStructure();
                    identifyStructureDFS(molecule, visited, structure);
                    structures.add(structure);
                }
            }
        }
        for (MolecularStructure structure : structures){
            structure.getMolecules().sort(Molecule::compareTo);
        }
        return structures;
    }

    private void identifyStructureDFS(Molecule molecule, Set<Molecule> visited, MolecularStructure structure) {
        visited.add(molecule);
        structure.addMolecule(molecule);
        for (String bondID : molecule.getBonds()) {
            Molecule nextMolecule = findMoleculeById(bondID);
            if (nextMolecule != null && !visited.contains(nextMolecule)) {
                identifyStructureDFS(nextMolecule, visited, structure);
            }
        }
        for (Molecule reverseMolecule : molecules) {
            if (!visited.contains(reverseMolecule) && reverseMolecule.getBonds().contains(molecule.getId())) {
                identifyStructureDFS(reverseMolecule, visited, structure);
            }
        }
    }


    private Molecule findMoleculeById(String ID) {
        for (Molecule molecule : molecules) {
            if (molecule.getId().equals(ID)) {
                return molecule;
            }
        }
        return null;
    }

    public void printMolecularStructures(List<MolecularStructure> molecularStructures, String species) {
        System.out.println(molecularStructures.size() + " molecular structures have been discovered in " + species + ".");
        int structureCount = 1;
        for (MolecularStructure structure : molecularStructures) {
            System.out.println("Molecules in Molecular Structure " + structureCount + ": " + structure);
            structureCount++;
        }
    }

    public static ArrayList<MolecularStructure> getVitalesAnomaly(List<MolecularStructure> sourceStructures, List<MolecularStructure> targetStructures) {
        ArrayList<MolecularStructure> anomalyList = new ArrayList<>();
        for (MolecularStructure targetStructure : targetStructures) {
            if (!sourceStructures.contains(targetStructure)) {
                anomalyList.add(targetStructure);
            }
        }
        return anomalyList;
    }

    public void printVitalesAnomaly(List<MolecularStructure> molecularStructures) {
        System.out.println("Molecular structures unique to Vitales individuals:");
        for (MolecularStructure structure : molecularStructures) {
            System.out.println(structure.getMolecules());
        }
    }
}
