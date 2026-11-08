\# RedSegura – Árboles y grafos aplicados a una red de atención de emergencias



\*\*Proyecto:\*\* RedSegura  

\*\*Asignatura:\*\* Estructura de Datos II  

\*\*Modalidad:\*\* Individual  

\*\*Autor:\*\* Luis Gabriel Rivas Díaz  

\*\*Docente:\*\* Jaider Enrique Reyes Herazo  

\*\*Periodo:\*\* 2026  



\## 1. Descripción del proyecto



RedSegura es una aplicación desarrollada en Java que modela la movilidad entre universidades, centros de salud, nodos de transporte y puntos de encuentro de Sincelejo mediante estructuras de datos y algoritmos de grafos.



El sistema permite administrar y consultar lugares mediante árboles binarios de búsqueda y árboles AVL. También representa las conexiones de la red mediante un grafo mixto y permite realizar recorridos, consultar rutas y calcular caminos de costo mínimo.



\*\*Nota:\*\* Los tiempos y las conexiones utilizados corresponden a datos didácticos de simulación y no representan información oficial del tránsito de Sincelejo.



\## 2. Objetivo



Implementar una aplicación que permita representar y analizar una red de atención de emergencias mediante árboles y grafos, aplicando algoritmos de búsqueda, recorridos y cálculo de rutas mínimas para comparar el comportamiento y la eficiencia de las estructuras de datos.



\## 3. Tecnologías utilizadas



\- \*\*Lenguaje:\*\* Java 17.

\- \*\*Gestor de dependencias y construcción:\*\* Apache Maven.

\- \*\*Entorno de desarrollo:\*\* Apache NetBeans.

\- \*\*Pruebas unitarias:\*\* JUnit 5 (Jupiter 5.10.2).

\- \*\*Control de versiones:\*\* Git y GitHub.



\## 4. Requisitos



Para compilar y ejecutar el proyecto se necesita:



\- JDK 17.

\- Apache Maven 3.8 o superior.

\- Git (opcional, para clonar el repositorio).

\- Apache NetBeans (opcional, para abrir el proyecto).



\## 5. Clonar, compilar, probar y ejecutar



Clonar el repositorio:



```bash

git clone https://github.com/Gabriel02RD/redsegura.git

cd redsegura

```



Ejecutar las pruebas unitarias:



```bash

mvn clean test

```



Compilar y empaquetar el proyecto:



```bash

mvn clean package

```



Ejecutar el archivo JAR generado:



```bash

java -jar target/redsegura-1.0.0.jar

```



También se puede abrir el proyecto desde Apache NetBeans mediante \*\*File → Open Project\*\* y ejecutar la clase principal:



`co.edu.redsegura.app.RedSeguraApplication`



\## 6. Arquitectura del sistema



El proyecto utiliza una organización por paquetes para separar las responsabilidades de la aplicación.



```text

RedSeguraApplication (CLI)

&#x20;         |

&#x20;         v

&#x20;    RedSeguraCore

&#x20;      /       \\

&#x20;     v         v

&#x20; AvlTree    BinarySearchTree

&#x20;     |

&#x20;     v

&#x20; MixedGraph

&#x20;     |

&#x20;     +---- GraphRepresentations

&#x20;     |

&#x20;     +---- BreadthFirstSearch

&#x20;     |

&#x20;     +---- DepthFirstSearch

&#x20;     |

&#x20;     +---- DijkstraSolver

&#x20;     |

&#x20;     v

&#x20; Vertex, Edge, EdgeType

```



\### Estructura de paquetes



| Paquete | Responsabilidad |

|---|---|

| `app` | Contiene `RedSeguraApplication`, responsable del menú de consola. |

| `core` | Contiene `RedSeguraCore`, que centraliza las operaciones, y `DatasetFactory`, que construye los datos de prueba. |

| `exception` | Contiene las excepciones personalizadas para datos inválidos, elementos duplicados y elementos no encontrados. |

| `graph` | Implementa el grafo mixto, sus representaciones, recorridos BFS y DFS, y el algoritmo de Dijkstra. |

| `model` | Contiene las clases `Vertex`, `Edge`, `EdgeType` y `Validation`. |

| `tree` | Implementa el árbol binario de búsqueda, el árbol AVL y los resultados de búsqueda. |



\## 7. Decisiones de diseño



\- \*\*Claves de los árboles:\*\* se utiliza el identificador de cada lugar (`S01` a `S12`). Los elementos se insertan en ese orden para evidenciar la diferencia entre el árbol binario de búsqueda y el árbol AVL.

\- \*\*Árbol binario de búsqueda:\*\* al insertar los identificadores ordenados, el árbol queda degenerado y alcanza una altura de 12.

\- \*\*Árbol AVL:\*\* aplica rotaciones para mantener el equilibrio y alcanza una altura de 4 con los mismos datos.

\- \*\*Altura:\*\* se define como 0 para un árbol vacío y 1 para un árbol con un solo nodo.

\- \*\*Factor de balance:\*\* se calcula como la altura del subárbol izquierdo menos la altura del subárbol derecho.

\- \*\*Orden de vecinos:\*\* los recorridos respetan el orden de inserción de las conexiones (`R01` a `R18`), permitiendo resultados reproducibles en BFS y DFS.

\- \*\*Grafo mixto:\*\* las conexiones no dirigidas se almacenan para ambos extremos; los arcos dirigidos se registran desde el origen hacia el destino.

\- \*\*Matriz de adyacencia:\*\* utiliza 0 para indicar ausencia de conexión. Los pesos válidos son mayores que cero.

\- \*\*Matriz de incidencia:\*\* representa las conexiones no dirigidas mediante `+1/+1` y los arcos dirigidos mediante `-1` en el origen y `+1` en el destino.

\- \*\*Bloqueos:\*\* el método `blockEdge` permite bloquear temporalmente una conexión sin eliminarla de la estructura original.

\- \*\*Dijkstra:\*\* utiliza una cola de prioridad (`PriorityQueue`) y relajación estricta mediante el operador `<`. Cuando existen rutas con el mismo costo, el orden de procesamiento determina cuál se devuelve primero.



\## 8. Complejidad de los algoritmos



Donde:



\- `V`: número de vértices.

\- `E`: número de conexiones.

\- `n`: número de nodos del árbol.

\- `h`: altura del árbol.



| Componente | Complejidad temporal | Complejidad espacial |

|---|---|---|

| ABB (inserción, búsqueda y eliminación) | O(h): O(log n) promedio y O(n) en el peor caso | O(n) |

| AVL (inserción, búsqueda y eliminación) | O(log n) garantizado | O(n) |

| Recorridos de árboles | O(n) | O(h) por recursión |

| Construcción de lista de adyacencia | O(V + E) | O(V + E) |

| Construcción de matriz de adyacencia | O(V² + E) | O(V²) |

| Construcción de matriz de incidencia | O(V × E) | O(V × E) |

| BFS | O(V + E) | O(V) |

| DFS | O(V + E) | O(V) |

| Dijkstra con `PriorityQueue` | O((V + E) log V) | O(V + E) |



\## 9. Resultados de las consultas



El sistema implementa las consultas Q1 a Q7 solicitadas para el proyecto.



| Consulta | Resultado |

|---|---|

| Q1 | Búsqueda del lugar `S05` (Hospital Universitario): 5 pasos en el ABB y 4 pasos en el AVL. |

| Q2 | Comprobación de rotaciones LL, RR, LR y RL del árbol AVL. La raíz final es 2 y el árbol queda balanceado. |

| Q3 | Generación de lista de adyacencia, matriz de adyacencia y matriz de incidencia para 12 vértices y 18 conexiones. |

| Q4 | Ejecución de recorridos BFS y DFS desde el vértice `S02`. |

| Q5 | Cálculo de la ruta mínima desde `S02` hasta `S08`: 24 minutos. |

| Q6 | Bloqueo temporal de la conexión `R16`. El costo mínimo continúa siendo 24 minutos, pero cambia la ruta. |

| Q7 | Consulta de un identificador inexistente (`S99`) y manejo controlado de la excepción, sin interrumpir el programa. |



\### Resultados de rutas



\*\*Q5 – Ruta original:\*\*



`S02 → S01 → S07 → S08`



Costo total: \*\*24 minutos\*\*.



\*\*Q6 – Ruta después de bloquear R16:\*\*



`S02 → S01 → S09 → S10 → S08`



Costo total: \*\*24 minutos\*\*.



El bloqueo permite comprobar que el algoritmo puede encontrar una ruta alternativa cuando una conexión deja de estar disponible.



\## 10. Pruebas unitarias



El proyecto utiliza JUnit 5 para verificar el funcionamiento de sus estructuras de datos, algoritmos y operaciones principales.



Se ejecutaron las pruebas mediante:



```bash

mvn clean test

```



\*\*Resultado de la ejecución verificada:\*\*



\- Pruebas ejecutadas: 72.

\- Fallos: 0.

\- Errores: 0.

\- Pruebas omitidas: 0.

\- Resultado de Maven: `BUILD SUCCESS`.



Las pruebas incluyen casos normales y situaciones especiales, como identificadores duplicados o inexistentes, pesos inválidos, grafos desconectados, bloqueos de conexiones y cambios de peso.



\## 11. Evidencias



Las evidencias de funcionamiento del proyecto se organizan en la carpeta `Evidencias\_RedSegura`, que contiene las capturas correspondientes a:



1\. Ejecución de las pruebas unitarias con JUnit 5.

2\. Compilación y empaquetado mediante Maven.

3\. Ejecución del archivo JAR.

4\. Resultados de las consultas Q1 a Q7.

5\. Publicación del repositorio en GitHub.



\## 12. Créditos y originalidad



El proyecto fue desarrollado individualmente por \*\*Luis Gabriel Rivas Díaz\*\* como trabajo académico de la asignatura Estructura de Datos II.



La implementación aplica estructuras de datos y algoritmos estudiados durante el curso, incluyendo árboles binarios de búsqueda, árboles AVL, grafos mixtos, BFS, DFS y Dijkstra.



Las pruebas y los resultados presentados corresponden a la ejecución del proyecto.



\---



\*\*Repositorio:\*\* https://github.com/Gabriel02RD/redsegura



\*\*Autor:\*\* Luis Gabriel Rivas Díaz  

\*\*Modalidad:\*\* Individual  

\*\*Periodo académico:\*\* 2026

