package structures;

import java.util.LinkedList;
import java.util.Queue;

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
        //TODO pouzi metodu getSons z heapNodu
        HeapNode<K, V> minNode = root;
        if (minNode != null) {
            HeapNode<K, V> parent = (HeapNode<K, V>) root.getLeftSon();
            if (parent != null) {
                Queue<HeapNode<K, V>> queue = new LinkedList<>();
                while (true) {
                    HeapNode<K, V> rightSon = (HeapNode<K, V>) parent.getRightSon();
                    parent.makeRoot(); // parent = null
                    parent.setRightSon(null);
                    queue.add(parent);
                    if (rightSon == null) {
                        break;
                    }
                    parent = rightSon;
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
            return minNode.getValue();
        }
        else {
            return null;
        }
    }

    public void increasePriority(K newPriority, HeapNode<K, V> node) {
        K oldPriority = node.getKey();
        if (node.hasBestPriority() || (oldPriority.compareTo(newPriority) > 0 && node != root)) {
            node.setKey(newPriority);
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
                root = meld(node, root);
            }
        }
    }

    /**
     * Data (V) si musia pamatat referenciu na haldu.
     */
    public void decreasePriority(K newPriority, HeapNode<K, V> node) {
        K oldPriority = node.getKey();
        if (oldPriority.compareTo(newPriority) < 0) {
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
    }

    public void deleteNode(HeapNode<K, V> node) {
        node.setHasBestPriority(true);
        increasePriority(node.getKey(), node);
        deleteMin();
        node.setHasBestPriority(false);
    }
}
