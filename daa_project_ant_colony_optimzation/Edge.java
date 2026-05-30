package ACOShortestPath;

public class Edge {
    int from;
    int to;
    double distance;
    double pheromone;

    public Edge(int from, int to, double distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.pheromone = 1.0; // initial pheromone
    }
}