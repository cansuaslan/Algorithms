import java.util.*;
import java.io.Serializable;

class Graph<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<T, List<Edge<T>>> adjVertices = new HashMap<>();

    public void addVertex(T v) {
        adjVertices.putIfAbsent(v, new ArrayList<>());
    }

    public void addEdge(T src, T dest, double weight, boolean isTrainRide) {
        //adjVertices.get(src).add(new Edge<>(src, dest, weight, isTrainRide));
        //adjVertices.get(dest).add(new Edge<>(dest, src, weight, isTrainRide));
        adjVertices.get(src).add(new Edge<>(src, dest, weight, isTrainRide));
    }

    public List<Edge<T>> getEdges(T v) {
        return adjVertices.getOrDefault(v, new ArrayList<>());
    }

    public Set<T> getVertices() {
        return adjVertices.keySet();
    }
}

class Edge<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private final T source;
    private final T destination;
    private final double weight;
    private final boolean isTrainRide;

    public Edge(T source, T destination, double weight, boolean isTrainRide) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.isTrainRide = isTrainRide;
    }

    public T getSource() {
        return source;
    }

    public T getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    public boolean isTrainRide() {
        return isTrainRide;
    }
}
