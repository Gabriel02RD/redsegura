package co.edu.redsegura.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.edu.redsegura.exception.InvalidDataException;
import org.junit.jupiter.api.Test;

class ModelTest {

    @Test
    void vertexValidoConservaSusDatos() {
        Vertex v = new Vertex("S01", "UAJS Sede C", "Universidad");
        assertEquals("S01", v.getId());
        assertEquals("UAJS Sede C", v.getLabel());
        assertEquals("Universidad", v.getType());
    }

    @Test
    void vertexConIdNuloOVacioSeRechaza() {
        assertThrows(InvalidDataException.class, () -> new Vertex(null, "A", "T"));
        assertThrows(InvalidDataException.class, () -> new Vertex("  ", "A", "T"));
        assertThrows(InvalidDataException.class, () -> new Vertex("S01", "", "T"));
    }

    @Test
    void edgeValidoConservaSusDatos() {
        Edge e = new Edge("R01", "S01", "S07", EdgeType.DIRECTED, 4, "Corredor");
        assertEquals(4, e.getWeight());
        assertEquals(EdgeType.DIRECTED, e.getType());
        assertEquals("S01", e.getOriginId());
        assertEquals("S07", e.getDestinationId());
    }

    @Test
    void edgeConPesoInvalidoSeRechaza() {
        assertThrows(InvalidDataException.class, () -> new Edge("R1", "A", "B", EdgeType.DIRECTED, 0, "x"));
        assertThrows(InvalidDataException.class, () -> new Edge("R1", "A", "B", EdgeType.DIRECTED, -3, "x"));
    }

    @Test
    void edgeConDatosNulosOBucleSeRechaza() {
        assertThrows(InvalidDataException.class, () -> new Edge(null, "A", "B", EdgeType.DIRECTED, 1, "x"));
        assertThrows(InvalidDataException.class, () -> new Edge("R1", "A", "B", null, 1, "x"));
        assertThrows(InvalidDataException.class, () -> new Edge("R1", "A", "A", EdgeType.UNDIRECTED, 1, "x"));
    }

    @Test
    void withWeightCreaOtraInstanciaSinModificarLaOriginal() {
        Edge e = new Edge("R1", "A", "B", EdgeType.UNDIRECTED, 5, "x");
        Edge e2 = e.withWeight(9);
        assertEquals(5, e.getWeight());
        assertEquals(9, e2.getWeight());
        assertThrows(InvalidDataException.class, () -> e.withWeight(0));
    }
}
