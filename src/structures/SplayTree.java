package structures;

import java.util.*;
import java.util.List;

public class SplayTree<K extends Comparable<K>, V> {

    private TreeNode<K, V> root;
    private int size;

    public SplayTree() {
        this.root = null;
        this.size = 0;
    }

    public TreeNode<K, V> getRoot() {
        return root;
    }

    private void rotate(TreeNode<K, V> parent, TreeNode<K, V> node) {
        TreeNode<K, V> grandParent = parent.getParent();
        if (grandParent != null) {
            if (parent.isLeftSon()) {
                grandParent.setLeftSon(node);
            }
            else {
                grandParent.setRightSon(node);
            }
        }
        else { // kedze node nema prarodica, tak to znamena ze jeho rodic je root, po rotacii bude node novy root
            root = node;
            node.makeRoot();
        }
    }

    private void rotateToRight(TreeNode<K, V> node) {
        if (node.isLeftSon()) {
            TreeNode<K, V> right = node.getRightSon();
            node.setRightSon(null);
            TreeNode<K, V> parent = node.getParent();
            parent.setLeftSon(null);
            rotate(parent, node);
            parent.setLeftSon(right);
            node.setRightSon(parent);
        }
    }

    private void rotateToLeft(TreeNode<K, V> node) {
        if (node.isRightSon()) {
            TreeNode<K, V> left = node.getLeftSon();
            node.setLeftSon(null);
            TreeNode<K, V> parent = node.getParent();
            parent.setRightSon(null);
            rotate(parent, node);
            parent.setRightSon(left);
            node.setLeftSon(parent);
        }
    }

    private void splay(TreeNode<K, V> node) {
        if (node == null) {
            return;
        }

        while (node != root) {
            TreeNode<K, V> parent = node.getParent();
            if (parent == root) {
                if (node.isLeftSon()) {
                    rotateToRight(node);
                }
                else {
                    rotateToLeft(node);
                }
            }
            else if (node.isLeftSon() && parent.isLeftSon()) {
                rotateToRight(parent);
                rotateToRight(node);
            }
            else if (node.isRightSon() && parent.isRightSon()) {
                rotateToLeft(parent);
                rotateToLeft(node);
            }
            else {
                if (node.isLeftSon()) {
                    rotateToRight(node);
                    rotateToLeft(node);
                }
                else {
                    rotateToLeft(node);
                    rotateToRight(node);
                }
            }
        }
    }

    /**
     * Pokusi sa najist vrchol s danym klucom. Ak najde vrchol tak do lastNode ulozi prislusny vrchol,
     * inak do lastNode ulozi posledny prehladany vrchol (list).
     * @param key hladany kluc
     * @return ak existuje vrchol s danym klucom tak vrati true, inak false
     */
    private TreeNode<K, V> findLastNode(K key) {
        TreeNode<K, V> lastNode = null;
        if (root == null) {
            return null;
        }
        else {
            lastNode = root;
            while (!lastNode.getKey().equals(key) && !lastNode.isLeaf()) {
                if (key.compareTo(lastNode.getKey()) < 0) {
                    if (lastNode.getLeftSon() != null) {
                        lastNode = lastNode.getLeftSon();
                    }
                    else {
                        return lastNode;
                    }
                }
                else {
                    if (lastNode.getRightSon() != null) {
                        lastNode = lastNode.getRightSon();
                    }
                    else {
                        return lastNode;
                    }
                }
            }
        }
        return lastNode;
    }

    public V find(K key) {
        TreeNode<K, V> lastNode = findLastNode(key);
        splay(lastNode);
        if ((lastNode != null) && (lastNode.getKey().equals(key))) {
            return lastNode.getValue();
        }
        else {
            return null;
        }
    }

    public V findOrInsert(K key, V value) {
        TreeNode<K, V> insertedNode = new TreeNode<>(key, value, null);
        if (root == null) {
            root = insertedNode;
        }
        else {
            TreeNode<K, V> parent = findLastNode(key);
            if (parent != null) { // nikdy by to nemalo byt null, if len preto aby nevypisovalo warning
                if (parent.getKey().equals(key)) {
                    return parent.getValue();
                }
                else if (key.compareTo(parent.getKey()) < 0) {
                    parent.setLeftSon(insertedNode);
                }
                else {
                    parent.setRightSon(insertedNode);
                }
            }
        }
        size++;
        splay(insertedNode);
        return insertedNode.getValue();
    }

    public TreeNode<K, V> findNode(K key) {
        return null;
    }

    public boolean insert(K key, V value) {
        TreeNode<K, V> insertedNode = new TreeNode<>(key, value, null);
        if (root == null) {
            root = insertedNode;
        }
        else {
            TreeNode<K, V> parent = findLastNode(key);
            if (parent != null) { // nikdy by to nemalo byt null, if len preto aby nevypisovalo warning
                if (parent.getKey().equals(key)) {
                    return false; // zadany kluc uz existuje
                }
                else if (key.compareTo(parent.getKey()) < 0) {
                    parent.setLeftSon(insertedNode);
                }
                else {
                    parent.setRightSon(insertedNode);
                }
            }
        }
        size++;
        splay(insertedNode);
        return true; // prvok bol pridany
    }

    public V remove(K key) {
        TreeNode<K, V> node = findLastNode(key);
        boolean keyExist = ((node != null) && node.getKey().equals(key));
        if (keyExist) {
            int numOfSons = node.numberOfSons();
            TreeNode<K, V> parent = node.getParent();
            if (numOfSons == 0) {
                if (parent == null) { // v strome je jediny prvok
                    root = null;
                }
                else {
                    parent.removeSon(node);
                }
            }
            else if (numOfSons == 1) {
                TreeNode<K, V> son = (node.getLeftSon() == null) ? node.getRightSon() : node.getLeftSon(); // node ma prave jedneho syna, zistime ktoreho
                removeNodeWith1Son(node, son);
            }
            else { //numOfSons == 2
                removeNodeWith2Sons(node);
            }
            size--;
            splay(parent);
            return node.getValue();
        }
        else {
            splay(node);
            return null;
        }
    }

    private void removeNodeWith1Son(TreeNode<K, V> deletedNode, TreeNode<K, V> replaceNode) {
        TreeNode<K, V> parent = deletedNode.getParent();
        if (parent == null) { // odstanujeme koren
            root = replaceNode;
            replaceNode.makeRoot();
        }
        else {
            if (deletedNode.isLeftSon()) {
                parent.setLeftSon(replaceNode);
            }
            else {
                parent.setRightSon(replaceNode);
            }
        }
    }

    private void removeNodeWith2Sons(TreeNode<K, V> node) {
        TreeNode<K, V> inOrderFollower = node.getRightSon();
        while (inOrderFollower.getLeftSon() != null) {
            inOrderFollower = inOrderFollower.getLeftSon();
        }

        TreeNode<K, V> inOrderFollowerParent = inOrderFollower.getParent();
        if (inOrderFollower.isLeftSon()) {
            inOrderFollowerParent.setLeftSon(inOrderFollower.getRightSon());
        }
        else {
            inOrderFollowerParent.setRightSon(inOrderFollower.getRightSon());
        }

        removeNodeWith1Son(node, inOrderFollower);
        inOrderFollower.setLeftSon(node.getLeftSon());
        inOrderFollower.setRightSon(node.getRightSon());
    }

    public int getSize() {
        return size;
    }


    public List<TreeNode<K, V>> levelOrder() {
        List<TreeNode<K, V>> orderedNodes = new ArrayList<>(size);
        LinkedList<TreeNode<K, V>> queue = new LinkedList<>();
        queue.addFirst(root);
        while (! queue.isEmpty()) {
            TreeNode<K, V> actual = queue.removeLast();
            orderedNodes.add(actual);
            if (actual.getLeftSon() != null) {
                queue.addFirst(actual.getLeftSon());
            }
            if (actual.getRightSon() != null) {
                queue.addFirst(actual.getRightSon());
            }
        }
        return orderedNodes;
    }

    public List<TreeNode<K, V>> inOrder() {
        List<TreeNode<K, V>> orderedNodes = new ArrayList<>(size);
        Stack<TreeNode<K, V>> stack = new Stack<>();
        TreeNode<K, V> node = root;
        while (! stack.isEmpty() || node != null) {
            if (node != null) {
                stack.push(node);
                node = node.getLeftSon();
            }
            else {
                node = stack.pop();
                orderedNodes.add(node);
                node = node.getRightSon();
            }
        }
        return orderedNodes;
    }

    public void inOrder(List<V> data) {
        Stack<TreeNode<K, V>> stack = new Stack<>();
        TreeNode<K, V> node = root;
        while (! stack.isEmpty() || node != null) {
            if (node != null) {
                stack.push(node);
                node = node.getLeftSon();
            }
            else {
                node = stack.pop();
                data.add(node.getValue());
                node = node.getRightSon();
            }
        }
    }

    /**
     * Vrati prve data ktorych kluc je vacsi ako kluc zadany ako parameter.
     */
    public V findFirstBiggerValue(K key) {
        Stack<TreeNode<K, V>> stack = new Stack<>();
        TreeNode<K, V> node = root;
        while (! stack.isEmpty() || node != null) {
            if (node != null) {
                stack.push(node);
                node = node.getLeftSon();
            }
            else {
                node = stack.pop();
                if (node.getKey().compareTo(key) >= 0) {
                    return node.getValue();
                }
                node = node.getRightSon();
            }
        }
        return null;
    }

}
