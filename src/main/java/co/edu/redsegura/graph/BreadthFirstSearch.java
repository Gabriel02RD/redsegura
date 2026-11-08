package co.edu.redsegura.graph;

import co.edu.redsegura.exception.NotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/** Búsqueda por anchura (BFS) con cola y conjunto de visitados. Respeta la orientación. */
public final class BreadthFirstSearch {

    /**
     * @param order  vértices en el orden en que fueron visitados
     * @param parent árbol BFS: vértice -> vértice desde el que se descubrió (la raíz no aparece)
     * @param level  nivel (distancia en número de conexiones) de cada vértice alcanzado
     */
    public record Result(List<String> order, Map<String, String> parent, Map<String, Integer> level) {
    }

    private BreadthFirstSearch() {
    }

    public static Result run(MixedGraph graph, String start) {
        if (!graph.hasVertex(start)) {
            throw new NotFoundException("No existe el vértice de inicio " + start + ".");
        }
        List<String> order = new ArrayList<>();
        Map<String, String> parent = new LinkedHashMap<>();
        Map<String, Integer> level = new LinkedHashMap<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new ArrayDeque<>();

        visited.add(start); // se marca al ENCOLAR para no encolar un vértice dos veces
        level.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            order.add(current);
            for (Neighbor n : graph.neighbors(current)) {
                if (visited.add(n.vertexId())) {
                    parent.put(n.vertexId(), current);
                    level.put(n.vertexId(), level.get(current) + 1);
                    queue.add(n.vertexId());
                }
            }
        }
        return new Result(order, parent, level);
    }
}
