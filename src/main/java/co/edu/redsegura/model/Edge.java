package co.edu.redsegura.model;

import co.edu.redsegura.exception.InvalidDataException;
import java.util.Objects;

/** Conexión ponderada entre dos vértices. Inmutable: para cambiar el peso se crea otra instancia. */
public final class Edge {

    private final String id;
    private final String originId;
    private final String destinationId;
    private final EdgeType type;
    private final int weight;
    private final String label;

    public Edge(String id, String originId, String destinationId,
                EdgeType type, int weight, String label) {
        this.id = Validation.requireText(id, "El ID de la conexión");
        this.originId = Validation.requireText(originId, "El origen de la conexión " + this.id);
        this.destinationId = Validation.requireText(destinationId, "El destino de la conexión " + this.id);
        if (type == null) {
            throw new InvalidDataException("El tipo de la conexión " + this.id + " no puede ser nulo.");
        }
        if (weight <= 0) {
            throw new InvalidDataException("El peso de la conexión " + this.id
                    + " debe ser mayor que 0 (recibido: " + weight + ").");
        }
        if (this.originId.equals(this.destinationId)) {
            throw new InvalidDataException("La conexión " + this.id + " no puede unir un vértice consigo mismo.");
        }
        this.type = type;
        this.weight = weight;
        this.label = Validation.requireText(label, "La etiqueta de la conexión " + this.id);
    }

    public Edge withWeight(int newWeight) {
        return new Edge(id, originId, destinationId, type, newWeight, label);
    }

    public String getId() {
        return id;
    }

    public String getOriginId() {
        return originId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public EdgeType getType() {
        return type;
    }

    public int getWeight() {
        return weight;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Edge)) {
            return false;
        }
        return id.equals(((Edge) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String arrow = (type == EdgeType.DIRECTED) ? " -> " : " -- ";
        return id + ": " + originId + arrow + destinationId + " [" + weight + " min, " + label + "]";
    }
}
