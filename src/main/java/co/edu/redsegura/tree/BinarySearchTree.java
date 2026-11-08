package co.edu.redsegura.tree;

import co.edu.redsegura.exception.InvalidDataException;
import java.util.ArrayList;
import java.util.List;

/**
 * Árbol binario de búsqueda sin balanceo. Implementación propia.
 * Convención: la altura de un árbol vacío es 0 y la de un árbol con un solo nodo es 1.
 */
public class BinarySearchTree<K extends Comparable<K>, V> {

    private static final class Node<K, V> {
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node<K, V> root;
    private int size;

    /** Inserta una clave. Devuelve false (sin modificar el árbol) si la clave ya existe. */
    public boolean insert(K key, V value) {
        requireKey(key);
        if (root == null) {
            root = new Node<>(key, value);
            size++;
            return true;
        }
        Node<K, V> current = root;
        while (true) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                return false;
            }
            if (cmp < 0) {
                if (current.left == null) {
                    current.left = new Node<>(key, value);
                    size++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node<>(key, value);
                    size++;
                    return true;
                }
                current = current.right;
            }
        }
    }

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

    /** Elimina una clave. Devuelve true si existía. */
    public boolean delete(K key) {
        requireKey(key);
        int before = size;
        root = delete(root, key);
        return size < before;
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
            if (node.left == null) {
                size--;
                return node.right;
            }
            if (node.right == null) {
                size--;
                return node.left;
            }
            // Dos hijos: se reemplaza por el sucesor in-order (mínimo del subárbol derecho).
            Node<K, V> successor = minNode(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node.right = delete(node.right, successor.key);
        }
        return node;
    }

    private Node<K, V> minNode(Node<K, V> node) {
        Node<K, V> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
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
        return height(root);
    }

    private int height(Node<K, V> node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /** Clave de la raíz o null si el árbol está vacío. */
    public K rootKey() {
        return (root == null) ? null : root.key;
    }

    private void requireKey(K key) {
        if (key == null) {
            throw new InvalidDataException("La clave no puede ser nula.");
        }
    }
}
