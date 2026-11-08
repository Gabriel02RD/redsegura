package co.edu.redsegura.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.redsegura.exception.InvalidDataException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BinarySearchTreeTest {

    private BinarySearchTree<Integer, String> tree;

    @BeforeEach
    void setUp() {
        tree = new BinarySearchTree<>();
        for (int k : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.insert(k, "v" + k);
        }
    }

    @Test
    void recorridos() {
        assertEquals(List.of(20, 30, 40, 50, 60, 70, 80), tree.inOrder());
        assertEquals(List.of(50, 30, 20, 40, 70, 60, 80), tree.preOrder());
        assertEquals(List.of(20, 40, 30, 60, 80, 70, 50), tree.postOrder());
    }

    @Test
    void busquedaCuentaPasos() {
        SearchResult<String> r = tree.search(40);
        assertTrue(r.found());
        assertEquals("v40", r.value());
        assertEquals(3, r.steps());
        assertFalse(tree.search(99).found());
    }

    @Test
    void insertarDuplicadoNoModificaElArbol() {
        assertFalse(tree.insert(50, "otro"));
        assertEquals(7, tree.size());
        assertEquals("v50", tree.search(50).value());
    }

    @Test
    void alturaDeArbolVacioYDegenerado() {
        BinarySearchTree<Integer, String> empty = new BinarySearchTree<>();
        assertEquals(0, empty.height());
        assertTrue(empty.isEmpty());
        BinarySearchTree<Integer, String> chain = new BinarySearchTree<>();
        for (int i = 1; i <= 5; i++) {
            chain.insert(i, "x");
        }
        assertEquals(5, chain.height());
    }

    @Test
    void eliminarHoja() {
        assertTrue(tree.delete(20));
        assertEquals(List.of(30, 40, 50, 60, 70, 80), tree.inOrder());
        assertEquals(6, tree.size());
    }

    @Test
    void eliminarNodoConUnHijo() {
        tree.delete(20);
        assertTrue(tree.delete(30)); // ahora solo tiene el hijo derecho 40
        assertEquals(List.of(40, 50, 60, 70, 80), tree.inOrder());
    }

    @Test
    void eliminarNodoConDosHijosYRaiz() {
        assertTrue(tree.delete(50));
        assertEquals(Integer.valueOf(60), tree.rootKey());
        assertEquals(List.of(20, 30, 40, 60, 70, 80), tree.inOrder());
        assertEquals(6, tree.size());
    }

    @Test
    void eliminarClaveInexistenteDevuelveFalse() {
        assertFalse(tree.delete(999));
        assertEquals(7, tree.size());
    }

    @Test
    void claveNulaSeRechaza() {
        assertThrows(InvalidDataException.class, () -> tree.insert(null, "x"));
        assertThrows(InvalidDataException.class, () -> tree.search(null));
    }

    @Test
    void arbolVacioNoFallaEnRecorridos() {
        BinarySearchTree<Integer, String> empty = new BinarySearchTree<>();
        assertTrue(empty.inOrder().isEmpty());
        assertFalse(empty.search(1).found());
        assertFalse(empty.delete(1));
    }
}
