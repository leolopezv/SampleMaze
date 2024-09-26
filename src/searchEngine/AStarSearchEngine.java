package searchEngine;

import maze1.Maze;

import java.awt.*;
import java.util.*;

public class AStarSearchEngine extends AbstractSearchEngine {

    public AStarSearchEngine(int width, int height) {
        super(width, height);
        aStarSearch();
    }

    // Función heurística: Usando la distancia de Manhattan para este ejemplo
    // Heuristica significa que es una estimación de la distancia desde el nodo actual hasta el nodo objetivo
    // h(n) = |x1 - x2| + |y1 - y2|
    private int heuristic(Dimension loc1, Dimension loc2) {
        return Math.abs(loc1.width - loc2.width) + Math.abs(loc1.height - loc2.height);
    }

    private void aStarSearch() {
        
        // Cola de prioridad para la lista abierta, ordenada por f(n) = g(n) + h(n)
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.fCost));
        HashSet<Dimension> closedList = new HashSet<>();  // Lista cerrada para rastrear nodos visitados
        HashMap<Dimension, Dimension> parentMap = new HashMap<>();  // Para reconstruir el camino

        //TIEMPO: 
        long inicioSelection = System.nanoTime();

        Node startNode = new Node(startLoc, 0, heuristic(startLoc, goalLoc));
        openList.add(startNode);  // Agregar el nodo inicial a la lista abierta

        while (!openList.isEmpty() && isSearching) {
            Node current = openList.poll();  // Obtener el nodo con el menor fCost
            Dimension currentLoc = current.location;

            if (equals(currentLoc, goalLoc)) {  // Si llegamos a la meta
                System.out.println("Encontrado el objetivo en " + currentLoc.width + ", " + currentLoc.height);
                isSearching = false;
                reconstructPath(currentLoc, parentMap);  // Reconstruir el camino desde la meta
                return;
            }

            closedList.add(currentLoc);  // Agregar la ubicación actual a la lista cerrada

            Dimension[] moves = getPossibleMoves(currentLoc);
            for (Dimension move : moves) {
                if (move != null && !closedList.contains(move)) {
                    int gCost = current.gCost + 1;  // g(n): costo desde el inicio hasta este movimiento
                    int hCost = heuristic(move, goalLoc);  // h(n): costo estimado hasta la meta
                    int fCost = gCost + hCost;  // f(n) = g(n) + h(n)

                    Node neighbor = new Node(move, gCost, hCost);

                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);  // Agregar vecino a la lista abierta si no está ya allí
                        parentMap.put(move, currentLoc);  // Rastrear el padre para la reconstrucción del camino
                        maze.setValue(move.width, move.height, (short) (gCost + 1));  // Marcar el movimiento con la profundidad
                    } else if (gCost < neighbor.gCost) {
                        // Si encontramos un mejor camino a este vecino, actualizarlo
                        parentMap.put(move, currentLoc);
                        neighbor.gCost = gCost;
                        neighbor.fCost = fCost;
                    }
                }
            }
        }
        //FIN TIEMPO
        long tiempoEstimado = System.nanoTime() - inicioSelection;
        System.out.println("Tiempo total del Arreglo1: " + tiempoEstimado + " nanosegundos");
    }

    @Override
    protected Dimension[] getPossibleMoves(Dimension loc) {
        Dimension[] tempMoves = new Dimension[8];
        int x = loc.width;
        int y = loc.height;
        int num = 0;

        // Vertical y horizontal
        if (maze.getValue(x - 1, y) == 0 || maze.getValue(x - 1, y) == Maze.GOAL_LOC_VALUE) {
            tempMoves[num++] = new Dimension(x - 1, y);
        }
        if (maze.getValue(x + 1, y) == 0 || maze.getValue(x + 1, y) == Maze.GOAL_LOC_VALUE) {
            tempMoves[num++] = new Dimension(x + 1, y);
        }
        if (maze.getValue(x, y - 1) == 0 || maze.getValue(x, y - 1) == Maze.GOAL_LOC_VALUE) {
            tempMoves[num++] = new Dimension(x, y - 1);
        }
        if (maze.getValue(x, y + 1) == 0 || maze.getValue(x, y + 1) == Maze.GOAL_LOC_VALUE) {
            tempMoves[num++] = new Dimension(x, y + 1);
        }

        // Esta diagonal es hacia arriba a la izquierda
        if ((maze.getValue(x - 1, y - 1) == 0 || maze.getValue(x - 1, y - 1) == Maze.GOAL_LOC_VALUE) &&
                maze.getValue(x - 1, y) != Maze.OBSTICLE && maze.getValue(x, y - 1) != Maze.OBSTICLE) {
            tempMoves[num++] = new Dimension(x - 1, y - 1);
        }
        // Esta diagonal es hacia arriba a la derecha
        if ((maze.getValue(x + 1, y - 1) == 0 || maze.getValue(x + 1, y - 1) == Maze.GOAL_LOC_VALUE) &&
                maze.getValue(x + 1, y) != Maze.OBSTICLE && maze.getValue(x, y - 1) != Maze.OBSTICLE) {
            tempMoves[num++] = new Dimension(x + 1, y - 1);
        }
        // Esta diagonal es hacia abajo a la izquierda
        if ((maze.getValue(x - 1, y + 1) == 0 || maze.getValue(x - 1, y + 1) == Maze.GOAL_LOC_VALUE) &&
                maze.getValue(x - 1, y) != Maze.OBSTICLE && maze.getValue(x, y + 1) != Maze.OBSTICLE) {
            tempMoves[num++] = new Dimension(x - 1, y + 1);
        }
        // Esta diagonal es hacia abajo a la derecha
        if ((maze.getValue(x + 1, y + 1) == 0 || maze.getValue(x + 1, y + 1) == Maze.GOAL_LOC_VALUE) &&
                maze.getValue(x + 1, y) != Maze.OBSTICLE && maze.getValue(x, y + 1) != Maze.OBSTICLE) {
            tempMoves[num++] = new Dimension(x + 1, y + 1);
        }

        return Arrays.copyOf(tempMoves, num);
    }

    // Clase Node para almacenar ubicación, gCost (distancia desde el inicio) y hCost (heurística hasta la meta)
    private static class Node {
        Dimension location;
        int gCost;  // Distancia desde el inicio
        int hCost;  // Estimación heurística hasta la meta
        int fCost;  // Costo total f(n) = g(n) + h(n)

        public Node(Dimension location, int gCost, int hCost) {
            this.location = location;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return location.equals(node.location);
        }

        @Override
        public int hashCode() {
            return location.hashCode();
        }
    }
}