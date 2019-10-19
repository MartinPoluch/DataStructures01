package structures;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Pri testovani, prejdi cez level order a zisti ci splna vlastnosti haldy.
 * @param <K>
 * @param <V>
 */
public class PairingHeap<K extends Comparable<K>, V> {

    private TreeNode<K, V> root;
    private int size;

    public PairingHeap() {
        this.root = null;
        this.size = 0;
    }

    public int getSize() {
        return size;
    }

    /**
     * Operacia paruj.
     */
    private TreeNode<K, V> meld(TreeNode<K, V> node1, TreeNode<K, V> node2) {
        if ((node1 != null) && (node2 != null)) {
            TreeNode<K, V> higherPriority;
            TreeNode<K, V> lowerPriority;
            if (node1.getKey().compareTo(node2.getKey()) < 0) {
                higherPriority = node1;
                lowerPriority = node2;
            }
            else {
                higherPriority = node2;
                lowerPriority = node1;
            }

            TreeNode<K, V> oldLeftSon = higherPriority.getLeftSon();
            higherPriority.setLeftSon(lowerPriority);
            lowerPriority.setRightSon(oldLeftSon);
            return higherPriority;
        }
        return null;
    }

    public void insert(K priority, V value) {
        if ((priority == null) || (value == null)) {
            return;
        }
        TreeNode<K, V> insertedNode = new TreeNode<>(priority, value, null);
        if (root == null) {
            root = insertedNode;
        }
        else {
            root = meld(insertedNode, root);
        }
        size++;
    }

    public V findMin() {
        return (root == null) ? null : root.getValue();
    }

    /**
     * Priorita nie je unikatna. Ak maju prvky rovnaku priority tak sa vyberie prvok ktory sa ako prvy vkladal (FIFO).
     */
    public V deleteMin() {
        TreeNode<K, V> minNode = root;
        if (minNode != null) {
            TreeNode<K, V> parent = root.getLeftSon();
            if (parent != null) {
                Queue<TreeNode<K, V>> queue = new LinkedList<>();
                while (true) {
                    TreeNode<K, V> rightSon = parent.getRightSon();
                    parent.makeRoot(); // parent = null
                    parent.setRightSon(null);
                    queue.add(parent);
                    if (rightSon == null) {
                        break;
                    }
                    parent = rightSon;
                }

                while (queue.size() > 1) {
                    TreeNode<K, V> heap1 = queue.poll();
                    TreeNode<K, V> heap2 = queue.poll();
                    TreeNode<K, V> mergedHeap = meld(heap1, heap2);
                    queue.add(mergedHeap);
                }
                root = queue.poll();
            }
            else {
                root = null; // bol odstraneny posledny prvok haldy
            }
            size--;
            return minNode.getValue();
        }
        else {
            return null;
        }
    }

    public void increasePriority(K newPriority, TreeNode<K, V> node) {
//        K oldPriority = node.getKey();
//        if (oldPriority.compareTo(newPriority) < 0) { // skontrolujem validnost parametrov, ci naozaj nova priorita bude mensia hodnota ako stara priorita
//            node.setKey(newPriority);
//            if (node != root) { // ak node je root tak nie je co pokazit, nemoze sa stat ze by jeho nova priorita bola lepsia ako priorita jeho rodica (lebo nema rodica)
//                TreeNode<K, V> parent = node.getParent();
//                boolean changeNeeded = (parent.getKey().compareTo(newPriority) < 0);
//                if (changeNeeded) {
//                    if (node.isLeftSon()) {
//                        parent.setLeftSon(null);
//                    }
//                    else {
//                        parent.setRightSon(null);
//                    }
//                    node.makeRoot(); // nastavim mu rodica na null
//                    root = meld(node, root);
//                }
//            }
//        }
        //TODO
    }

    /**
     * Data (V) si musia pamatat referenciu na haldu.
     */
    public void decresePriority(K newPriority, TreeNode<K, V> node) {
        K oldPriority = node.getKey();
        //TODO
    }



    public void deleteNode(HeapNode<K, V> node) {
        node.setHasBestPriority(true);
        increasePriority(node.getKey(), node);
        deleteMin();
        node.setHasBestPriority(false);
    }
}
