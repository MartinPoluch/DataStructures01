package structures;

import java.util.Objects;

public class TreeNode<K extends Comparable<K>, V> implements Comparable<TreeNode<K, V>>{

    private K key;
    private V value;
    private TreeNode<K, V> leftSon;
    private TreeNode<K, V> rightSon;
    private TreeNode<K, V> parent;

    public TreeNode(K key, V value, TreeNode<K, V> parent) {
        this.key = key;
        this.value = value;
        this.parent = parent;
        this.leftSon = null;
        this.rightSon = null;
    }

    public TreeNode<K, V> getParent() {
        return parent;
    }

    private void setParent(TreeNode<K, V> parent) {
        if (parent != this) {
            this.parent = parent;
        }
    }

    public void makeRoot() {
        parent = null;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public TreeNode<K, V> getLeftSon() {
        return leftSon;
    }

    /**
     * Nastavi laveho syna a automaticky o laveho syna zmeni referenciu na noveho rodica
     */
    public void setLeftSon(TreeNode<K, V> leftSon) {
        if (leftSon != this) {
            this.leftSon = leftSon;
            if (leftSon != null) {
                leftSon.setParent(this);
            }
        }
    }

    public TreeNode<K, V> getRightSon() {
        return rightSon;
    }

    /**
     * Nastavi praveho syna a automaticky o laveho syna zmeni referenciu na noveho rodica
     */
    public void setRightSon(TreeNode<K, V> rightSon) {
        if (rightSon != this) {
            this.rightSon = rightSon;
            if (rightSon != null) {
                rightSon.setParent(this);
            }
        }
    }

    public int numberOfSons() {
        if ((leftSon == null) && (rightSon == null)) {
            return 0;
        }
        else if ((leftSon != null) && (rightSon != null)) {
            return 2;
        }
        else {
            return 1;
        }
    }

    public boolean isLeaf() {
        return numberOfSons() == 0;
    }

    public void removeSon(TreeNode<K, V> son) {
        if (son == leftSon) {
            leftSon = null;
        }
        else if (son == rightSon) {
            rightSon = null;
        }
    }

    public boolean isCorrect() {
        if (parent == null && leftSon == null && rightSon == null) {
            return true; // jediny vrchol v stome, nie je co pokazit
        }
        boolean correct = true;
        if (parent != null) { // nie je koren
            correct = ((parent.getLeftSon() == this) || (parent.getRightSon() == this)) && // jeho rodic musi o nom vediet
                      ((leftSon != parent) && (rightSon != parent)); // rodic nemoze byt zaroven aj syn
        }
        if (leftSon == rightSon && leftSon != null) {
            return false; // synovia sa nemozu rovnat
        }
        return (correct &&
                (leftSon != this) &&
                (rightSon != this) &&
                (parent != this));
    }

    public boolean isLeftSon() {
        if (parent != null) {
            return (parent.getLeftSon() == this);
        }
        return false;
    }

    public boolean isRightSon() {
        if (parent != null) {
            return (parent.getRightSon() == this);
        }
        return false;
    }

    @Override
    public int compareTo(TreeNode<K, V> node) {
        return this.key.compareTo(node.key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TreeNode)) {
            return false;
        }
        TreeNode<?, ?> treeNode = (TreeNode<?, ?>) o;
        return Objects.equals(getKey(), treeNode.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getValue(), getLeftSon(), getRightSon());
    }

    @Override
    public String toString() {
        return "(key = " + key + " ,value = " + value + ")";
    }
}
