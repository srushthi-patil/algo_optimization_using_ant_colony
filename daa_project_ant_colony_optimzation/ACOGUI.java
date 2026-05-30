package ACOShortestPath;

import javax.swing.*;
import java.awt.*;

public class ACOGUI extends JFrame {

    // =========================
    // UI COMPONENTS
    // =========================

    JTextArea outputArea;

    JButton runButton;

    JButton clearButton;

    JButton obstacleButton;

    JTextField startField;

    JTextField endField;

    // =========================
    // ACO + GRAPH
    // =========================

    ACO aco;

    Graph graph;

    GraphPanel graphPanel;

    // =========================
    // CREATE GRAPH
    // =========================

    private void initGraph() {

        graph = new Graph(12);

        // =====================
        // EDGES
        // =====================

        graph.addEdge(0,1,4);
graph.addEdge(0,2,2);

graph.addEdge(1,2,1);
graph.addEdge(1,3,5);

graph.addEdge(2,3,8);
graph.addEdge(2,4,10);

graph.addEdge(3,4,2);
graph.addEdge(3,5,6);

graph.addEdge(4,5,3);
graph.addEdge(4,6,7);

graph.addEdge(5,6,1);
graph.addEdge(5,7,8);

graph.addEdge(6,7,2);
graph.addEdge(6,8,6);

graph.addEdge(7,8,3);
graph.addEdge(7,9,5);

graph.addEdge(8,9,2);
graph.addEdge(8,10,7);

graph.addEdge(9,10,1);
graph.addEdge(9,11,4);

graph.addEdge(10,11,3);

        // =====================
        // NODE POSITIONS
        // =====================

        graph.setPosition(0,100,100);

graph.setPosition(1,220,80);

graph.setPosition(2,180,200);

graph.setPosition(3,320,140);

graph.setPosition(4,420,220);

graph.setPosition(5,520,140);

graph.setPosition(6,620,240);

graph.setPosition(7,740,180);

graph.setPosition(8,820,300);

graph.setPosition(9,940,220);

graph.setPosition(10,1050,140);

graph.setPosition(11,1160,260);
    }

    // =========================
    // RUN ACO
    // =========================

    private void runACO() {

        try {

            int start =
                    Integer.parseInt(
                            startField.getText()
                    );

            int end =
                    Integer.parseInt(
                            endField.getText()
                    );

            // validation
            if(start < 0 || start >= graph.nodes
                    ||
                    end < 0 || end >= graph.nodes) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid node index!"
                );

                return;
            }

            // clear old text
            outputArea.setText("");

            outputArea.append(
                    "Running ACO Algorithm...\n\n"
            );

            // disable button
            runButton.setEnabled(false);

            // run in background
            new Thread(() -> {

                aco.run(start,end);

                SwingUtilities.invokeLater(() -> {

                    runButton.setEnabled(true);

                });

            }).start();

        }
        catch(Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid integers!"
            );
        }
    }

    // =========================
    // CONSTRUCTOR
    // =========================

    public ACOGUI() {

        // =====================
        // FRAME
        // =====================

        setTitle("ACO Heatmap Simulator");

        setSize(1200,800);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLayout(new BorderLayout());

        // =====================
        // TOP PANEL
        // =====================

        JPanel topPanel = new JPanel();

        topPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        topPanel.setBackground(
                new Color(245,245,245)
        );

        // =====================
        // LABELS
        // =====================

        JLabel startLabel =
                new JLabel("Start:");

        startLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        JLabel endLabel =
                new JLabel("End:");

        endLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        // =====================
        // TEXTFIELDS
        // =====================

        startField =
                new JTextField("1",5);

        startField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        18
                )
        );

        endField =
                new JTextField("5",5);

        endField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        18
                )
        );

        // =====================
        // BUTTONS
        // =====================

        runButton =
                new JButton("RUN ACO");

        runButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        clearButton =
                new JButton("Clear");

        clearButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        obstacleButton =
                new JButton("Add Obstacle");

        obstacleButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        // =====================
        // ADD TO TOP PANEL
        // =====================

        topPanel.add(obstacleButton);

        topPanel.add(
                Box.createHorizontalStrut(20)
        );

        topPanel.add(startLabel);

        topPanel.add(startField);

        topPanel.add(
                Box.createHorizontalStrut(20)
        );

        topPanel.add(endLabel);

        topPanel.add(endField);

        topPanel.add(
                Box.createHorizontalStrut(20)
        );

        topPanel.add(runButton);

        topPanel.add(
                Box.createHorizontalStrut(10)
        );

        topPanel.add(clearButton);

        add(topPanel, BorderLayout.NORTH);

        // =====================
        // OUTPUT AREA
        // =====================

        outputArea =
                new JTextArea();

        outputArea.setEditable(false);

        outputArea.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        18
                )
        );

        outputArea.setMargin(
                new Insets(
                        10,
                        10,
                        10,
                        10
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        outputArea
                );

        scrollPane.setPreferredSize(
                new Dimension(
                        1200,
                        180
                )
        );

        add(scrollPane, BorderLayout.SOUTH);

        // =====================
        // GRAPH
        // =====================

        initGraph();

        graphPanel =
                new GraphPanel(graph);

        add(graphPanel, BorderLayout.CENTER);

        // =====================
        // ACO
        // =====================

        aco =
                new ACO(
                        graph,
                        this,
                        graphPanel
                );

        // =====================
        // RUN BUTTON
        // =====================

        runButton.addActionListener(
                e -> runACO()
        );

        // =====================
        // CLEAR BUTTON
        // =====================

        clearButton.addActionListener(e -> {

            outputArea.setText("");

            // restore graph
            graph.resetGraph();

            // reset aco
            aco.reset();

            // clear best path
            graphPanel.setBestPath(null);

            // repaint
            graphPanel.repaint();

            outputArea.append(
                    "Graph Reset Successfully.\n"
            );
        });

        // =====================
        // OBSTACLE BUTTON
        // =====================

        obstacleButton.addActionListener(e -> {

            JPanel panel =
                    new JPanel(
                            new GridLayout(2,2)
                    );

            JTextField fromField =
                    new JTextField();

            JTextField toField =
                    new JTextField();

            panel.add(
                    new JLabel("From Vertex:")
            );

            panel.add(fromField);

            panel.add(
                    new JLabel("To Vertex:")
            );

            panel.add(toField);

            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            panel,
                            "Add Obstacle",
                            JOptionPane.OK_CANCEL_OPTION
                    );

            if(result
                    ==
                    JOptionPane.OK_OPTION) {

                try {

                    int from =
                            Integer.parseInt(
                                    fromField.getText()
                            );

                    int to =
                            Integer.parseInt(
                                    toField.getText()
                            );

                    graph.removeEdge(from,to);

                    aco.startRecoveryTimer();

                    outputArea.append(
                            "\nObstacle Added Between "
                            + from
                            + " and "
                            + to
                            + "\n"
                    );

                    graphPanel.repaint();

                }
                catch(Exception ex) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid Input"
                    );
                }
            }
        });

        // =====================
        // SHOW
        // =====================

        setVisible(true);
    }

    // =========================
    // MAIN
    // =========================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                ACOGUI::new
        );
    }
}