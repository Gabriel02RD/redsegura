package co.edu.redsegura.tree;

import co.edu.redsegura.exception.InvalidDataException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Árbol AVL (autobalanceado). Implementación propia con rotaciones LL, RR, LR y RL.
 * Altura de un árbol vacío = 0; factor de balance = altura(izq) - altura(der).
 */
public class AvlTree<K extends Comparable<K>, V> {

    private static final class Node<K, V> {
        K key;
        V value;
        int height = 1;
        Node<K, V> left;
        Node<K, V> right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node<K, V> root;
    private int size;
    private boolean inserted;
    private boolean removed;

    private final Map<String, Integer> rotationCounts = new LinkedHashMap<>();
    private final List<String> rotationLog = new ArrayList<>();

    public AvlTree() {
        rotationCounts.put("LL", 0);
        rotationCounts.put("RR", 0);
        rotationCounts.put("LR", 0);
        rotationCounts.put("RL", 0);
    }

    // ------------------------------------------------------------------ insertar

    /** Inserta una clave. Devuelve false (sin modificar el árbol) si la clave ya existe. */
    public boolean insert(K key, V value) {
        requireKey(key);
        inserted = false;
        root = insert(root, key, value);
        if (inserted) {
            size++;
        }
        return inserted;
    }

    private Node<K, V> insert(Node<K, V> node, K key, V value) {
        if (node == null) {
            inserted = true;
            return new Node<>(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value);
        } else {
            return node; // duplicado
        }
        return rebalance(node);
    }

    // ------------------------------------------------------------------ eliminar

    /** Elimina una clave. Devuelve true si existía. */
    public boolean delete(K key) {
        requireKey(key);
        removed = false;
        root = delete(root, key);
        if (removed) {
            size--;
        }
        return removed;
    }

    private Node<K, V> delete(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = delete(node.left, key);
        } else if (cmp > 0) {
            node.right = delete(node.right, key);
        } else {
            removed = true;
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            Node<K, V> successor = minNode(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node.right = delete(node.right, successor.key);
        }
        return rebalance(node);
    }

    private Node<K, V> minNode(Node<K, V> node) {
        Node<K, V> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // ------------------------------------------------------------------ balanceo

    private int heightOf(Node<K, V> node) {
        return (node == null) ? 0 : node.height;
    }

    private void updateHeight(Node<K, V> node) {
        node.height = 1 + Math.max(heightOf(node.left), heightOf(node.right));
    }

    private int balanceOf(Node<K, V> node) {
        return (node == null) ? 0 : heightOf(node.left) - heightOf(node.right);
    }

    private Node<K, V> rotateRight(Node<K, V> y) {
        Node<K, V> x = y.left;
        Node<K, V> middle = x.right;
        x.right = y;
        y.left = middle;
        updateHeight(y); // primero el nodo que baja
        updateHeight(x); // luego el que sube
        return x;
    }

    private Node<K, V> rotateLeft(Node<K, V> x) {
        Node<K, V> y = x.right;
        Node<K, V> middle = y.left;
        y.left = x;
        x.right = middle;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private Node<K, V> rebalance(Node<K, V> node) {
        updateHeight(node);
        int balance = balanceOf(node);

        if (balance > 1) {
            if (balanceOf(node.left) >= 0) {
                logRotation("LL", node.key);
                return rotateRight(node);
            }
            logRotation("LR", node.key);
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1) {
            if (balanceOf(node.right) <= 0) {
                logRotation("RR", node.key);
                return rotateLeft(node);
            }
            logRotation("RL", node.key);
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    private void logRotation(String type, K pivotKey) {
        rotationCounts.merge(type, 1, Integer::sum);
        rotationLog.add(type + " en el nodo " + pivotKey);
    }

    // ------------------------------------------------------------------ consultas

    /** Busca una clave y cuenta los nodos visitados. */
    public SearchResult<V> search(K key) {
        requireKey(key);
        Node<K, V> current = root;
        int steps = 0;
        while (current != null) {
            steps++;
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                return new SearchResult<>(true, current.value, steps);
            }
            current = (cmp < 0) ? current.left : current.right;
        }
        return new SearchResult<>(false, null, steps);
    }

    public List<K> inOrder() {
        List<K> out = new ArrayList<>();
        inOrder(root, out);
        return out;
    }

    private void inOrder(Node<K, V> node, List<K> out) {
        if (node == null) {
            return;
        }
        inOrder(node.left, out);
        out.add(node.key);
        inOrder(node.right, out);
    }

    public List<K> preOrder() {
        List<K> out = new ArrayList<>();
        preOrder(root, out);
        return out;
    }

    private void preOrder(Node<K, V> node, List<K> out) {
        if (node == null) {
            return;
        }
        out.add(node.key);
        preOrder(node.left, out);
        preOrder(node.right, out);
    }

    public List<K> postOrder() {
        List<K> out = new ArrayList<>();
        postOrder(root, out);
        return out;
    }

    private void postOrder(Node<K, V> node, List<K> out) {
        if (node == null) {
            return;
        }
        postOrder(node.left, out);
        postOrder(node.right, out);
        out.add(node.key);
    }

    public int height() {
        return heightOf(root);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public K rootKey() {
        return (root == null) ? null : root.key;
    }

    /** Verifica la invariante AVL: alturas almacenadas correctas y |balance| <= 1 en todos los nodos. */
    public boolean isBalanced() {
        return checkBalance(root) != -1;
    }

    private int checkBalance(Node<K, V> node) {
        if (node == null) {
            return 0;
        }
        int left = checkBalance(node.left);
        if (left == -1) {
            return -1;
        }
        int right = checkBalance(node.right);
        if (right == -1) {
            return -1;
        }
        if (Math.abs(left - right) > 1) {
            return -1;
        }
        int height = 1 + Math.max(left, right);
        return (height == node.height) ? height : -1;
    }

    // ------------------------------------------------------------------ rotaciones (para la demo)

    public int getRotationCount(String type) {
        return rotationCounts.getOrDefault(type, 0);
    }

    public List<String> getRotationLog() {
        return Collections.unmodifiableList(rotationLog);
    }

    private void requireKey(K key) {
        if (key == null) {
            throw new InvalidDataException("La clave no puede ser nula.");
        }
    }
}
