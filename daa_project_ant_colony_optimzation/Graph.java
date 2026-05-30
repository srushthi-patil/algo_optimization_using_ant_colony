package ACOShortestPath;

import java.awt.*;

public class Graph {

    // =========================
    // TOTAL NODES
    // =========================
    int nodes;

    // =========================
    // DISTANCE MATRIX
    // =========================
    double[][] distance;

    // =========================
    // NODE POSITIONS
    // =========================
    Point[] positions;

    // =========================
    // CONSTRUCTOR
    // =========================
    public Graph(int nodes) {

        this.nodes = nodes;

        distance = new double[nodes][nodes];

        positions = new Point[nodes];
    }

    // =========================
    // ADD EDGE
    // =========================
    public void addEdge(
            int from,
            int to,
            double weight
    ) {

        distance[from][to] = weight;

        distance[to][from] = weight;
    }
    // =========================
    // REMOVE EDGE
    // =========================

    public void removeEdge(
            int from,
            int to
    ) {

        distance[from][to] = 0;
 
        distance[to][from] = 0;
    }
    // =========================
    // SET NODE POSITION
    // =========================
    public void setPosition(
            int node,
            int x,
            int y
    ) {

        positions[node] = new Point(x, y);
    }
    // =========================
// RESET DEFAULT GRAPH
// =========================

public void resetGraph() {

    // clear matrix
    for(int i=0;i<nodes;i++) {

        for(int j=0;j<nodes;j++) {

            distance[i][j] = 0;
        }
    }

    // restore edges
    addEdge(0,1,4);
    addEdge(0,2,2);

    addEdge(1,2,1);
    addEdge(1,3,5);

    addEdge(2,3,8);
    addEdge(2,4,10);

    addEdge(3,4,2);
    addEdge(3,5,6);

    addEdge(4,5,3);
    addEdge(4,6,7);

    addEdge(5,6,1);
    addEdge(5,7,8);

    addEdge(6,7,2);
    addEdge(6,8,6);

    addEdge(7,8,3);
    addEdge(7,9,5);

    addEdge(8,9,2);
    addEdge(8,10,7);

    addEdge(9,10,1);
    addEdge(9,11,4);

    addEdge(10,11,3);
}
}
