package ACOShortestPath;

import javax.swing.*;
import java.util.*;

public class ACO {

    private Graph graph;
    private ACOGUI gui;
    private GraphPanel panel;

    // =========================
    // PARAMETERS
    // =========================

    private int antsCount = 25;
    private int iterations = 180;

    private double alpha = 0.8;
    private double beta = 5.0;

    private double evaporation = 0.60;

    // =========================
    // TIME METRICS
    // =========================

    private long startTime;
    private long endTime;
    private long executionTime;

    private double averageIterationTime;

    private long obstacleRecoveryTime;

    // =========================
    // START RECOVERY TIMER
    // =========================

    public void startRecoveryTimer() {

        obstacleRecoveryTime =
                System.currentTimeMillis();
    }

    // =========================
    // STOP RECOVERY TIMER
    // =========================

    public void stopRecoveryTimer() {

        obstacleRecoveryTime =
                System.currentTimeMillis()
                        - obstacleRecoveryTime;
    }

    // =========================
    // EXPLORATION RATE
    // =========================

    // random exploration chance
    // prevents local optimum trapping
    private double explorationRate = 0.10;

    // =========================
    // PHEROMONE MATRIX
    // =========================

    private double[][] pheromone;

    // =========================
    // RANDOM GENERATOR
    // =========================

    private final Random rand =
            new Random();

    // =========================
    // GLOBAL BEST PATH
    // =========================

    private List<Integer> globalBestPath =
            new ArrayList<>();

    // =========================
    // GLOBAL BEST DISTANCE
    // =========================

    private double globalBestLength =
            Double.MAX_VALUE;
    private double idealDistance =
        Double.MAX_VALUE;       

    // =========================
    // PERFORMANCE METRICS
    // =========================

    private int convergenceIteration = -1;

    private int stableIterations = 0;

    private List<Integer> previousBestPath =
            null;

    // =========================
    // CONSTRUCTOR
    // =========================

    public ACO(
            Graph graph,
            ACOGUI gui,
            GraphPanel panel
    ) {

        this.graph = graph;
        this.gui = gui;
        this.panel = panel;

        // =====================
        // CREATE PHEROMONE MATRIX
        // =====================

        pheromone =
                new double[
                        graph.nodes
                        ][
                        graph.nodes
                        ];

        // =====================
        // INITIALIZE
        // =====================

        initializePheromone();

        // =====================
        // INITIAL GUI UPDATE
        // =====================

        panel.setPheromone(
                pheromone
        );

        panel.repaint();
    }

    // =========================
    // INITIAL PHEROMONE
    // =========================

    private void initializePheromone() {

        for (int i = 0;
             i < graph.nodes;
             i++) {

            for (int j = 0;
                 j < graph.nodes;
                 j++) {

                // edge exists
                if (graph.distance[i][j] > 0) {

                    // initial pheromone
                    pheromone[i][j] = 1.0;

                } else {

                    // no edge
                    pheromone[i][j] = 0.0;
                }
            }
        }
    }

    // =========================
    // RESET ACO
    // =========================

    public void reset() {

        // reinitialize pheromone
        initializePheromone();

        // reset best path
        globalBestPath =
                new ArrayList<>();

        globalBestLength =
                Double.MAX_VALUE;

        // reset convergence
        convergenceIteration = -1;

        stableIterations = 0;

        previousBestPath = null;

        // update panel
        panel.setPheromone(
                pheromone
        );

        panel.setBestPath(
                new ArrayList<>()
        );

        panel.repaint();
    }

    // =========================
    // NODE SELECTION
    // =========================

    private int selectNextNode(Ant ant) {

        int current =
                ant.currentNode();

        List<Integer> possible =
                new ArrayList<>();

        // =====================
        // FIND POSSIBLE NODES
        // =====================

        for (int i = 0;
             i < graph.nodes;
             i++) {

            if (
                    !ant.visited[i]
                            &&
                            graph.distance[current][i] > 0
            ) {

                possible.add(i);
            }
        }

        // no move possible
        if (possible.isEmpty()) {
            return -1;
        }

        // =====================
        // RANDOM EXPLORATION
        // =====================

        if (rand.nextDouble()
                < explorationRate) {

            return possible.get(
                    rand.nextInt(
                            possible.size()
                    )
            );
        }

        // =====================
        // PROBABILITY CALCULATION
        // =====================

        double[] probability =
                new double[graph.nodes];

        double total = 0;

        for (int next : possible) {

            // pheromone influence
            double tau =
                    Math.pow(
                            pheromone[current][next],
                            alpha
                    );

            // heuristic influence
            double eta =
                    Math.pow(
                            1.0 /
                                    graph.distance[current][next],
                            beta
                    );

            probability[next] =
                    tau * eta;

            total +=
                    probability[next];
        }

        // =====================
        // ROULETTE SELECTION
        // =====================

        double random =
                rand.nextDouble() * total;

        double cumulative = 0;

        for (int next : possible) {

            cumulative +=
                    probability[next];

            if (cumulative >= random) {

                return next;
            }
        }

        // fallback
        return possible.get(0);
    }

    // =========================
    // EVAPORATION
    // =========================

    private void evaporate() {

        for (int i = 0;
             i < graph.nodes;
             i++) {

            for (int j = 0;
                 j < graph.nodes;
                 j++) {

                if (graph.distance[i][j] > 0) {

                    pheromone[i][j] *=
                            (1.0 - evaporation);

                    pheromone[i][j] =
                            Math.max(
                                    0.05,
                                    pheromone[i][j]
                            );

                } else {

                    pheromone[i][j] = 0;
                }
            }
        }
    }

    // =========================
    // PHEROMONE UPDATE
    // =========================

    private void updatePheromone(
            List<Ant> ants,
            int destination
    ) {

        for (Ant ant : ants) {

            // only successful ants
            if (
                    ant.currentNode()
                            != destination
            ) {
                continue;
            }

            // avoid divide by zero
            if (ant.pathLength <= 0) {
                continue;
            }

            // stronger reward
            double contribution =
                    100.0 /
                            ant.pathLength;

            for (int i = 0;
                 i < ant.path.size() - 1;
                 i++) {

                int from =
                        ant.path.get(i);

                int to =
                        ant.path.get(i + 1);

                pheromone[from][to]
                        += contribution;

                pheromone[to][from]
                        += contribution;

                // upper limit
                pheromone[from][to] =
                        Math.min(
                                50,
                                pheromone[from][to]
                        );

                pheromone[to][from] =
                        Math.min(
                                50,
                                pheromone[to][from]
                        );
            }
        }
    }

    // =========================
    // PATH EFFICIENCY
    // =========================
    private double calculateEfficiency() {

        if(globalBestLength == Double.MAX_VALUE
            ||
            idealDistance == Double.MAX_VALUE) {

        return 0;
        }

        return
            (idealDistance
                    / globalBestLength)
                    * 100.0;
    }
    //dijkstra
    private double dijkstraShortestPath(
        int start,
        int end
    ) {

        double[] dist =
            new double[graph.nodes];

        boolean[] visited =
            new boolean[graph.nodes];

        Arrays.fill(
                dist,
                Double.MAX_VALUE
        );

        dist[start] = 0;

        for(int count = 0;
            count < graph.nodes - 1;
            count++) {

            int u = -1;

            double min =
                Double.MAX_VALUE;

            // find minimum distance node
            for(int i = 0;
                i < graph.nodes;
                i++) {

                if(
                    !visited[i]
                    &&
                    dist[i] < min
                ) {

                min = dist[i];
                u = i;
                }
            }

            if(u == -1)
                break;

            visited[u] = true;

            // update neighbors
            for(int v = 0;
                v < graph.nodes;
                v++) {

                if(
                    !visited[v]
                    &&
                    graph.distance[u][v] > 0
                    &&
                    dist[u]
                        + graph.distance[u][v]
                        < dist[v]
                ) {

                    dist[v] =
                        dist[u]
                        + graph.distance[u][v];
                }
            }
        }

       return dist[end];
    }
    // =========================
    // RUN ACO
    // =========================

    public void run(
            int start,
            int end
    ) {

        startTime =
                System.currentTimeMillis();
        // calculate optimal path using Dijkstra

        idealDistance =
                 dijkstraShortestPath(
                      start,
                      end
                 );    

        // reset
        globalBestLength =
                Double.MAX_VALUE;

        globalBestPath =
                new ArrayList<>();

        // reinitialize pheromone
        initializePheromone();

        // =====================
        // ITERATIONS
        // =====================

        for (int iteration = 0;
             iteration < iterations;
             iteration++) {

            List<Ant> ants =
                    new ArrayList<>();

            // =====================
            // CREATE ANTS
            // =====================

            for (int a = 0;
                 a < antsCount;
                 a++) {

                Ant ant =
                        new Ant(
                                graph.nodes,
                                start
                        );

                // safety limit
                int safety = 0;

                while (
                        ant.currentNode()
                                != end
                                &&
                                safety < graph.nodes * 2
                ) {

                    int next =
                            selectNextNode(ant);

                    if (next == -1) {
                        break;
                    }

                    ant.pathLength +=
                            graph.distance[
                                    ant.currentNode()
                                    ][next];

                    ant.path.add(next);

                    ant.visited[next] = true;

                    safety++;
                }

                ants.add(ant);

                // =================
                // GLOBAL BEST
                // =================

                if (
                        ant.currentNode()
                                == end
                                &&
                                ant.pathLength
                                        < globalBestLength
                ) {

                    globalBestLength =
                            ant.pathLength;

                    globalBestPath =
                            new ArrayList<>(
                                    ant.path
                            );

                    
                    if(convergenceIteration == -1) {

                        convergenceIteration =
                                iteration;
                    }
                    stableIterations = 0;

                    stopRecoveryTimer();
                }
            }

            // =====================
            // PHEROMONE UPDATE
            // =====================

            evaporate();

            updatePheromone(
                    ants,
                    end
            );

            // =====================
            // CHECK PATH STABILITY
            // =====================

            if (
                    previousBestPath != null
                            &&
                            previousBestPath.equals(
                                    globalBestPath
                            )
            ) {

                stableIterations++;

            } else {

                stableIterations = 0;

                if (globalBestPath != null) {

                    previousBestPath =
                            new ArrayList<>(
                                    globalBestPath
                            );
                }
            }

            // =====================
            // UPDATE GUI
            // =====================

            panel.setPheromone(
                    pheromone
            );

            panel.setBestPath(
                    globalBestPath
            );
            
            // =====================
            // LIVE TIME UPDATE
            // =====================

            endTime =
                  System.currentTimeMillis();

            executionTime =
                   endTime - startTime;

            averageIterationTime =
                   (double) executionTime
                      / (iteration + 1);

            SwingUtilities.invokeLater(() -> {

                panel.repaint();

                gui.outputArea.setText(

                        "\n\n====================\n"

                                + "ACO PERFORMANCE REPORT\n"

                                + "====================\n\n"

                                + "Best Path: "
                                + globalBestPath
                                + "\n"

                                + "Distance: "
                                + globalBestLength
                                + "\n"

                                + "Dijkstra Optimal Distance: "
                                + idealDistance
                                + "\n\n"
                                + "Convergence Iteration: "
                                + convergenceIteration
                                + "\n"

                                + "Stable Iterations: "
                                + stableIterations
                                + "\n\n"

                                + "Execution Time: "
                                + executionTime
                                + " ms\n"

                                + "Average Iteration Time: "
                                + String.format(
                                "%.3f",
                                averageIterationTime
                        )
                                + " ms\n"

                                + "Obstacle Recovery Time: "
                                + obstacleRecoveryTime
                                + " ms\n\n"

                                + "Path Efficiency: "
                                + String.format(
                                "%.2f",
                                calculateEfficiency()
                        )
                                + "%\n\n"

                                + "ACO Running...\n"
                );
            });

            // =====================
            // ANIMATION SPEED
            // =====================

            try {

                Thread.sleep(45);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        // =========================
        // FINAL MESSAGE
        // =========================

        SwingUtilities.invokeLater(() -> {

            endTime =
                    System.currentTimeMillis();

            executionTime =
                    endTime - startTime;

            averageIterationTime =
                    (double) executionTime
                            / iterations;

            gui.outputArea.append(
                    "\n\nACO Finished Successfully."
            );
        });
    }
}