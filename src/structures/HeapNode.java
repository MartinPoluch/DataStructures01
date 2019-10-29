package structures;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * HeapNode riesi problem najlepsej priority pre genericky kluc K.
 * Obsahuje atribut hasBestPriority podla ktoreho sa nody prioritne porovnavaju.
 */
public class HeapNode<K extends Comparable<K>, V> extends TreeNode<K, V> {

    private boolean hasBestPriority;

    public HeapNode(K key, V value, TreeNode<K, V> parent) {
        super(key, value, parent);
    }

    public void setHasBestPriority(boolean hasBestPriority) {
        this.hasBestPriority = hasBestPriority;
    }

    public boolean hasBestPriority() {
        return hasBestPriority;
    }

    public HeapNode<K, V> getHeapParent() {
        TreeNode<K, V> heapParent = this;
        while (heapParent.isRightSon()) {
            heapParent = heapParent.getParent();
        }
        return (HeapNode<K, V>) heapParent.getParent();
    }

    public LinkedList<HeapNode<K, V>> getSons() {
        LinkedList<HeapNode<K, V>> queue = new LinkedList<>();
        HeapNode<K, V> son = (HeapNode<K, V>) this.getLeftSon();
        while (son != null) {
            queue.add(son);
            son = (HeapNode<K, V>) son.getRightSon();
        }
        return queue;
    }

    public int compareTo(HeapNode<K, V> node) {
        if (hasBestPriority) {
            return -1;
        }
        else {
            return super.compareTo(node);
        }
    }

    @Override
    public boolean equals(Object o) {
        return (this == o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hasBestPriority);
    }
}
