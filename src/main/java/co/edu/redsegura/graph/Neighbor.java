package co.edu.redsegura.graph;

import co.edu.redsegura.model.Edge;

/**
 * Vecino alcanzable desde un vértice, junto con la conexión usada para llegar a él.
 *
 * @param vertexId ID del vértice vecino
 * @param edge conexión que lo une
 */
public record Neighbor(String vertexId, Edge edge) {

    public int weight() {
        return edge.getWeight();
    }
}
