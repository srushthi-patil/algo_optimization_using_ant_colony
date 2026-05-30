package ACOShortestPath;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphPanel extends JPanel {

    // =========================
    // GRAPH
    // =========================
    private Graph graph;

    // =========================
    // PHEROMONE MATRIX
    // =========================
    private double[][] pheromone;

    // =========================
    // BEST PATH
    // =========================
    private List<Integer> bestPath =
            new ArrayList<>();

    // =========================
    // CONSTRUCTOR
    // =========================
    public GraphPanel(Graph graph) {

        this.graph = graph;

        setBackground(new Color(245,245,245));

        setDoubleBuffered(true);
    }

    // =========================
    // SET PHEROMONE
    // =========================
    public void setPheromone(
            double[][] pheromone
    ) {

        this.pheromone = pheromone;

        repaint();
    }

    // =========================
    // SET BEST PATH
    // =========================
    public void setBestPath(
            List<Integer> path
    ) {

        if(path == null)
            return;

        this.bestPath =
                new ArrayList<>(path);

        repaint();
    }

    // =========================
    // PAINT
    // =========================
    @Override
    protected void paintComponent(
            Graphics g
    ) {

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g;
            
                
        // smooth rendering
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        drawEdges(g2);

        drawNodes(g2);

        drawLegend(g2);
    }

    // =========================
    // DRAW EDGES
    // =========================
    private void drawEdges(
            Graphics2D g2
    ) {

        double maxPheromone =
                getMaxPheromone();

        for(int i=0;
            i<graph.nodes;
            i++) {

            for(int j=i+1;
                j<graph.nodes;
                j++) {

                // =====================
                // BLOCKED / REMOVED EDGE
                // =====================

                // no edge
                if(graph.distance[i][j] <= 0)
                 continue;

                   Point p1 =
                        graph.positions[i];

                    Point p2 =
                        graph.positions[j];

                      // =====================
                      // PHEROMONE LEVEL
                      // =====================

                      double pheromoneLevel = 0.1;

                       if(pheromone != null) {

                           pheromoneLevel =
                                pheromone[i][j];
                       }

                       // =====================
                       // NORMALIZE
                       // =====================

                       float intensity =
                          (float)(
                                pheromoneLevel
                                        /
                                maxPheromone
                         );

                         intensity =
                              Math.max(
                                0.05f,
                                intensity
                          );

                         intensity =
                               Math.min(
                                1f,
                                intensity
                           );

                // =====================
                // EDGE THICKNESS
                // =====================

                float thickness =
                        2f + intensity * 10f;

                // =====================
                // HEATMAP COLOR
                // =====================

                Color heatColor =
                        getHeatColor(
                                intensity
                        );

                // =====================
                // DRAW PHEROMONE EDGE
                // =====================

                g2.setColor(heatColor);

                g2.setStroke(
                        new BasicStroke(
                                thickness,
                                BasicStroke.CAP_ROUND,
                                BasicStroke.JOIN_ROUND
                        )
                );

                g2.drawLine(
                        p1.x,
                        p1.y,
                        p2.x,
                        p2.y
                );

                // =====================
                // BEST PATH
                // ONLY AFTER
                // STRONG CONVERGENCE
                // =====================

                boolean isBestEdge =
                        isPathEdge(i,j);

                if(
                        isBestEdge
                        &&
                        pheromoneLevel > 8
                ) {

                    // glow
                    g2.setColor(
                            new Color(
                                    0,
                                    255,
                                    0,
                                    70
                            )
                    );

                    g2.setStroke(
                            new BasicStroke(
                                    18,
                                    BasicStroke.CAP_ROUND,
                                    BasicStroke.JOIN_ROUND
                            )
                    );

                    g2.drawLine(
                            p1.x,
                            p1.y,
                            p2.x,
                            p2.y
                    );

                    // main line
                    g2.setColor(
                            new Color(
                                    0,
                                    230,
                                    0
                            )
                    );

                    g2.setStroke(
                            new BasicStroke(
                                    7,
                                    BasicStroke.CAP_ROUND,
                                    BasicStroke.JOIN_ROUND
                            )
                    );

                    g2.drawLine(
                            p1.x,
                            p1.y,
                            p2.x,
                            p2.y
                    );
                }

                // =====================
                // EDGE WEIGHT LABEL
                // =====================

                int midX =
                        (p1.x + p2.x)/2;

                int midY =
                        (p1.y + p2.y)/2;

                // bg
                g2.setColor(
                        new Color(
                                255,
                                255,
                                255,
                                230
                        )
                );

                g2.fillRoundRect(
                        midX - 12,
                        midY - 10,
                        26,
                        20,
                        10,
                        10
                );

                // text
                g2.setColor(Color.BLACK);

                g2.setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                12
                        )
                );

                g2.drawString(
                        String.valueOf(
                                (int)
                                        graph.distance[i][j]
                        ),
                        midX - 4,
                        midY + 5
                );
            }
        }
    }

    // =========================
    // DRAW NODES
    // =========================
    private void drawNodes(
            Graphics2D g2
    ) {

        for(int i=0;
            i<graph.nodes;
            i++) {

            Point p =
                    graph.positions[i];

            // shadow
            g2.setColor(
                    new Color(
                            0,
                            0,
                            0,
                            40
                    )
            );

            g2.fillOval(
                    p.x - 24,
                    p.y - 24,
                    48,
                    48
            );

            // node
            g2.setColor(
                    new Color(
                            52,
                            152,
                            219
                    )
            );

            g2.fillOval(
                    p.x - 22,
                    p.y - 22,
                    44,
                    44
            );

            // border
            g2.setColor(Color.WHITE);

            g2.setStroke(
                    new BasicStroke(2)
            );

            g2.drawOval(
                    p.x - 22,
                    p.y - 22,
                    44,
                    44
            );

            // text
            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            16
                    )
            );

            g2.drawString(
                    String.valueOf(i),
                    p.x - 5,
                    p.y + 5
            );
        }
    }

    // =========================
    // CHECK BEST EDGE
    // =========================
    private boolean isPathEdge(
            int a,
            int b
    ) {

        if(bestPath == null)
            return false;

        for(int i=0;
            i<bestPath.size()-1;
            i++) {

            int from =
                    bestPath.get(i);

            int to =
                    bestPath.get(i+1);

            if(
                    (from == a && to == b)
                    ||
                    (from == b && to == a)
            ) {

                return true;
            }
        }

        return false;
    }

    // =========================
    // MAX PHEROMONE
    // =========================
    private double getMaxPheromone() {

        double max = 1;

        if(pheromone == null)
            return max;

        for(int i=0;
            i<graph.nodes;
            i++) {

            for(int j=0;
                j<graph.nodes;
                j++) {

                max = Math.max(
                        max,
                        pheromone[i][j]
                );
            }
        }

        return max;
    }

    // =========================
    // HEATMAP COLOR
    // =========================
    private Color getHeatColor(
            float intensity
    ) {

        /*
            LOW  -> DARK RED
            MID  -> ORANGE
            HIGH -> YELLOW
         */

        intensity =
                Math.max(
                        0f,
                        Math.min(1f,intensity)
                );

        int red = 255;

        int green =
                (int)(220 * intensity);

        int blue =
                (int)(40 * (1-intensity));

        return new Color(
                red,
                green,
                blue
        );
    }

    // =========================
    // LEGEND
    // =========================
    private void drawLegend(
            Graphics2D g2
    ) {

        int x = 20;

        int y =
                getHeight() - 100;

        g2.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        // low
        g2.setColor(
                new Color(
                        180,
                        0,
                        0
                )
        );

        g2.fillRect(x,y,25,12);

        g2.setColor(Color.BLACK);

        g2.drawString(
                "Weak Path",
                x+35,
                y+12
        );

        // medium
        y += 25;

        g2.setColor(
                new Color(
                        255,
                        120,
                        0
                )
        );

        g2.fillRect(x,y,25,12);

        g2.setColor(Color.BLACK);

        g2.drawString(
                "Medium Pheromone",
                x+35,
                y+12
        );

        // high
        y += 25;

        g2.setColor(
                new Color(
                        255,
                        220,
                        0
                )
        );

        g2.fillRect(x,y,25,12);

        g2.setColor(Color.BLACK);

        g2.drawString(
                "Strong Path",
                x+35,
                y+12
        );

        // best
        y += 25;

        g2.setColor(
                new Color(
                        0,
                        220,
                        0
                )
        );

        g2.fillRect(x,y,25,12);

        g2.setColor(Color.BLACK);

        g2.drawString(
                "Best Path",
                x+35,
                y+12
        );
    }
}