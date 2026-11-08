package co.edu.redsegura.core;

import co.edu.redsegura.graph.MixedGraph;
import co.edu.redsegura.model.Edge;
import co.edu.redsegura.model.EdgeType;
import co.edu.redsegura.model.Vertex;

/**
 * Fábrica del conjunto de datos didáctico del parcial (12 lugares y 18 conexiones de Sincelejo).
 * Los tiempos son datos de simulación, no información oficial de tránsito.
 */
public final class DatasetFactory {

    private DatasetFactory() {
    }

    public static MixedGraph createCityGraph() {
        MixedGraph g = new MixedGraph();

        g.addVertex(new Vertex("S01", "UAJS Sede C", "Universidad"));
        g.addVertex(new Vertex("S02", "UAJS Sede E", "Universidad"));
        g.addVertex(new Vertex("S03", "Universidad de Sucre", "Universidad"));
        g.addVertex(new Vertex("S04", "CECAR", "Universidad"));
        g.addVertex(new Vertex("S05", "Hospital Universitario", "Salud"));
        g.addVertex(new Vertex("S06", "Clínica Las Américas", "Salud"));
        g.addVertex(new Vertex("S07", "Estación Central SETP", "Transporte"));
        g.addVertex(new Vertex("S08", "Terminal de Transporte", "Transporte"));
        g.addVertex(new Vertex("S09", "Parque Santander", "Punto de encuentro"));
        g.addVertex(new Vertex("S10", "Mercado de Sincelejo", "Comercio"));
        g.addVertex(new Vertex("S11", "PEP Argelia", "Paradero"));
        g.addVertex(new Vertex("S12", "Glorieta Sincelejito", "Movilidad"));

        g.addEdge(new Edge("R01", "S01", "S07", EdgeType.DIRECTED, 4, "Corredor universitario"));
        g.addEdge(new Edge("R02", "S07", "S01", EdgeType.DIRECTED, 6, "Retorno por vía alterna"));
        g.addEdge(new Edge("R03", "S01", "S09", EdgeType.UNDIRECTED, 5, "Corredor peatonal"));
        g.addEdge(new Edge("R04", "S02", "S01", EdgeType.UNDIRECTED, 8, "Conexión entre sedes"));
        g.addEdge(new Edge("R05", "S02", "S06", EdgeType.DIRECTED, 10, "Ruta de atención"));
        g.addEdge(new Edge("R06", "S06", "S05", EdgeType.DIRECTED, 4, "Corredor clínico"));
        g.addEdge(new Edge("R07", "S05", "S09", EdgeType.UNDIRECTED, 6, "Vía hospitalaria"));
        g.addEdge(new Edge("R08", "S09", "S10", EdgeType.UNDIRECTED, 3, "Centro histórico"));
        g.addEdge(new Edge("R09", "S10", "S08", EdgeType.DIRECTED, 8, "Salida al terminal"));
        g.addEdge(new Edge("R10", "S08", "S10", EdgeType.DIRECTED, 9, "Ingreso desde terminal"));
        g.addEdge(new Edge("R11", "S03", "S12", EdgeType.UNDIRECTED, 4, "Corredor Puerta Roja"));
        g.addEdge(new Edge("R12", "S12", "S09", EdgeType.UNDIRECTED, 7, "Troncal al centro"));
        g.addEdge(new Edge("R13", "S04", "S11", EdgeType.UNDIRECTED, 6, "Conexión universitaria"));
        g.addEdge(new Edge("R14", "S11", "S07", EdgeType.DIRECTED, 8, "Ruta SETP al centro"));
        g.addEdge(new Edge("R15", "S07", "S09", EdgeType.UNDIRECTED, 3, "Transferencia central"));
        g.addEdge(new Edge("R16", "S07", "S08", EdgeType.DIRECTED, 12, "Ruta SETP al terminal"));
        g.addEdge(new Edge("R17", "S09", "S03", EdgeType.DIRECTED, 7, "Ruta hacia universidad"));
        g.addEdge(new Edge("R18", "S12", "S04", EdgeType.DIRECTED, 9, "Ruta hacia CECAR"));

        return g;
    }
}
