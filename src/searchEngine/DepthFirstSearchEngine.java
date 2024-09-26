package searchEngine; /**
 * Title:        DepthFirstSearchEngine<p>
 * Description:  Performs a depth first search in a maze<p>
 * Copyright:    Copyright (c) Mark Watson, Released under Open Source Artistic License<p>
 * Company:      Mark Watson Associates<p>
 * @author Mark Watson
 * @version 1.0
 */

import java.awt.*;

public class DepthFirstSearchEngine extends AbstractSearchEngine {
    public DepthFirstSearchEngine(int width, int height) {
        super(width, height);
        long inicioTotal = System.nanoTime();

        depthFirstSearch(startLoc, 1);

        long tiempoTotal = System.nanoTime() - inicioTotal;
        System.out.println("Tiempo total de ejecución: " + tiempoTotal + " nanosegundos");
    }

    private void depthFirstSearch(Dimension loc, int depth) {
        if (isSearching == false) return;
        maze.setValue(loc.width, loc.height, (short)depth); // marca la profundidad de la busqueda
        Dimension [] moves = getPossibleMoves(loc); // obtener posibles movimientos desde esta ubicacion
        for (int i=0; i<4; i++) { // cuatro posibles movimientos
            if (moves[i] == null) break; // out of possible moves from this location
            searchPath[depth] = moves[i];  // agregar a la ruta de busqueda actual
            if (equals(moves[i], goalLoc)) { // si se encuentra la meta se imprime
                System.out.println("Found the goal at " + moves[i].width +
                                   ", " + moves[i].height);
                isSearching = false;
                maxDepth = depth;
                return;
            } else {
                depthFirstSearch(moves[i], depth + 1); // busqueda recursiva
                if (isSearching == false) return; // si se encontro la meta se termina la busqueda
            }
        }
        return; // no more moves from this location
    }
}
