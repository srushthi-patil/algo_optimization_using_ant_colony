# Ant Colony Optimization for Shortest Path (Robot Navigation)

## 📌 Overview

This project implements an **Ant Colony Optimization (ACO)** algorithm for finding the shortest path in a graph-based environment, inspired by natural ant foraging behavior. It is applied to robotic path planning for efficient navigation in unknown or weighted environments.

## 🚀 Features

* Graph-based environment representation
* Ant Colony Optimization (ACO) based path finding
* Pheromone update and evaporation mechanism
* Comparison with traditional algorithms (A*, Dijkstra)
* Iterative convergence to optimal/near-optimal path
* Java Swing GUI for visualization (if included in your project)

## 🧠 Algorithm Highlights

* Uses probabilistic decision-making based on pheromone intensity and heuristic distance
* Supports exploration and exploitation balance
* Iterative improvement of path quality
* Converges toward shortest path over multiple iterations

## 📊 Results

* Achieves efficient convergence to optimal paths
* Performs competitively against A* and Dijkstra in average computation time
* Demonstrates adaptability in dynamic environments

## 🛠️ Tech Stack

* Java
* Java Swing (GUI)
* Data Structures (Graphs, Matrices)

## 📁 Project Structure

```
/src
   ├── ACO.java
   ├── Graph.java
   ├── Ant.java
   ├── ACOGUI.java
   └── Main.java
```

## ▶️ How to Run

1. Clone the repository
2. Open in any Java IDE (IntelliJ / Eclipse / NetBeans)
3. Compile and run `Main.java`
4. Use GUI to visualize path generation

## 📚 Reference

Based on Ant Colony Optimization research applied to robotic shortest path planning in dynamic environments.

## 👨‍💻 Author

Project developed as part of academic implementation of bio-inspired optimization algorithms.
