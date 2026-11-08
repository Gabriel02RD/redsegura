package co.edu.redsegura.graph;

import co.edu.redsegura.model.Edge;
import co.edu.redsegura.model.EdgeType;
import co.edu.redsegura.model.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Representaciones del grafo: lista de adyacencia, matriz de adyacencia ponderada y matriz de incidencia.
 * Las tres usan el mismo orden: vértices en orden de inserción (filas) y conexiones en orden de inserción.
 * Se representa la estructura completa del grafo (los bloqueos temporales no se reflejan aquí).
 *
 * Convenciones:
 *  - Matriz de adyacencia: celda [i][j] = peso de la conexión i -> j; 0 significa "sin conexión".
 *    Una arista no dirigida llena [i][j] y [j][i]; un arco solo [origen][destino].
 *  - Matriz de incidencia: filas = vértices, columnas = conexiones.
 *    Arista no dirigida: +1 en ambos extremos. Arco: -1 en el origen y +1 en el destino.
 */
public class GraphRepresentations {

    private final MixedGraph graph;

    public GraphRepresentations(MixedGraph graph) {
        this.graph = Objects.requireNonNull(graph, "El grafo no puede ser nulo.");
    }

    public List<String> vertexOrder() {
        List<String> ids = new ArrayList<>();
        for (Vertex v : graph.getVertices()) {
            ids.add(v.getId());
        }
        return ids;
    }

    public List<String> edgeOrder() {
        List<String> ids = new ArrayList<>();
        for (Edge e : graph.getEdges()) {
            ids.add(e.getId());
        }
        return ids;
    }

    // ------------------------------------------------------------------ lista de adyacencia

    public Map<String, List<Neighbor>> adjacencyList() {
        Map<String, List<Neighbor>> result = new LinkedHashMap<>();
        for (Vertex v : graph.getVertices()) {
            result.put(v.getId(), graph.neighbors(v.getId(), true));
        }
        return result;
    }

    public String adjacencyListToString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<Neighbor>> entry : adjacencyList().entrySet()) {
            sb.append(String.format("%-4s", entry.getKey())).append("-> ");
            if (entry.getValue().isEmpty()) {
                sb.append("(sin salidas)");
            }
            for (Neighbor n : entry.getValue()) {
                sb.append('[').append(n.vertexId()).append(':').append(n.weight())
                  .append(" ").append(n.edge().getId()).append("] ");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------ matriz de adyacencia

    public int[][] adjacencyMatrix() {
        List<String> order = vertexOrder();
        Map<String, Integer> index = indexOf(order);
        int n = order.size();
        int[][] matrix = new int[n][n];
        for (Edge e : graph.getEdges()) {
            int i = index.get(e.getOriginId());
            int j = index.get(e.getDestinationId());
            putWeight(matrix, i, j, e.getWeight());
            if (e.getType() == EdgeType.UNDIRECTED) {
                putWeight(matrix, j, i, e.getWeight());
            }
        }
        return matrix;
    }

    private void putWeight(int[][] matrix, int i, int j, int weight) {
        if (matrix[i][j] == 0 || weight < matrix[i][j]) {
            matrix[i][j] = weight;
        }
    }

    public String adjacencyMatrixToString() {
        List<String> order = vertexOrder();
        return formatMatrix(adjacencyMatrix(), order, order, true);
    }

    // ------------------------------------------------------------------ matriz de incidencia

    public int[][] incidenceMatrix() {
        List<String> order = vertexOrder();
        Map<String, Integer> index = indexOf(order);
        List<Edge> edgeList = graph.getEdges();
        int[][] matrix = new int[order.size()][edgeList.size()];
        for (int k = 0; k < edgeList.size(); k++) {
            Edge e = edgeList.get(k);
            int i = index.get(e.getOriginId());
            int j = index.get(e.getDestinationId());
            if (e.getType() == EdgeType.DIRECTED) {
                matrix[i][k] = -1;
                matrix[j][k] = 1;
            } else {
                matrix[i][k] = 1;
                matrix[j][k] = 1;
            }
        }
        return matrix;
    }

    public String incidenceMatrixToString() {
        return formatMatrix(incidenceMatrix(), vertexOrder(), edgeOrder(), false);
    }

    // ------------------------------------------------------------------ utilidades

    private Map<String, Integer> indexOf(List<String> order) {
        Map<String, Integer> index = new HashMap<>();
        for (int i = 0; i < order.size(); i++) {
            index.put(order.get(i), i);
        }
        return index;
    }

    public static String formatMatrix(int[][] matrix, List<String> rowLabels,
                                      List<String> colLabels, boolean zeroAsDash) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s", ""));
        for (String col : colLabels) {
            sb.append(String.format("%5s", col));
        }
        sb.append(System.lineSeparator());
        for (int i = 0; i < matrix.length; i++) {
            sb.append(String.format("%-5s", rowLabels.get(i)));
            for (int j = 0; j < matrix[i].length; j++) {
                String cell = (zeroAsDash && matrix[i][j] == 0) ? "-" : String.valueOf(matrix[i][j]);
                sb.append(String.format("%5s", cell));
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
