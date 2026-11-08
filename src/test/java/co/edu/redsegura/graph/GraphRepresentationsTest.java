package co.edu.redsegura.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.redsegura.core.DatasetFactory;
import co.edu.redsegura.model.Edge;
import co.edu.redsegura.model.EdgeType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraphRepresentationsTest {

    private MixedGraph graph;
    private GraphRepresentations rep;
    private List<String> vertexOrder;
    private List<String> edgeOrder;

    @BeforeEach
    void setUp() {
        graph = DatasetFactory.createCityGraph();
        rep = new GraphRepresentations(graph);
        vertexOrder = rep.vertexOrder();
        edgeOrder = rep.edgeOrder();
    }

    private int v(String id) {
        return vertexOrder.indexOf(id);
    }

    private int e(String id) {
        return edgeOrder.indexOf(id);
    }

    @Test
    void dimensiones() {
        assertEquals(12, rep.adjacencyMatrix().length);
        assertEquals(12, rep.adjacencyMatrix()[0].length);
        assertEquals(12, rep.incidenceMatrix().length);
        assertEquals(18, rep.incidenceMatrix()[0].length);
        assertEquals(12, rep.adjacencyList().size());
    }

    @Test
    void ordenCoincideConElDelGrafo() {
        assertEquals("S01", vertexOrder.get(0));
        assertEquals("S12", vertexOrder.get(11));
        assertEquals("R01", edgeOrder.get(0));
        assertEquals("R18", edgeOrder.get(17));
    }

    @Test
    void matrizDeAdyacenciaRespetaOrientacionYPesos() {
        int[][] m = rep.adjacencyMatrix();
        // arcos opuestos con pesos distintos
        assertEquals(4, m[v("S01")][v("S07")]);
        assertEquals(6, m[v("S07")][v("S01")]);
        // arista no dirigida: simétrica
        assertEquals(5, m[v("S01")][v("S09")]);
        assertEquals(5, m[v("S09")][v("S01")]);
        // arco dirigido: solo un sentido
        assertEquals(10, m[v("S02")][v("S06")]);
        assertEquals(0, m[v("S06")][v("S02")]);
        // diagonal vacía
        assertEquals(0, m[v("S01")][v("S01")]);
    }

    @Test
    void matrizDeIncidenciaUsaSignosCorrectos() {
        int[][] m = rep.incidenceMatrix();
        // R01 arco S01 -> S07
        assertEquals(-1, m[v("S01")][e("R01")]);
        assertEquals(1, m[v("S07")][e("R01")]);
        // R03 arista S01 -- S09
        assertEquals(1, m[v("S01")][e("R03")]);
        assertEquals(1, m[v("S09")][e("R03")]);
        // un vértice ajeno a la conexión queda en 0
        assertEquals(0, m[v("S12")][e("R01")]);
    }

    @Test
    void cadaColumnaDeIncidenciaSumaSegunSuTipo() {
        int[][] m = rep.incidenceMatrix();
        List<Edge> edges = graph.getEdges();
        for (int k = 0; k < edges.size(); k++) {
            int sum = 0;
            for (int i = 0; i < m.length; i++) {
                sum += m[i][k];
            }
            int expected = (edges.get(k).getType() == EdgeType.DIRECTED) ? 0 : 2;
            assertEquals(expected, sum, "Columna " + edges.get(k).getId());
        }
    }

    @Test
    void listaDeAdyacenciaCoincideConLaMatriz() {
        int[][] m = rep.adjacencyMatrix();
        rep.adjacencyList().forEach((id, neighbors) -> {
            int count = 0;
            for (int j = 0; j < m.length; j++) {
                if (m[v(id)][j] != 0) {
                    count++;
                }
            }
            assertEquals(count, neighbors.size(), "Vértice " + id);
        });
    }

    @Test
    void lasRepresentacionesEnTextoNoEstanVacias() {
        assertTrue(rep.adjacencyListToString().contains("S01"));
        assertTrue(rep.adjacencyMatrixToString().contains("S12"));
        assertTrue(rep.incidenceMatrixToString().contains("R18"));
    }
}
