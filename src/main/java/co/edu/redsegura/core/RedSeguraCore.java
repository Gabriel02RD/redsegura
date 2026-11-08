package co.edu.redsegura.core;

import co.edu.redsegura.exception.InvalidDataException;
import co.edu.redsegura.exception.NotFoundException;
import co.edu.redsegura.graph.BreadthFirstSearch;
import co.edu.redsegura.graph.DepthFirstSearch;
import co.edu.redsegura.graph.DijkstraSolver;
import co.edu.redsegura.graph.DijkstraSolver.PathResult;
import co.edu.redsegura.graph.GraphRepresentations;
import co.edu.redsegura.graph.MixedGraph;
import co.edu.redsegura.graph.Neighbor;
import co.edu.redsegura.model.Vertex;
import co.edu.redsegura.tree.AvlTree;
import co.edu.redsegura.tree.BinarySearchTree;
import co.edu.redsegura.tree.SearchResult;
import java.util.List;

/**
 * Fachada de la aplicación. RedSeguraApplication solo habla con esta clase;
 * ella coordina el ABB, el AVL y el grafo mediante sus métodos públicos.
 */
public class RedSeguraCore {

    /** Comparación de la búsqueda de un mismo lugar en el ABB y en el AVL (consulta Q1). */
    public record SearchComparison(String key, boolean found, int bstSteps, int avlSteps,
                                   int bstHeight, int avlHeight) {
    }

    private final MixedGraph graph;
    private final BinarySearchTree<String, Vertex> bst = new BinarySearchTree<>();
    private final AvlTree<String, Vertex> avl = new AvlTree<>();

    public RedSeguraCore(MixedGraph graph) {
        if (graph == null) {
            throw new InvalidDataException("El grafo no puede ser nulo.");
        }
        this.graph = graph;
        for (Vertex v : graph.getVertices()) { // se insertan en orden S01..S12
            bst.insert(v.getId(), v);
            avl.insert(v.getId(), v);
        }
    }

    public static RedSeguraCore withDefaultData() {
        return new RedSeguraCore(DatasetFactory.createCityGraph());
    }

    // ------------------------------------------------------------------ Q1: árboles

    public Vertex findVertexByLabel(String label) {
        if (label != null) {
            for (Vertex v : graph.getVertices()) {
                if (v.getLabel().equalsIgnoreCase(label.trim())) {
                    return v;
                }
            }
        }
        throw new NotFoundException("No existe un lugar con la etiqueta \"" + label + "\".");
    }

    public SearchComparison compareSearch(String vertexId) {
        SearchResult<Vertex> inBst = bst.search(vertexId);
        SearchResult<Vertex> inAvl = avl.search(vertexId);
        return new SearchComparison(vertexId, inBst.found() && inAvl.found(),
                inBst.steps(), inAvl.steps(), bst.height(), avl.height());
    }

    public SearchComparison compareSearchByLabel(String label) {
        return compareSearch(findVertexByLabel(label).getId());
    }

    // ------------------------------------------------------------------ Q2: rotaciones

    /** Construye un AVL pequeño por cada tipo de rotación y describe qué ocurrió. */
    public String demonstrateRotations() {
        String[] names = {"LL", "RR", "LR", "RL"};
        int[][] sequences = {{3, 2, 1}, {1, 2, 3}, {3, 1, 2}, {1, 3, 2}};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            AvlTree<Integer, String> tree = new AvlTree<>();
            for (int key : sequences[i]) {
                tree.insert(key, "nodo " + key);
            }
            sb.append("Rotación ").append(names[i]).append(": insertar ");
            for (int k = 0; k < sequences[i].length; k++) {
                sb.append(sequences[i][k]).append(k < sequences[i].length - 1 ? ", " : "");
            }
            sb.append(System.lineSeparator())
              .append("   Registro: ").append(tree.getRotationLog()).append(System.lineSeparator())
              .append("   Raíz final: ").append(tree.rootKey())
              .append(" | Preorden: ").append(tree.preOrder())
              .append(" | Altura: ").append(tree.height())
              .append(" | Balanceado: ").append(tree.isBalanced())
              .append(System.lineSeparator());
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------ Q3: representaciones

    public GraphRepresentations representations() {
        return new GraphRepresentations(graph);
    }

    // ------------------------------------------------------------------ Q4: recorridos

    public BreadthFirstSearch.Result bfs(String startId) {
        return BreadthFirstSearch.run(graph, startId);
    }

    public DepthFirstSearch.Result dfs(String startId) {
        return DepthFirstSearch.run(graph, startId);
    }

    public boolean hasCycle() {
        return DepthFirstSearch.hasCycle(graph);
    }

    public List<List<String>> weakComponents() {
        return DepthFirstSearch.weakComponents(graph);
    }

    // ------------------------------------------------------------------ Q5 y Q6: rutas y bloqueos

    public PathResult shortestRoute(String fromId, String toId) {
        return DijkstraSolver.shortestPath(graph, fromId, toId);
    }

    public void blockConnection(String edgeId) {
        graph.blockEdge(edgeId);
    }

    public void unblockConnection(String edgeId) {
        graph.unblockEdge(edgeId);
    }

    public void changeWeight(String edgeId, int newWeight) {
        graph.setEdgeWeight(edgeId, newWeight);
    }

    // ------------------------------------------------------------------ Q7: consultas seguras

    public List<Neighbor> neighborsOf(String vertexId) {
        return graph.neighbors(vertexId);
    }

    public String labelOf(String vertexId) {
        return graph.getVertex(vertexId).getLabel();
    }

    public String describePath(PathResult result) {
        if (!result.reachable()) {
            return "(sin ruta)";
        }
        StringBuilder sb = new StringBuilder();
        List<String> path = result.path();
        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i)).append(" (").append(labelOf(path.get(i))).append(")");
            if (i < path.size() - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }
}
