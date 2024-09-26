package searchEngine;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearchEngine extends AbstractSearchEngine {

    public BreadthFirstSearchEngine(int width, int height) {
        super(width, height);

        long inicioTotal = System.nanoTime();

        breadthFirstSearch();

        long tiempoTotal = System.nanoTime() - inicioTotal;
        System.out.println("Tiempo total de ejecución: " + tiempoTotal + " nanosegundos");
    }

    private void breadthFirstSearch() {
        Queue<Dimension> openList = new LinkedList<>();
        HashSet<Dimension> closedList = new HashSet<>();
        HashMap<Dimension, Dimension> parentMap = new HashMap<>();

        openList.add(startLoc); // Add start location to open list
        maze.setValue(startLoc.width, startLoc.height, (short) 1); // Mark start as depth 1

        while (!openList.isEmpty() && isSearching) {
            Dimension current = openList.poll(); // Remove current from open list
            closedList.add(current); // Add to closed list
            int depth = maze.getValue(current.width, current.height);

            if (equals(current, goalLoc)) { // If we reach the goal
                System.out.println("Found the goal at " + current.width + ", " + current.height);
                isSearching = false;
                reconstructPath(current, parentMap); // Reconstruct path from goal
                return;
            }

            Dimension[] moves = getPossibleMoves(current);
            for (Dimension move : moves) {
                if (move != null && !closedList.contains(move) && !openList.contains(move)) {
                    openList.add(move); // Add valid move to open list
                    parentMap.put(move, current); // Track parent of this node
                    maze.setValue(move.width, move.height, (short) (depth + 1)); // Set depth
                }
            }
        }
    }
}
