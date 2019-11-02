package structures;

import java.util.*;

/**
 * Pri testovani, prejdi cez level order a zisti ci splna vlastnosti haldy.
 * @param <K>
 * @param <V>
 */
public class PairingHeap<K extends Comparable<K>, V> {

    private HeapNode<K, V> root;
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
    private HeapNode<K, V> meld(HeapNode<K, V> node1, HeapNode<K, V> node2) {
        if ((node1 != null) && (node2 != null)) {
            HeapNode<K, V> higherPriority;
            HeapNode<K, V> lowerPriority;
            if (node1.compareTo(node2) < 0) {
                higherPriority = node1;
                lowerPriority = node2;
            }
            else {
                higherPriority = node2;
                lowerPriority = node1;
            }

            HeapNode<K, V> oldLeftSon = (HeapNode<K, V>) higherPriority.getLeftSon();
            higherPriority.setLeftSon(lowerPriority);
            lowerPriority.setRightSon(oldLeftSon);
            return higherPriority;
        }
        return null;
    }

    public HeapNode<K, V> insert(K priority, V value) {
        if ((priority == null) || (value == null)) {
            return null;
        }
        HeapNode<K, V> insertedNode = new HeapNode<>(priority, value, null);
        if (root == null) {
            root = insertedNode;
        }
        else {
            root = meld(insertedNode, root);
        }
        size++;
        return insertedNode;
    }

    public V findMin() {
        return (root == null) ? null : root.getValue();
    }

    /**
     * Priorita nie je unikatna. Ak maju prvky rovnaku priority tak sa vyberie prvok ktory sa ako prvy vkladal (FIFO).
     */
    public V deleteMin() {
        HeapNode<K, V> deletedNode = deleteMinNode();
        return (deletedNode == null) ? null : deletedNode.getValue();
    }

    public HeapNode<K, V> deleteMinNode() {
        HeapNode<K, V> minNode = root;
        if (minNode != null) { // ak root je null nie je co zmazat
            HeapNode<K, V> son = (HeapNode<K, V>) root.getLeftSon();
            if (son != null) {
                Queue<HeapNode<K, V>> queue = new LinkedList<>();
                while (true) {
                    HeapNode<K, V> rightSon = (HeapNode<K, V>) son.getRightSon();
                    son.makeRoot(); // parent = null
                    son.setRightSon(null);
                    queue.add(son);
                    if (rightSon == null) {
                        break;
                    }
                    son = rightSon;
                }

                while (queue.size() > 1) {
                    HeapNode<K, V> heap1 = queue.poll();
                    HeapNode<K, V> heap2 = queue.poll();
                    HeapNode<K, V> mergedHeap = meld(heap1, heap2);
                    queue.add(mergedHeap);
                }
                root = queue.poll();
            }
            else {
                root = null; // bol odstraneny posledny prvok haldy
            }
            size--;
            return minNode;
        }
        else {
            return null;
        }
    }


    public void increasePriority(K newPriority, HeapNode<K, V> node) {
        if (node == null) {
            return;
        }

        node.setKey(newPriority);
        if (node != root) {
            HeapNode<K, V> heapParent = node.getHeapParent();
            if (heapParent != null && node.compareTo(heapParent) < 0) {
                HeapNode<K, V> directParent = (HeapNode<K, V>) node.getParent();
                if (node.isRightSon()) {
                    directParent.setRightSon(node.getRightSon());
                }
                else {
                    directParent.setLeftSon(node.getRightSon());
                }
                node.makeRoot();
                node.setRightSon(null);
                root = meld(node, root);
            }
        }
    }

    /**
     * Data (V) si musia pamatat referenciu na haldu.
     */
    public void decreasePriority(K newPriority, HeapNode<K, V> node) {
        node.setKey(newPriority);
        LinkedList<HeapNode<K, V>> sons = node.getSons();
        boolean ok = true;
        for (HeapNode<K, V> son : sons) {
            if (node.compareTo(son) > 0) {
                ok = false;
                break;
            }
        }

        if (ok) {
            return;
        }
        HeapNode<K, V> parent = (HeapNode<K, V>) node.getParent();
        HeapNode<K, V> rightSon = (HeapNode<K, V>) node.getRightSon();
        boolean isRightSon = node.isRightSon();

        node.setLeftSon(null);
        sons.addFirst(node);

        while (sons.size() > 1) {
            HeapNode<K, V> son1 = sons.removeFirst();
            son1.makeRoot();
            son1.setRightSon(null);

            HeapNode<K, V> son2 = sons.removeFirst();
            son2.makeRoot();
            son2.setRightSon(null);

            HeapNode<K, V> heap = meld(son1, son2);
            sons.addLast(heap);
        }

        HeapNode<K, V> replaceNode = sons.removeFirst();
        if (parent == null) {
            root = replaceNode;
        }
        else {
            if (isRightSon) {
                parent.setRightSon(replaceNode);
            }
            else {
                parent.setLeftSon(replaceNode);
            }
            replaceNode.setRightSon(rightSon);
        }
    }

    public void deleteNode(HeapNode<K, V> node) {
        node.setHasBestPriority(true);
        increasePriority(node.getKey(), node);
        deleteMin();
        node.setHasBestPriority(false);
    }

    public int getTrueSize() {
        int realSize = 0;
        Stack<HeapNode<K, V>> stack = new Stack<>();
        HeapNode<K, V> node = root;
        while (! stack.isEmpty() || node != null) {
            if (node != null) {
                stack.push(node);
                node = (HeapNode<K, V>) node.getLeftSon();
            }
            else {
                node = stack.pop();
                realSize++;
                node = (HeapNode<K, V>) node.getRightSon();
            }
        }
        return realSize;
    }
}
