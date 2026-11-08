package co.edu.redsegura.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.redsegura.core.DatasetFactory;
import co.edu.redsegura.exception.InvalidDataException;
import co.edu.redsegura.exception.NotFoundException;
import co.edu.redsegura.graph.DijkstraSolver.PathResult;
import co.edu.redsegura.model.Vertex;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DijkstraSolverTest {

    private MixedGraph graph;

    @BeforeEach
    void setUp() {
        graph = DatasetFactory.createCityGraph();
    }

    @Test
    void rutaDeControlS02aS08CuestaVeinticuatroMinutos() {
        PathResult r = DijkstraSolver.shortestPath(graph, "S02", "S08");
        assertTrue(r.reachable());
        assertEquals(24, r.totalMinutes());
        assertEquals(List.of("S02", "S01", "S07", "S08"), r.path());
    }

    @Test
    void bloquearR16CambiaElCaminoPeroConservaElCosto() {
        graph.blockEdge("R16");
        PathResult r = DijkstraSolver.shortestPath(graph, "S02", "S08");
        assertEquals(24, r.totalMinutes());
        assertEquals(List.of("S02", "S01", "S09", "S10", "S08"), r.path());
    }

    @Test
    void desbloquearRestauraLaRutaOriginal() {
        graph.blockEdge("R16");
        graph.unblockEdge("R16");
        assertEquals(List.of("S02", "S01", "S07", "S08"), DijkstraSolver.shortestPath(graph, "S02", "S08").path());
    }

    @Test
    void cambiarElPesoDeR16ModificaLaRuta() {
        graph.setEdgeWeight("R16", 30);
        PathResult r = DijkstraSolver.shortestPath(graph, "S02", "S08");
        assertEquals(24, r.totalMinutes());
        assertEquals(List.of("S02", "S01", "S09", "S10", "S08"), r.path());
    }

    @Test
    void respetaLaOrientacion() {
        // S07 -> S01 cuesta 6 (R02), pero S01 -> S07 cuesta 4 (R01)
        assertEquals(4, DijkstraSolver.shortestPath(graph, "S01", "S07").totalMinutes());
        assertEquals(6, DijkstraSolver.shortestPath(graph, "S07", "S01").totalMinutes());
    }

    @Test
    void origenIgualADestinoCuestaCero() {
        PathResult r = DijkstraSolver.shortestPath(graph, "S05", "S05");
        assertTrue(r.reachable());
        assertEquals(0, r.totalMinutes());
        assertEquals(List.of("S05"), r.path());
    }

    @Test
    void destinoInalcanzableEnGrafoDesconectado() {
        graph.addVertex(new Vertex("X01", "Aislado", "Prueba"));
        PathResult r = DijkstraSolver.shortestPath(graph, "S02", "X01");
        assertFalse(r.reachable());
        assertTrue(r.path().isEmpty());
    }

    @Test
    void verticesInexistentesLanzanExcepcion() {
        assertThrows(NotFoundException.class, () -> DijkstraSolver.shortestPath(graph, "S99", "S08"));
        assertThrows(NotFoundException.class, () -> DijkstraSolver.shortestPath(graph, "S02", "S99"));
    }

    @Test
    void pesoInvalidoNoPuedeEntrarAlGrafo() {
        assertThrows(InvalidDataException.class, () -> graph.setEdgeWeight("R01", -5));
        assertEquals(24, DijkstraSolver.shortestPath(graph, "S02", "S08").totalMinutes());
    }

    @Test
    void solucionExponeDistanciasYPredecesores() {
        DijkstraSolver.Solution s = DijkstraSolver.solve(graph, "S02");
        assertEquals(0, s.distances().get("S02"));
        assertEquals(8, s.distances().get("S01"));
        assertEquals("S01", s.predecessors().get("S07"));
        assertEquals(24, s.distances().get("S08"));
    }
}
