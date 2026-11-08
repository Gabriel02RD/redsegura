package co.edu.redsegura.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.redsegura.core.DatasetFactory;
import co.edu.redsegura.exception.DuplicateException;
import co.edu.redsegura.exception.InvalidDataException;
import co.edu.redsegura.exception.NotFoundException;
import co.edu.redsegura.model.Edge;
import co.edu.redsegura.model.EdgeType;
import co.edu.redsegura.model.Vertex;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MixedGraphTest {

    private MixedGraph graph;

    @BeforeEach
    void setUp() {
        graph = new MixedGraph();
        graph.addVertex(new Vertex("A", "Lugar A", "Prueba"));
        graph.addVertex(new Vertex("B", "Lugar B", "Prueba"));
        graph.addVertex(new Vertex("C", "Lugar C", "Prueba"));
        graph.addEdge(new Edge("E1", "A", "B", EdgeType.DIRECTED, 5, "arco A->B"));
        graph.addEdge(new Edge("E2", "B", "C", EdgeType.UNDIRECTED, 7, "arista B-C"));
    }

    private List<String> ids(List<Neighbor> list) {
        List<String> out = new ArrayList<>();
        for (Neighbor n : list) {
            out.add(n.vertexId());
        }
        return out;
    }

    @Test
    void conjuntoDelParcialTiene12VerticesY18Conexiones() {
        MixedGraph g = DatasetFactory.createCityGraph();
        assertEquals(12, g.vertexCount());
        assertEquals(18, g.edgeCount());
    }

    @Test
    void arcoSoloSeRecorreEnSuSentido() {
        assertEquals(List.of("B"), ids(graph.neighbors("A")));
        assertEquals(List.of("C"), ids(graph.neighbors("B"))); // E1 no sale de B
    }

    @Test
    void aristaNoDirigidaSeRecorreEnAmbosSentidos() {
        assertEquals(List.of("B"), ids(graph.neighbors("C")));
    }

    @Test
    void verticeDuplicadoSeRechaza() {
        assertThrows(DuplicateException.class, () -> graph.addVertex(new Vertex("A", "Otro", "Prueba")));
    }

    @Test
    void conexionDuplicadaSeRechaza() {
        assertThrows(DuplicateException.class,
                () -> graph.addEdge(new Edge("E1", "A", "C", EdgeType.DIRECTED, 1, "dup")));
    }

    @Test
    void conexionConVerticeInexistenteSeRechaza() {
        assertThrows(NotFoundException.class,
                () -> graph.addEdge(new Edge("E3", "A", "Z", EdgeType.DIRECTED, 1, "x")));
    }

    @Test
    void consultarVecinosDeVerticeInexistenteLanzaExcepcion() {
        assertThrows(NotFoundException.class, () -> graph.neighbors("S99"));
        assertThrows(NotFoundException.class, () -> graph.getVertex("S99"));
    }

    @Test
    void bloqueoOcultaLaConexionPeroNoLaBorra() {
        graph.blockEdge("E1");
        assertTrue(graph.isBlocked("E1"));
        assertTrue(graph.neighbors("A").isEmpty());
        assertEquals(1, graph.neighbors("A", true).size());
        assertEquals(2, graph.edgeCount());
        graph.unblockEdge("E1");
        assertFalse(graph.isBlocked("E1"));
        assertEquals(1, graph.neighbors("A").size());
    }

    @Test
    void bloquearConexionInexistenteLanzaExcepcion() {
        assertThrows(NotFoundException.class, () -> graph.blockEdge("R99"));
    }

    @Test
    void cambiarPesoActualizaGrafoYValida() {
        graph.setEdgeWeight("E2", 20);
        assertEquals(20, graph.getEdge("E2").getWeight());
        assertEquals(20, graph.neighbors("B").get(0).weight());
        assertEquals(20, graph.neighbors("C").get(0).weight());
        assertThrows(InvalidDataException.class, () -> graph.setEdgeWeight("E2", 0));
        assertEquals(20, graph.getEdge("E2").getWeight());
    }
}
