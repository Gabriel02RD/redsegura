package co.edu.redsegura.graph;

import co.edu.redsegura.exception.NotFoundException;
import co.edu.redsegura.model.Edge;
import co.edu.redsegura.model.Vertex;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/** Búsqueda en profundidad (DFS recursivo), detección de ciclos y componentes débilmente conexas. */
public final class DepthFirstSearch {

    /**
     * @param order  vértices en el orden de visita
     * @param parent árbol DFS: vértice -> vértice desde el que se descubrió (la raíz no aparece)
     */
    public record Result(List<String> order, Map<String, String> parent) {
    }

    private DepthFirstSearch() {
    }

    public static Result run(MixedGraph graph, String start) {
        if (!graph.hasVertex(start)) {
            throw new NotFoundException("No existe el vértice de inicio " + start + ".");
        }
        List<String> order = new ArrayList<>();
        Map<String, String> parent = new LinkedHashMap<>();
        Set<String> visited = new HashSet<>();
        visit(graph, start, visited, order, parent);
        return new Result(order, parent);
    }

    private static void visit(MixedGraph graph, String current, Set<String> visited,
                              List<String> order, Map<String, String> parent) {
        visited.add(current);
        order.add(current);
        for (Neighbor n : graph.neighbors(current)) {
            if (!visited.contains(n.vertexId())) {
                parent.put(n.vertexId(), current);
                visit(graph, n.vertexId(), visited, order, parent);
            }
        }
    }

    // ------------------------------------------------------------------ ciclos

    /**
     * Indica si el grafo tiene al menos un ciclo, respetando la orientación:
     * un arco que llega a un vértice "en proceso" forma un ciclo dirigido, y una arista no dirigida
     * que llega a un vértice "en proceso" por una conexión distinta de la que se usó para entrar
     * forma un ciclo no dirigido.
     */
    public static boolean hasCycle(MixedGraph graph) {
        Map<String, Integer> state = new HashMap<>(); // ausente = sin visitar, 1 = en proceso, 2 = terminado
        for (Vertex v : graph.getVertices()) {
            if (!state.containsKey(v.getId())) {
                if (cycleFrom(graph, v.getId(), null, state)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean cycleFrom(MixedGraph graph, String current, String parentEdgeId,
                                     Map<String, Integer> state) {
        state.put(current, 1);
        for (Neighbor n : graph.neighbors(current)) {
            if (n.edge().getId().equals(parentEdgeId)) {
                continue; // no regresar por la misma conexión por la que se llegó
            }
            Integer s = state.get(n.vertexId());
            if (s == null) {
                if (cycleFrom(graph, n.vertexId(), n.edge().getId(), state)) {
                    return true;
                }
            } else if (s == 1) {
                return true;
            }
        }
        state.put(current, 2);
        return false;
    }

    // ------------------------------------------------------------------ componentes débiles

    /** Componentes débilmente conexas: se ignora la orientación y las conexiones bloqueadas. */
    public static List<List<String>> weakComponents(MixedGraph graph) {
        Map<String, List<String>> undirected = new LinkedHashMap<>();
        for (Vertex v : graph.getVertices()) {
            undirected.put(v.getId(), new ArrayList<>());
        }
        for (Edge e : graph.getEdges()) {
            if (graph.isBlocked(e.getId())) {
                continue;
            }
            undirected.get(e.getOriginId()).add(e.getDestinationId());
            undirected.get(e.getDestinationId()).add(e.getOriginId());
        }

        List<List<String>> components = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        for (Vertex v : graph.getVertices()) {
            if (visited.contains(v.getId())) {
                continue;
            }
            List<String> component = new ArrayList<>();
            Queue<String> queue = new ArrayDeque<>();
            visited.add(v.getId());
            queue.add(v.getId());
            while (!queue.isEmpty()) {
                String current = queue.poll();
                component.add(current);
                for (String next : undirected.get(current)) {
                    if (visited.add(next)) {
                        queue.add(next);
                    }
                }
            }
            components.add(component);
        }
        return components;
    }
}
