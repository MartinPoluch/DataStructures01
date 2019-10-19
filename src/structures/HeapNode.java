package structures;

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

    @Override
    public int compareTo(TreeNode<K, V> node) {
        if (hasBestPriority) {
            return 1;
        }
        else {
            return super.compareTo(node);
        }
    }
}
