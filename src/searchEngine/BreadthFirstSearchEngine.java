package searchEngine;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearchEngine extends AbstractSearchEngine {


    // Constructor que inicializa el laberinto y ejecuta el algoritmo BFS
    public BreadthFirstSearchEngine(int width, int height) {
        super(width, height);

        long inicioTotal = System.nanoTime();

        breadthFirstSearch();

        long tiempoTotal = System.nanoTime() - inicioTotal;
        System.out.println("Tiempo total de ejecución: " + tiempoTotal + " nanosegundos");
    }

    // Método que implementa la búsqueda en anchura (BFS)
    private void breadthFirstSearch() {
        Queue<Dimension> openList = new LinkedList<>();// Lista abierta: cola para explorar nodos
        HashSet<Dimension> closedList = new HashSet<>(); // Lista cerrada: nodos ya visitados
        HashMap<Dimension, Dimension> parentMap = new HashMap<>(); // Mapa de padres: rastrear el camino

        openList.add(startLoc); // Agrega la posición inicial a la lista abierta
        maze.setValue(startLoc.width, startLoc.height, (short) 1); // Marca la posición inicial con profundidad 1


        // Bucle principal de BFS: sigue ejecutándose mientras haya nodos por explorar y no se haya encontrado el objetivo
        while (!openList.isEmpty() && isSearching) {
            Dimension current = openList.poll(); // Extrae el nodo actual (el primero en la cola)
            closedList.add(current); // Marca el nodo actual como visitado
            int depth = maze.getValue(current.width, current.height);// Obtiene la profundidad del nodo actual


            // Si hemos encontrado el objetivo, termina la búsqueda
            if (equals(current, goalLoc)) { 
                System.out.println("Found the goal at " + current.width + ", " + current.height);
                isSearching = false;// Detiene la búsqueda
                reconstructPath(current, parentMap); // Reconstruct path from goal
                return; 
            } // Termina el método ya que se ha encontrado la solución


            // Obtiene los movimientos posibles desde la posición actual

            Dimension[] moves = getPossibleMoves(current);
            for (Dimension move : moves) {
                // Si el movimiento es válido y no ha sido visitado ni está en la lista abierta
                if (move != null && !closedList.contains(move) && !openList.contains(move)) {
                    openList.add(move); // Agrega el movimiento a la lista abierta (nodos por explorar)
                    parentMap.put(move, current); // Registra el nodo padre para reconstruir el camino
                    maze.setValue(move.width, move.height, (short) (depth + 1)); // Marca el nodo con su profundidad
                }
            }
        }
    }
}
