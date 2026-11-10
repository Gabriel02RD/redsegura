
package co.edu.redsegura.app;

import co.edu.redsegura.core.RedSeguraCore;
import co.edu.redsegura.core.RedSeguraCore.SearchComparison;
import co.edu.redsegura.exception.NotFoundException;
import co.edu.redsegura.exception.RedSeguraException;
import co.edu.redsegura.graph.BreadthFirstSearch;
import co.edu.redsegura.graph.DepthFirstSearch;
import co.edu.redsegura.graph.DijkstraSolver.PathResult;
import co.edu.redsegura.graph.GraphRepresentations;
import java.util.Scanner;

/** Aplicación de consola RedSegura: ejecuta las consultas Q1 a Q7 y permite pruebas en vivo. */
public class RedSeguraApplication {

    private final RedSeguraCore core;
    private final Scanner in;

    public RedSeguraApplication(RedSeguraCore core, Scanner in) {
        this.core = core;
        this.in = in;
    }

    public static void main(String[] args) {
        RedSeguraApplication app = new RedSeguraApplication(
                RedSeguraCore.withDefaultData(),
                new Scanner(System.in)
        );
        app.run();
    }

    public void run() {
        boolean exit = false;

        while (!exit) {
            printMenu();
            String option = readLine("Seleccione una opción: ");

            try {
                switch (option) {
                    case "1" -> q1();
                    case "2" -> q2();
                    case "3" -> q3();
                    case "4" -> q4();
                    case "5" -> q5();
                    case "6" -> q6();
                    case "7" -> q7();
                    case "8" -> fullDemo();
                    case "9" -> customRoute();
                    case "10" -> toggleBlock();
                    case "11" -> changeWeight();
                    case "12" -> customTraversal();
                    case "0" -> exit = true;
                    default -> System.out.println("[AVISO] Opción no válida.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("[ERROR] Debe ingresar un número entero válido.");
            } catch (RedSeguraException ex) {
                System.out.println("[ERROR] " + ex.getMessage());
            }

            if (!exit) {
                System.out.println();
                System.out.println("Presione ENTER para continuar...");
                if (in.hasNextLine()) {
                    in.nextLine();
                }
            }
        }

        System.out.println();
        System.out.println("============================================================");
        System.out.println("       Gracias por utilizar el sistema REDSEGURA");
        System.out.println("============================================================");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                         REDSEGURA");
        System.out.println("       Sistema de gestión y análisis de redes de emergencia");
        System.out.println("============================================================");

        System.out.println();
        System.out.println("  CONSULTAS PRINCIPALES");
        System.out.println("  ----------------------------------------------------------");
        System.out.println("  1.  Q1: Buscar Hospital Universitario en ABB y AVL");
        System.out.println("  2.  Q2: Demostración de rotaciones AVL (LL, RR, LR, RL)");
        System.out.println("  3.  Q3: Representaciones del grafo");
        System.out.println("  4.  Q4: Recorridos BFS y DFS desde S02");
        System.out.println("  5.  Q5: Ruta mínima con Dijkstra (S02 -> S08)");
        System.out.println("  6.  Q6: Bloquear R16 y recalcular la ruta");
        System.out.println("  7.  Q7: Consultar un vértice inexistente");

        System.out.println();
        System.out.println("  HERRAMIENTAS ADICIONALES");
        System.out.println("  ----------------------------------------------------------");
        System.out.println("  8.  Demostración completa (Q1 a Q7)");
        System.out.println("  9.  Calcular una ruta mínima personalizada");
        System.out.println("  10. Bloquear o desbloquear una conexión");
        System.out.println("  11. Modificar el peso de una conexión");
        System.out.println("  12. BFS, DFS, ciclos y componentes desde un vértice");

        System.out.println();
        System.out.println("  0.  Salir");
        System.out.println("============================================================");
    }

    // ------------------------------------------------------------------
    // CONSULTAS PRINCIPALES
    // ------------------------------------------------------------------

    private void q1() {
        title("Q1 - Búsqueda en ABB vs AVL");
        SearchComparison r = core.compareSearchByLabel("Hospital Universitario");
        System.out.println("Clave buscada: " + r.key()
                + " (Hospital Universitario) | encontrado: " + r.found());
        System.out.println("ABB: " + r.bstSteps()
                + " pasos (altura del árbol: " + r.bstHeight() + ")");
        System.out.println("AVL: " + r.avlSteps()
                + " pasos (altura del árbol: " + r.avlHeight() + ")");
        System.out.println("Las claves se insertaron en orden S01..S12: el ABB queda como una lista enlazada,");
        System.out.println("mientras que el AVL se reorganiza y mantiene la altura en O(log n).");
    }

    private void q2() {
        title("Q2 - Rotaciones del AVL");
        System.out.print(core.demonstrateRotations());
    }

    private void q3() {
        title("Q3 - Representaciones del grafo");
        GraphRepresentations rep = core.representations();

        System.out.println("Lista de adyacencia [vecino:minutos conexión]:");
        System.out.print(rep.adjacencyListToString());

        System.out.println();
        System.out.println("Matriz de adyacencia ponderada ('-' = sin conexión):");
        System.out.print(rep.adjacencyMatrixToString());

        System.out.println();
        System.out.println("Matriz de incidencia (arista no dirigida: +1/+1; arco: -1 origen, +1 destino):");
        System.out.print(rep.incidenceMatrixToString());
    }

    private void q4() {
        title("Q4 - BFS y DFS desde S02");
        printTraversals("S02");
        System.out.println("El orden difiere porque BFS visita por niveles (primero todos los vecinos de");
        System.out.println("un vértice) y DFS baja por una rama hasta agotarla antes de retroceder.");
    }

    private void q5() {
        title("Q5 - Ruta mínima S02 -> S08");
        PathResult r = core.shortestRoute("S02", "S08");
        printRoute(r);
        System.out.println("Resultado de control (24 minutos): "
                + (r.reachable() && r.totalMinutes() == 24 ? "OK" : "REVISAR"));
    }

    private void q6() {
        title("Q6 - Bloquear R16 (Ruta SETP al terminal) y recalcular");

        PathResult before = core.shortestRoute("S02", "S08");
        System.out.println("Antes del bloqueo:");
        printRoute(before);

        core.blockConnection("R16");

        try {
            PathResult after = core.shortestRoute("S02", "S08");
            System.out.println("Con R16 bloqueada:");
            printRoute(after);

            if (!after.reachable()) {
                System.out.println("Explicación: al bloquear R16 ya no existe ningún camino hacia el terminal.");
            } else if (after.path().equals(before.path())) {
                System.out.println("Explicación: la ruta no cambió porque R16 no formaba parte de ella.");
            } else if (after.totalMinutes() == before.totalMinutes()) {
                System.out.println("Explicación: R16 era parte de la ruta, pero existía una alternativa por el centro");
                System.out.println("(Parque Santander y Mercado) con el mismo costo; el tiempo no aumenta, cambia el camino.");
            } else {
                System.out.println("Explicación: la ruta cambió y el tiempo pasó de "
                        + before.totalMinutes() + " a " + after.totalMinutes() + " minutos.");
            }
        } finally {
            core.unblockConnection("R16");
        }

        System.out.println("(R16 fue desbloqueada para no alterar las demás consultas.)");
    }

    private void q7() {
        title("Q7 - Consulta de un vértice inexistente");

        try {
            core.neighborsOf("S99");
        } catch (NotFoundException ex) {
            System.out.println("[CONTROLADO] " + ex.getMessage());
        }

        System.out.println("El programa continúa funcionando con normalidad.");
    }

    private void fullDemo() {
        q1();
        q2();
        q3();
        q4();
        q5();
        q6();
        q7();
    }

    // ------------------------------------------------------------------
    // PRUEBAS EN VIVO (SUSTENTACIÓN)
    // ------------------------------------------------------------------

    private void customRoute() {
        String from = readLine("Origen (ej. S02): ");
        String to = readLine("Destino (ej. S08): ");
        printRoute(core.shortestRoute(from, to));
    }

    private void toggleBlock() {
        String id = readLine("ID de la conexión (ej. R16): ");
        String action = readLine("¿Bloquear (B) o desbloquear (D)?: ");

        if (action.equalsIgnoreCase("B")) {
            core.blockConnection(id);
            System.out.println("Conexión " + id + " bloqueada.");
        } else if (action.equalsIgnoreCase("D")) {
            core.unblockConnection(id);
            System.out.println("Conexión " + id + " desbloqueada.");
        } else {
            System.out.println("Acción no válida: use B o D.");
        }
    }

    private void changeWeight() {
        String id = readLine("ID de la conexión (ej. R03): ");
        int weight = Integer.parseInt(readLine("Nuevo peso en minutos: "));
        core.changeWeight(id, weight);
        System.out.println("Peso de " + id + " actualizado a " + weight + " minutos.");
    }

    private void customTraversal() {
        String start = readLine("Vértice de inicio (ej. S02): ");
        printTraversals(start);
        System.out.println("¿Tiene ciclos? " + (core.hasCycle() ? "sí" : "no"));
        System.out.println("Componentes débilmente conexas: " + core.weakComponents());
    }

    // ------------------------------------------------------------------
    // UTILIDADES DE IMPRESIÓN
    // ------------------------------------------------------------------

    private void printTraversals(String start) {
        BreadthFirstSearch.Result bfs = core.bfs(start);
        DepthFirstSearch.Result dfs = core.dfs(start);

        System.out.println("BFS: " + bfs.order());
        System.out.println("   niveles: " + bfs.level());
        System.out.println("DFS: " + dfs.order());
    }

    private void printRoute(PathResult r) {
        if (!r.reachable()) {
            System.out.println("No hay ruta entre " + r.source() + " y " + r.target() + ".");
            return;
        }

        System.out.println("Tiempo total: " + r.totalMinutes() + " minutos");
        System.out.println("Ruta: " + core.describePath(r));
    }

    private void title(String text) {
        System.out.println();
        System.out.println("---------- " + text + " ----------");
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return in.hasNextLine() ? in.nextLine().trim() : "0";
    }
}
