package co.edu.redsegura.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.redsegura.core.DatasetFactory;
import co.edu.redsegura.exception.NotFoundException;
import co.edu.redsegura.model.Edge;
import co.edu.redsegura.model.EdgeType;
import co.edu.redsegura.model.Vertex;
import java.util.List;
import org.junit.jupiter.api.Test;

class TraversalTest {

    @Test
    void bfsDesdeS02SigueElOrdenDeInsercionDeLasConexiones() {
        BreadthFirstSearch.Result r = BreadthFirstSearch.run(DatasetFactory.createCityGraph(), "S02");
        assertEquals(List.of("S02", "S01", "S06", "S07", "S09", "S05", "S08", "S10", "S12", "S03", "S04", "S11"),
                r.order());
        assertEquals(0, r.level().get("S02"));
        assertEquals(1, r.level().get("S01"));
        assertEquals("S02", r.parent().get("S01"));
    }

    @Test
    void dfsDesdeS02SigueElOrdenDeInsercionDeLasConexiones() {
        DepthFirstSearch.Result r = DepthFirstSearch.run(DatasetFactory.createCityGraph(), "S02");
        assertEquals(List.of("S02", "S01", "S07", "S09", "S05", "S10", "S08", "S12", "S03", "S04", "S11", "S06"),
                r.order());
    }

    @Test
    void bfsYDfsVisitanLosMismosVerticesPeroEnOrdenDistinto() {
        MixedGraph g = DatasetFactory.createCityGraph();
        List<String> bfs = BreadthFirstSearch.run(g, "S02").order();
        List<String> dfs = DepthFirstSearch.run(g, "S02").order();
        assertEquals(12, bfs.size());
        assertEquals(12, dfs.size());
        assertTrue(bfs.containsAll(dfs));
        assertFalse(bfs.equals(dfs));
    }

    @Test
    void recorridosRespetanLaOrientacion() {
        // Desde S06 solo se puede ir S06 -> S05 -> S09 ... pero nunca volver a S02 por el arco R05
        MixedGraph g = DatasetFactory.createCityGraph();
        BreadthFirstSearch.Result r = BreadthFirstSearch.run(g, "S06");
        assertEquals("S05", r.order().get(1));
        assertEquals(0, r.level().get("S06"));
    }

    @Test
    void verticeInicialInexistenteLanzaExcepcion() {
        MixedGraph g = DatasetFactory.createCityGraph();
        assertThrows(NotFoundException.class, () -> BreadthFirstSearch.run(g, "S99"));
        assertThrows(NotFoundException.class, () -> DepthFirstSearch.run(g, "S99"));
    }

    @Test
    void grafoDesconectadoNoVisitaElVerticeAislado() {
        MixedGraph g = DatasetFactory.createCityGraph();
        g.addVertex(new Vertex("X01", "Aislado", "Prueba"));
        assertEquals(12, BreadthFirstSearch.run(g, "S02").order().size());
        assertEquals(12, DepthFirstSearch.run(g, "S02").order().size());
        assertEquals(List.of("X01"), BreadthFirstSearch.run(g, "X01").order());
        assertEquals(2, DepthFirstSearch.weakComponents(g).size());
    }

    @Test
    void componentesDebilesDelConjuntoOriginalEsUna() {
        List<List<String>> components = DepthFirstSearch.weakComponents(DatasetFactory.createCityGraph());
        assertEquals(1, components.size());
        assertEquals(12, components.get(0).size());
    }

    @Test
    void detectaCiclosEnElConjuntoOriginal() {
        assertTrue(DepthFirstSearch.hasCycle(DatasetFactory.createCityGraph()));
    }

    @Test
    void noDetectaCicloEnUnaCadenaDirigida() {
        MixedGraph g = new MixedGraph();
        g.addVertex(new Vertex("A", "A", "T"));
        g.addVertex(new Vertex("B", "B", "T"));
        g.addVertex(new Vertex("C", "C", "T"));
        g.addEdge(new Edge("E1", "A", "B", EdgeType.DIRECTED, 1, "ab"));
        g.addEdge(new Edge("E2", "B", "C", EdgeType.DIRECTED, 1, "bc"));
        assertFalse(DepthFirstSearch.hasCycle(g));
        g.addEdge(new Edge("E3", "C", "A", EdgeType.DIRECTED, 1, "ca"));
        assertTrue(DepthFirstSearch.hasCycle(g));
    }

    @Test
    void unaSolaAristaNoDirigidaNoEsCiclo() {
        MixedGraph g = new MixedGraph();
        g.addVertex(new Vertex("A", "A", "T"));
        g.addVertex(new Vertex("B", "B", "T"));
        g.addEdge(new Edge("E1", "A", "B", EdgeType.UNDIRECTED, 1, "ab"));
        assertFalse(DepthFirstSearch.hasCycle(g));
    }
}
