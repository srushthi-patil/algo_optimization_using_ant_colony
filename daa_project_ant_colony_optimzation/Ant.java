package ACOShortestPath;

import java.util.ArrayList;
import java.util.List;

public class Ant {

    // =========================
    // VISITED NODES
    // =========================
    boolean[] visited;

    // =========================
    // PATH TAKEN
    // =========================
    List<Integer> path;

    // =========================
    // TOTAL DISTANCE
    // =========================
    double pathLength;

    // =========================
    // CONSTRUCTOR
    // =========================
    public Ant(int totalNodes, int startNode) {

        visited = new boolean[totalNodes];

        path = new ArrayList<>();

        path.add(startNode);

        visited[startNode] = true;

        pathLength = 0;
    }

    // =========================
    // CURRENT NODE
    // =========================
    public int currentNode() {

        return path.get(path.size() - 1);
    }
}
