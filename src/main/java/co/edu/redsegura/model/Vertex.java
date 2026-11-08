package co.edu.redsegura.model;

import java.util.Objects;

/** Lugar de la red (universidad, hospital, terminal, etc.). Inmutable. */
public final class Vertex {

    private final String id;
    private final String label;
    private final String type;

    public Vertex(String id, String label, String type) {
        this.id = Validation.requireText(id, "El ID del vértice");
        this.label = Validation.requireText(label, "La etiqueta del vértice");
        this.type = Validation.requireText(type, "El tipo del vértice");
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vertex)) {
            return false;
        }
        return id.equals(((Vertex) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + " (" + label + ")";
    }
}
