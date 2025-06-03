import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MissionGenesis {

    private MolecularData molecularDataHuman;
    private MolecularData molecularDataVitales;

    public MolecularData getMolecularDataHuman() {
        return molecularDataHuman;
    }

    public MolecularData getMolecularDataVitales() {
        return molecularDataVitales;
    }

    public void readXML(String file) {
        try {
            File input = new File(file);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(input);
            doc.getDocumentElement().normalize();

            // Process human molecular data
            NodeList humanList = doc.getElementsByTagName("HumanMolecularData");
            List<Molecule> humanMolecules = parseMolecularData(humanList);
            molecularDataHuman = new MolecularData(humanMolecules); // Here's the issue

            // Process Vitales molecular data
            NodeList vitalesList = doc.getElementsByTagName("VitalesMolecularData");
            List<Molecule> vitalesMolecules = parseMolecularData(vitalesList);
            molecularDataVitales = new MolecularData(vitalesMolecules); // Here's the issue

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Molecule> parseMolecularData(NodeList molecularData) {
        List<Molecule> molecules = new ArrayList<>();
        for (int i = 0; i < molecularData.getLength(); i++) {
            Node node = molecularData.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList moleculeList = element.getElementsByTagName("Molecule");
                for (int j = 0; j < moleculeList.getLength(); j++) {
                    Element mol = (Element) moleculeList.item(j);
                    String ID = mol.getElementsByTagName("ID").item(0).getTextContent();
                    int bondStrength = Integer.parseInt(mol.getElementsByTagName("BondStrength").item(0).getTextContent());
                    NodeList bondsNode = mol.getElementsByTagName("Bonds");
                    List<String> bonds = new ArrayList<>();
                    if (bondsNode.getLength() > 0) {
                        NodeList bondIds = ((Element) bondsNode.item(0)).getElementsByTagName("MoleculeID");
                        for (int k = 0; k < bondIds.getLength(); k++) {
                            bonds.add(bondIds.item(k).getTextContent());
                        }
                    }
                    Molecule molecule = new Molecule(ID, bondStrength, bonds);
                    molecules.add(molecule);
                }
            }
        }
        return molecules;
    }

}
