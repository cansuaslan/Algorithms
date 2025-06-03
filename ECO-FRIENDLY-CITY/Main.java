import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {

        // Set the default locale to English
        Locale locale = new Locale("en_EN"); 
        Locale.setDefault(locale);

        System.out.println("### URBAN INFRASTRUCTURE DEVELOPMENT START ###");
        UrbanInfrastructureDevelopment urbanInfrastructureDevelopment = new UrbanInfrastructureDevelopment();
        List<Project> projectList = urbanInfrastructureDevelopment.readXML(args[0]);
        //List<Project> projectList = urbanInfrastructureDevelopment.readXML("C:\\Users\\AslanPC\\IdeaProjects\\assignment4\\src\\part_1_sample_input.xml");
        urbanInfrastructureDevelopment.printSchedule(projectList);
        System.out.println("### URBAN INFRASTRUCTURE DEVELOPMENT END ###");

        System.out.println("### URBAN TRANSPORTATION APP START ###");
        UrbanTransportationApp urbanTransportationApp = new UrbanTransportationApp();
        HyperloopTrainNetwork network = urbanTransportationApp.readHyperloopTrainNetwork(args[1]);
        //HyperloopTrainNetwork network = urbanTransportationApp.readHyperloopTrainNetwork("C:\\Users\\AslanPC\\IdeaProjects\\assignment4\\src\\part_2_sample_input.dat");
        List<RouteDirection> directions = urbanTransportationApp.getFastestRouteDirections(network);
        urbanTransportationApp.printRouteDirections(directions);
        System.out.println("### URBAN TRANSPORTATION APP END ###");
    }
}

