# RedSegura – Árboles y grafos aplicados a una red de atención de emergencias

**Proyecto:** RedSegura
**Asignatura:** Estructura de Datos II
**Modalidad:** Individual
**Autor:** Luis Gabriel Rivas Díaz
**Docente:** Jaider Enrique Reyes Herazo
**Periodo académico:** 2026

---

## 1. Descripción del proyecto

RedSegura es una aplicación de consola desarrollada en Java que simula una red de atención de emergencias en Sincelejo. El sistema permite administrar y analizar estaciones de atención y conexiones entre ellas mediante estructuras de datos y algoritmos de grafos.

El proyecto integra árboles binarios de búsqueda, árboles AVL, grafos mixtos, recorridos BFS y DFS, y el algoritmo de Dijkstra para calcular rutas de menor costo.

## 2. Objetivo general

Desarrollar una aplicación en Java que implemente árboles y grafos para representar y analizar una red de atención de emergencias, permitiendo realizar búsquedas, recorridos y cálculos de rutas mediante algoritmos y estructuras de datos eficientes.

## 3. Tecnologías utilizadas

- **Lenguaje de programación:** Java 17.
- **Gestor de construcción:** Apache Maven.
- **Pruebas automatizadas:** JUnit 5.
- **Entorno de desarrollo:** Apache NetBeans.
- **Control de versiones:** Git.
- **Repositorio remoto:** GitHub.

## 4. Requisitos del sistema

Para ejecutar el proyecto se requiere:

- JDK 17 o superior.
- Apache Maven.
- Git (opcional, para clonar el repositorio).
- Entorno compatible con Java para ejecutar la aplicación.

## 5. Instalación y ejecución

### Clonar el repositorio

```bash
git clone https://github.com/Gabriel02RD/redsegura.git
```

### Ingresar a la carpeta del proyecto

```bash
cd redsegura
```

### Ejecutar las pruebas automatizadas

```bash
mvn test
```

### Compilar y generar el archivo JAR

```bash
mvn clean package
```

### Ejecutar la aplicación

```bash
java -jar target/redsegura-1.0.0.jar
```

## 6. Arquitectura del sistema

La aplicación utiliza una arquitectura organizada por responsabilidades. La clase principal `RedSeguraApplication` presenta el menú de consola y delega las operaciones al componente central `RedSeguraCore`, que coordina las estructuras de árboles y grafos.

### Diagrama de dependencias

```text
                  RedSeguraApplication
                           |
                           v
                     RedSeguraCore
                    /      |       \
                   v       v        v
                  ABB      AVL   MixedGraph
                                    |
              +---------------------+---------------------+
              |                     |                     |
              v                     v                     v
     GraphRepresentations          BFS                   DFS

                                    |
                                    v
                             DijkstraSolver

                       Vertex, Edge, EdgeType
                         (modelos del grafo)
```

El diagrama representa de forma simplificada la organización de los componentes principales del sistema. Las relaciones específicas se encuentran implementadas en las clases del proyecto.

## 7. Estructura del proyecto

```text
redsegura/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── co/edu/redsegura/
│   │           ├── app/
│   │           ├── core/
│   │           ├── exception/
│   │           ├── graph/
│   │           ├── model/
│   │           └── tree/
│   │
│   └── test/
│       └── java/
│           └── co/edu/redsegura/
│
├── Documentacion_RedSegura/
│   ├── Diagrama_Dependencias_RedSegura.pdf
│   └── Informe_RedSegura.pdf
│
├── Evidencias_RedSegura/
│   ├── 01_Pruebas_JUnit/
│   ├── 02_Compilacion_Maven/
│   ├── 03_Ejecucion_JAR/
│   ├── 04_Consultas_Q1_Q7/
│   └── 05_Repositorio_GitHub/
│
├── pom.xml
├── README.md
└── .gitignore
```

## 8. Funcionalidades principales

El sistema cuenta con un menú de consultas principales y herramientas adicionales.

### Consultas principales

- **Q1. Búsqueda de estaciones:** permite consultar estaciones utilizando un árbol binario de búsqueda y un árbol AVL.
- **Q2. Rotaciones AVL:** demuestra las cuatro rotaciones del árbol AVL: LL, RR, LR y RL.
- **Q3. Representaciones del grafo:** permite visualizar la lista de adyacencia, la matriz de adyacencia y la matriz de incidencia.
- **Q4. Recorridos del grafo:** ejecuta los algoritmos de búsqueda en anchura (BFS) y búsqueda en profundidad (DFS).
- **Q5. Ruta de menor costo:** utiliza el algoritmo de Dijkstra para calcular una ruta entre dos estaciones.
- **Q6. Bloqueo de conexiones:** simula el bloqueo de una conexión y analiza la ruta alternativa disponible.
- **Q7. Manejo de excepciones:** demuestra el manejo controlado de errores al consultar estaciones inexistentes.

### Herramientas adicionales

- **Opción 8:** demostración adicional de estructuras y algoritmos.
- **Opción 9:** consulta de rutas personalizadas.
- **Opción 10:** bloqueo y desbloqueo de conexiones.
- **Opción 11:** modificación del peso de una conexión.
- **Opción 12:** análisis adicional del grafo mediante recorridos, ciclos y componentes.
- **Opción 0:** salida del sistema.

## 9. Decisiones de diseño

- Se utiliza un árbol binario de búsqueda para comparar el comportamiento de las búsquedas.
- Se implementa un árbol AVL para mantener el equilibrio y mejorar la eficiencia de las operaciones.
- Se utiliza un grafo mixto para representar las estaciones y sus conexiones.
- Se separan las responsabilidades en paquetes para facilitar el mantenimiento y la comprensión del código.
- Se incorporan excepciones personalizadas para manejar situaciones inválidas y datos inexistentes.
- Se utilizan pruebas automatizadas con JUnit 5 para verificar el funcionamiento de las estructuras y algoritmos.
- Se emplea Maven para gestionar la construcción del proyecto, las dependencias y la ejecución de las pruebas.

## 10. Complejidad algorítmica

| Estructura o algoritmo | Complejidad |
|---|---|
| Búsqueda en ABB | O(h) |
| Búsqueda en ABB equilibrado | O(log n) promedio |
| Búsqueda en ABB degenerado | O(n) |
| Búsqueda e inserción en AVL | O(log n) |
| Recorridos de árboles | O(n) |
| Lista de adyacencia | O(V + E) para recorrer el grafo |
| Matriz de adyacencia | O(V² + E) para su construcción |
| Matriz de incidencia | O(V × E) |
| BFS | O(V + E) |
| DFS | O(V + E) |
| Dijkstra con cola de prioridad | O((V + E) log V) |

Donde:

- **V:** número de vértices del grafo.
- **E:** número de conexiones o aristas.
- **n:** número de elementos almacenados en el árbol.
- **h:** altura del árbol.

Las complejidades indicadas corresponden a los costos teóricos de las operaciones y algoritmos bajo las representaciones y condiciones señaladas.

## 11. Resultados de ejecución y pruebas

### Resultados de las consultas

- **Q1:** la estación S05 (Hospital Universitario) fue encontrada. El ABB registró 5 pasos y altura 12; el AVL registró 4 pasos y altura 4.
- **Q2:** se verificaron las cuatro rotaciones AVL: LL, RR, LR y RL. La raíz resultante fue 2, el recorrido en preorden fue `[2, 1, 3]` y la altura fue 2.
- **Q3:** se generaron las tres representaciones del grafo con 12 vértices y 18 conexiones.
- **Q4:** se ejecutaron correctamente los recorridos BFS y DFS desde la estación S02.
- **Q5:** Dijkstra encontró una ruta de 24 minutos desde S02 hasta S08: `S02 → S01 → S07 → S08`.
- **Q6:** al bloquear la conexión R16, el sistema encontró una ruta alternativa de 24 minutos: `S02 → S01 → S09 → S10 → S08`. La conexión se desbloqueó al finalizar la simulación.
- **Q7:** la consulta de la estación S99 produjo una excepción controlada y el programa continuó funcionando.

### Pruebas automatizadas

El proyecto cuenta con 72 pruebas automatizadas ejecutadas mediante JUnit 5.

**Resultado de ejecución:**

- Pruebas ejecutadas: 72.
- Fallos: 0.
- Errores: 0.
- Pruebas omitidas: 0.
- Resultado de Maven: `BUILD SUCCESS`.

La compilación y generación del archivo JAR también finalizaron correctamente mediante el comando `mvn clean package`.

## 12. Evidencias y documentación complementaria

### Carpeta Evidencias_RedSegura

Contiene las capturas de pantalla que documentan las pruebas automatizadas, la compilación, la ejecución del archivo JAR, las consultas principales y el repositorio de GitHub.

Su organización es la siguiente:

- `01_Pruebas_JUnit/`
- `02_Compilacion_Maven/`
- `03_Ejecucion_JAR/`
- `04_Consultas_Q1_Q7/`
- `05_Repositorio_GitHub/`

### Carpeta Documentacion_RedSegura

Contiene los documentos complementarios del proyecto:

- `Diagrama_Dependencias_RedSegura.pdf`
- `Informe_RedSegura.pdf`

## 13. Créditos y originalidad

El proyecto RedSegura fue desarrollado individualmente por **Luis Gabriel Rivas Díaz** como trabajo académico de la asignatura Estructura de Datos II.

La implementación aplica estructuras de datos y algoritmos estudiados durante el curso, incluyendo árboles binarios de búsqueda, árboles AVL, grafos mixtos, búsqueda en anchura (BFS), búsqueda en profundidad (DFS) y el algoritmo de Dijkstra.

Las pruebas y los resultados presentados corresponden a la ejecución del proyecto.

---

**Repositorio:** https://github.com/Gabriel02RD/redsegura

**Autor:** Luis Gabriel Rivas Díaz

**Modalidad:** Individual

**Periodo académico:** 2026