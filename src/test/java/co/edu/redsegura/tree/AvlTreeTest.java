package co.edu.redsegura.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class AvlTreeTest {

    private AvlTree<Integer, String> build(int... keys) {
        AvlTree<Integer, String> tree = new AvlTree<>();
        for (int k : keys) {
            tree.insert(k, "v" + k);
        }
        return tree;
    }

    @Test
    void rotacionLL() {
        AvlTree<Integer, String> t = build(3, 2, 1);
        assertEquals(1, t.getRotationCount("LL"));
        assertEquals(Integer.valueOf(2), t.rootKey());
        assertTrue(t.isBalanced());
    }

    @Test
    void rotacionRR() {
        AvlTree<Integer, String> t = build(1, 2, 3);
        assertEquals(1, t.getRotationCount("RR"));
        assertEquals(Integer.valueOf(2), t.rootKey());
        assertTrue(t.isBalanced());
    }

    @Test
    void rotacionLR() {
        AvlTree<Integer, String> t = build(3, 1, 2);
        assertEquals(1, t.getRotationCount("LR"));
        assertEquals(Integer.valueOf(2), t.rootKey());
        assertTrue(t.isBalanced());
    }

    @Test
    void rotacionRL() {
        AvlTree<Integer, String> t = build(1, 3, 2);
        assertEquals(1, t.getRotationCount("RL"));
        assertEquals(Integer.valueOf(2), t.rootKey());
        assertTrue(t.isBalanced());
    }

    @Test
    void insercionSecuencialMantieneBalanceYAlturaLogaritmica() {
        AvlTree<Integer, String> t = new AvlTree<>();
        for (int i = 1; i <= 1000; i++) {
            t.insert(i, "x");
        }
        assertTrue(t.isBalanced());
        assertEquals(1000, t.size());
        assertTrue(t.height() <= 14); // 1.44 * log2(1000) ~ 14.4
    }

    @Test
    void conjuntoDelParcialQuedaConRaizS08() {
        AvlTree<String, String> t = new AvlTree<>();
        for (int i = 1; i <= 12; i++) {
            String key = String.format("S%02d", i);
            t.insert(key, key);
        }
        assertEquals("S08", t.rootKey());
        assertEquals(4, t.search("S05").steps());
        assertTrue(t.isBalanced());
    }

    @Test
    void eliminarProvocaRotacionRR() {
        AvlTree<Integer, String> t = build(2, 1, 3, 4);
        assertTrue(t.delete(1));
        assertEquals(Integer.valueOf(3), t.rootKey());
        assertEquals(List.of(2, 3, 4), t.inOrder());
        assertTrue(t.isBalanced());
    }

    @Test
    void eliminarNodoConDosHijos() {
        AvlTree<Integer, String> t = build(5, 3, 8, 2, 4, 7, 9);
        assertTrue(t.delete(5));
        assertEquals(Integer.valueOf(7), t.rootKey());
        assertEquals(List.of(2, 3, 4, 7, 8, 9), t.inOrder());
        assertTrue(t.isBalanced());
    }

    @Test
    void eliminarMuchasClavesMantieneBalanceYOrden() {
        AvlTree<Integer, String> t = new AvlTree<>();
        List<Integer> expected = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            t.insert(i, "x");
            expected.add(i);
        }
        for (int i = 1; i <= 200; i += 3) {
            assertTrue(t.delete(i));
            expected.remove(Integer.valueOf(i));
            assertTrue(t.isBalanced());
        }
        assertEquals(expected, t.inOrder());
        assertEquals(expected.size(), t.size());
    }

    @Test
    void duplicadosEInexistentes() {
        AvlTree<Integer, String> t = build(10, 20);
        assertFalse(t.insert(10, "otro"));
        assertEquals(2, t.size());
        assertFalse(t.delete(99));
        assertFalse(t.search(99).found());
    }

    @Test
    void arbolVacio() {
        AvlTree<Integer, String> t = new AvlTree<>();
        assertTrue(t.isEmpty());
        assertEquals(0, t.height());
        assertTrue(t.isBalanced());
        assertTrue(t.inOrder().isEmpty());
    }

    @Test
    void recorridosDelArbolBalanceado() {
        AvlTree<Integer, String> t = build(1, 2, 3, 4, 5, 6, 7);
        assertEquals(List.of(4, 2, 1, 3, 6, 5, 7), t.preOrder());
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7), t.inOrder());
        assertEquals(List.of(1, 3, 2, 5, 7, 6, 4), t.postOrder());
    }
}
