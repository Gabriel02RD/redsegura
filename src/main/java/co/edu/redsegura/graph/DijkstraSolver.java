package co.edu.redsegura.graph;

import co.edu.redsegura.exception.NotFoundException;
import co.edu.redsegura.model.Vertex;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Rutas mínimas con Dijkstra (PriorityQueue + relajación + mapa de predecesores).
 * Requiere pesos positivos, condición que ya garantiza el constructor de Edge.
 */
public final class DijkstraSolver {

    public static final int INFINITY = Integer.MAX_VALUE;

    /** Distancias y predecesores desde un origen. */
    public record Solution(String source, Map<String, Integer> distances, Map<String, String> predecessors) {

        public boolean isReachable(String target) {
            Integer d = distances.get(target);
            return d != null && d != INFINITY;
        }

        /** Camino desde el origen hasta target (lista vacía si no es alcanzable). */
        public List<String> pathTo(String target) {
            if (!isReachable(target)) {
                return List.of();
            }
            List<String> path = new ArrayList<>();
            String current = target;
            while (current != null) {
                path.add(current);
                current = predecessors.get(current);
            }
            Collections.reverse(path);
            return path;
        }
    }

    /** Resultado resumido de una ruta origen -> destino. */
    public record PathResult(String source, String target, boolean reachable, int totalMinutes, List<String> path) {
    }

    private record QueueEntry(String vertexId, int distance) {
    }

    private DijkstraSolver() {
    }

    public static Solution solve(MixedGraph graph, String source) {
        if (!graph.hasVertex(source)) {
            throw new NotFoundException("No existe el vértice de origen " + source + ".");
        }
        Map<String, Integer> dist = new LinkedHashMap<>();
        Map<String, String> prev = new HashMap<>();
        for (Vertex v : graph.getVertices()) {
            dist.put(v.getId(), INFINITY);
        }
        dist.put(source, 0);

        PriorityQueue<QueueEntry> queue = new PriorityQueue<>(Comparator.comparingInt(QueueEntry::distance));
        queue.add(new QueueEntry(source, 0));

        while (!queue.isEmpty()) {
            QueueEntry current = queue.poll();
            if (current.distance() > dist.get(current.vertexId())) {
                continue; // entrada desactualizada
            }
            for (Neighbor n : graph.neighbors(current.vertexId())) {
                int candidate = current.distance() + n.weight();
                if (candidate < dist.get(n.vertexId())) { // relajación con "<" estricto
                    dist.put(n.vertexId(), candidate);
                    prev.put(n.vertexId(), current.vertexId());
                    queue.add(new QueueEntry(n.vertexId(), candidate));
                }
            }
        }
        return new Solution(source, dist, prev);
    }

    public static PathResult shortestPath(MixedGraph graph, String source, String target) {
        if (!graph.hasVertex(target)) {
            throw new NotFoundException("No existe el vértice de destino " + target + ".");
        }
        Solution solution = solve(graph, source);
        if (!solution.isReachable(target)) {
            return new PathResult(source, target, false, -1, List.of());
        }
        return new PathResult(source, target, true, solution.distances().get(target), solution.pathTo(target));
    }
}
