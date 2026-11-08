package co.edu.redsegura.graph;

import co.edu.redsegura.exception.DuplicateException;
import co.edu.redsegura.exception.InvalidDataException;
import co.edu.redsegura.exception.NotFoundException;
import co.edu.redsegura.model.Edge;
import co.edu.redsegura.model.EdgeType;
import co.edu.redsegura.model.Vertex;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Grafo mixto (aristas no dirigidas y arcos dirigidos), etiquetado y ponderado.
 * Guarda internamente una lista de adyacencia para consultar vecinos en O(grado).
 * Los vértices y las conexiones conservan su orden de inserción.
 */
public class MixedGraph {

    private final Map<String, Vertex> vertices = new LinkedHashMap<>();
    private final Map<String, Edge> edges = new LinkedHashMap<>();
    private final Map<String, List<Edge>> adjacency = new LinkedHashMap<>();
    private final Set<String> blockedEdges = new HashSet<>();

    // ------------------------------------------------------------------ construcción

    public void addVertex(Vertex vertex) {
        if (vertex == null) {
            throw new InvalidDataException("El vértice no puede ser nulo.");
        }
        if (vertices.containsKey(vertex.getId())) {
            throw new DuplicateException("Ya existe un vértice con ID " + vertex.getId() + ".");
        }
        vertices.put(vertex.getId(), vertex);
        adjacency.put(vertex.getId(), new ArrayList<>());
    }

    public void addEdge(Edge edge) {
        if (edge == null) {
            throw new InvalidDataException("La conexión no puede ser nula.");
        }
        if (edges.containsKey(edge.getId())) {
            throw new DuplicateException("Ya existe una conexión con ID " + edge.getId() + ".");
        }
        requireVertex(edge.getOriginId());
        requireVertex(edge.getDestinationId());

        edges.put(edge.getId(), edge);
        adjacency.get(edge.getOriginId()).add(edge);
        if (edge.getType() == EdgeType.UNDIRECTED) {
            adjacency.get(edge.getDestinationId()).add(edge);
        }
    }

    /** Cambia el peso de una conexión existente (valida que el nuevo peso sea mayor que 0). */
    public void setEdgeWeight(String edgeId, int newWeight) {
        Edge updated = getEdge(edgeId).withWeight(newWeight); // lanza excepción si el peso es inválido
        edges.put(edgeId, updated); // conserva la posición en el LinkedHashMap
        for (List<Edge> list : adjacency.values()) {
            list.replaceAll(e -> e.getId().equals(edgeId) ? updated : e);
        }
    }

    // ------------------------------------------------------------------ bloqueos

    public void blockEdge(String edgeId) {
        getEdge(edgeId);
        blockedEdges.add(edgeId);
    }

    public void unblockEdge(String edgeId) {
        getEdge(edgeId);
        blockedEdges.remove(edgeId);
    }

    public boolean isBlocked(String edgeId) {
        return blockedEdges.contains(edgeId);
    }

    public void clearBlocks() {
        blockedEdges.clear();
    }

    // ------------------------------------------------------------------ consultas

    public boolean hasVertex(String id) {
        return id != null && vertices.containsKey(id);
    }

    public Vertex getVertex(String id) {
        requireVertex(id);
        return vertices.get(id);
    }

    public Edge getEdge(String id) {
        Edge edge = (id == null) ? null : edges.get(id);
        if (edge == null) {
            throw new NotFoundException("No existe la conexión con ID " + id + ".");
        }
        return edge;
    }

    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(new ArrayList<>(vertices.values()));
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(new ArrayList<>(edges.values()));
    }

    public int vertexCount() {
        return vertices.size();
    }

    public int edgeCount() {
        return edges.size();
    }

    /** Vecinos alcanzables respetando la orientación y omitiendo las conexiones bloqueadas. */
    public List<Neighbor> neighbors(String vertexId) {
        return neighbors(vertexId, false);
    }

    /** Vecinos alcanzables; si includeBlocked es true también se incluyen las conexiones bloqueadas. */
    public List<Neighbor> neighbors(String vertexId, boolean includeBlocked) {
        requireVertex(vertexId);
        List<Neighbor> result = new ArrayList<>();
        for (Edge edge : adjacency.get(vertexId)) {
            if (!includeBlocked && blockedEdges.contains(edge.getId())) {
                continue;
            }
            String other = edge.getOriginId().equals(vertexId)
                    ? edge.getDestinationId()
                    : edge.getOriginId();
            result.add(new Neighbor(other, edge));
        }
        return Collections.unmodifiableList(result);
    }

    private void requireVertex(String id) {
        if (!hasVertex(id)) {
            throw new NotFoundException("No existe el vértice con ID " + id + ".");
        }
    }
}
