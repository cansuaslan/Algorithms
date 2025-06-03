import java.io.Serializable;
import java.util.*;

class UrbanTransportationApp implements Serializable {
    static final long serialVersionUID = 99L;
    
    public HyperloopTrainNetwork readHyperloopTrainNetwork(String filename) {
        HyperloopTrainNetwork hyperloopTrainNetwork = new HyperloopTrainNetwork();
        hyperloopTrainNetwork.readInput(filename);
        return hyperloopTrainNetwork;
    }

    /**
     * Function calculate the fastest route from the user's desired starting point to 
     * the desired destination point, taking into consideration the hyperloop train
     * network. 
     * @return List of RouteDirection instances
     */
    public List<RouteDirection> getFastestRouteDirections(HyperloopTrainNetwork network) {
        Graph<Station> graph = createGraph(network);
        if (graph == null) {
            throw new IllegalStateException("Graph is null");
        }

        Set<Station> visited = new HashSet<>();

        Map<Station, Double> distance = new HashMap<>();
        for (Station station : graph.getVertices()) {
            distance.put(station, Double.MAX_VALUE);
        }
        distance.put(network.startPoint, 0.0); // Distance from the starting point to itself is 0

        PriorityQueue<Station> pq = new PriorityQueue<>(Comparator.comparingDouble(distance::get));
        pq.add(network.startPoint);

        Map<Station, Station> previousStations = new HashMap<>();

        while (!pq.isEmpty()) {
            Station current = pq.poll();
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            if (current.equals(network.destinationPoint)) {
                break;
            }

            List<Edge<Station>> edges = graph.getEdges(current);
            for (Edge<Station> edge : edges) {
                Station neighbor = edge.getDestination();
                if (visited.contains(neighbor)) {
                    continue; // Neighbor already visited
                }

                double tentativeDistance = distance.get(current) + edge.getWeight();

                if (tentativeDistance < distance.get(neighbor)) {
                    distance.put(neighbor, tentativeDistance);
                    previousStations.put(neighbor, current);
                    pq.add(neighbor);
                }
            }
        }

        List<RouteDirection> routeDirections = new ArrayList<>();
        Station current = network.destinationPoint;
        if (!previousStations.containsKey(current)) {
            return routeDirections; // No route found
        }

        while (!current.equals(network.startPoint)) {
            Station previous = previousStations.get(current);
            double time = getTimeBetweenStations(previous, current, network);
            boolean isTrainRide = isTrainRide(previous, current, network);
            routeDirections.add(0, new RouteDirection(previous.description, current.description, time, isTrainRide));
            current = previous;
        }
        return routeDirections;
    }


    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {
        if (directions.isEmpty()) {
            System.out.println("No route found.");
            return;
        }

        double totalTime = 0;
        for (RouteDirection direction : directions) {
            totalTime += direction.duration;
        }

        System.out.printf("The fastest route takes %.2f minute(s).\n", totalTime);

        System.out.println("Directions");
        System.out.println("----------");
        int step = 1;
        for (RouteDirection direction : directions) {
            System.out.println(step + ". " + direction.startStationName + " to " + direction.endStationName + " for " + String.format("%.2f", direction.duration) + " minutes.");
            step++;
        }
        
        // TODO: Your code goes here

    }
    private Graph<Station> createGraph(HyperloopTrainNetwork network) {
        Graph<Station> graph = new Graph<>();
        Set<Station> allStations = new HashSet<>();
        for (TrainLine line : network.lines) {
            List<Station> stations = line.trainLineStations;
            for (int i = 0; i < stations.size(); i++) {
                Station source = stations.get(i);
                graph.addVertex(source);
                if (i < stations.size() - 1) {
                    Station destination = stations.get(i + 1);
                    double weight = getTimeBetweenStations(source, destination, network);
                    graph.addEdge(source, destination, weight, true); // Assuming train rides between adjacent stations
                }
            }
        }

        List<Station> stationList = new ArrayList<>();
        for (int i = 0; i < stationList.size(); i++) {
            for (int j = i + 1; j < stationList.size(); j++) {
                Station station1 = stationList.get(i);
                Station station2 = stationList.get(j);
                double weight = getTimeBetweenStations(station1, station2, network);
                graph.addEdge(station1, station2, weight, false);
            }
        }
        return graph;
    }

    private double getTimeBetweenStations(Station source, Station destination, HyperloopTrainNetwork network) {
        double distance = Math.sqrt(Math.pow(destination.coordinates.x - source.coordinates.x, 2) +
                Math.pow(destination.coordinates.y - source.coordinates.y, 2));
        double walkingTime = distance / network.averageWalkingSpeed;
        double trainTime = distance / network.averageTrainSpeed;
        return Math.min(walkingTime, trainTime);
    }
    private boolean isTrainRide(Station source, Station destination, HyperloopTrainNetwork network) {
        for (TrainLine line : network.lines) {
            List<Station> stations = line.trainLineStations;
            if (stations.contains(source) && stations.contains(destination) &&
                    Math.abs(stations.indexOf(source) - stations.indexOf(destination)) == 1) {
                return true;
            }
        }
        return false;
    }
}