package structures.tester;

import structures.SplayTree;
import structures.TreeNode;
import java.util.*;

public class SplayTreeTester {

    private List<Integer> usedKeys; // kluce ktore su vo vnutri struktury
    private SplayTree<Integer, String> myTree;
    private TreeMap<Integer, String> javaTree;
    private KeyAndOperationGen generator;
    private boolean check;

    public SplayTreeTester(int numOfInserts, int numOfDeletes, int numOfFounds, boolean check) {
        this(numOfInserts, numOfDeletes, numOfFounds, System.currentTimeMillis());
        this.check = check;
    }

    public SplayTreeTester(int numOfInserts, int numOfDeletes, int numOfFounds) {
        this(numOfInserts, numOfDeletes, numOfFounds, System.currentTimeMillis());
        this.check = true;
    }

    public SplayTreeTester(int numOfInserts, int numOfDeletes, int numOfFounds, long seed) {
        this.myTree = new SplayTree<>();
        this.javaTree = new TreeMap<>();
        this.usedKeys = new ArrayList<>(numOfInserts);
        this.generator = new KeyAndOperationGen(seed);
        generator.generateKeys(numOfInserts);
        generator.generateOperations(numOfInserts, numOfDeletes, numOfFounds);
    }

    private void initialize(int numOfInserts, int numOfDeletes, int numOfFounds, KeyAndOperationGen generator) {
        this.myTree = new SplayTree<>();
        this.javaTree = new TreeMap<>();
        this.usedKeys = new ArrayList<>(numOfInserts);
        this.generator = generator;
        generator.generateKeys(numOfInserts);
        generator.generateOperations(numOfInserts, numOfDeletes, numOfFounds);
    }

    public boolean test() throws DifferentTreesException, WrongImplementationException {
        //initialize(numOfInserts, numOfDeletes, numOfFounds, generator);
        for (Operation  operation : generator.getOperations()) {
            switch (operation) {
                case INSERT: {
                    int key = removeRandomKey(generator.getFreeKeys());
//                    System.out.println("inserted key: " + key);
                    usedKeys.add(key);
                    String value =  Integer.toString(key);
                    myTree.insert(key, value);
                    javaTree.put(key, value);
                    if (check) {
                        checkEquality("insert");
                    }
                    if (! myTree.getRoot().getKey().equals(key)) {
                        throw new WrongImplementationException("After splay operation key " + key + " should be root, but actual root is: " + myTree.getRoot().getKey(), generator.getSeed());
                    }
                    break;
                }
                case DELETE: {
                    if (!usedKeys.isEmpty()) {
                        int key = removeRandomKey(usedKeys);
//                        System.out.println("deleted key: " + key);
                        String myRemovedValue = myTree.remove(key);
                        String javaRemovedValue = javaTree.remove(key);
                        if (! myRemovedValue.equals(javaRemovedValue)) {
                            throw new DifferentTreesException("Wrong value after operation delete!\nexpected= " + javaRemovedValue + "\nactual = " + myRemovedValue, generator.getSeed());
                        }
                        if (check) {
                            checkEquality("delete");
                        }
                    }
                    else {
                        if (myTree.getSize() == 0) {
                            int randomKey = generator.getGenerator().nextInt(1000);
                            String nullValue = myTree.remove(randomKey);
                            if (nullValue != null) {
                                throw new WrongImplementationException("When delete from empty tree return type should be null",generator.getSeed());
                            }
                        }
                        else {
                            throw new WrongImplementationException("Tree should be empty", generator.getSeed());
                        }
                    }
                    break;
                }
                case FIND: {
                    if (! usedKeys.isEmpty()) {
                        int randomIndex = generator.getGenerator().nextInt(usedKeys.size());
                        int key = usedKeys.get(randomIndex);
                        String myFoundValue = myTree.find(key);
                        String javaFoundValue = javaTree.get(key);
                        if (! myFoundValue.equals(javaFoundValue)) {
                            throw new DifferentTreesException("Wrong value after operation find!\nexpected= " + javaFoundValue + "\nactual = " + myFoundValue, generator.getSeed());
                        }
                        if (! myTree.getRoot().getKey().equals(key)) {
                            throw new WrongImplementationException("After splay operation key " + key + " should be root, but actual root is: " + myTree.getRoot().getKey(), generator.getSeed());
                        }
                    }
                    else {
                        if (myTree.getSize() == 0) {
                            int randomKey = generator.getGenerator().nextInt(1000);
                            String nullValue = myTree.find(randomKey);
                            if (nullValue != null) {
                                throw new WrongImplementationException("When you tried find key in empty tree return type should be null", generator.getSeed());
                            }
                        }
                        else {
                            throw new WrongImplementationException("Tree should be empty", generator.getSeed());
                        }
                    }
                    break;
                }
            }
            //System.out.println(myTree.getSize());
        }
        checkEquality("end");
        return true;
    }

    private int removeRandomKey(List<Integer> keys) {
        int index = generator.getGenerator().nextInt(keys.size());
        return keys.remove(index);
    }

    private void checkEquality(String operation) throws DifferentTreesException, WrongImplementationException {
        List<TreeNode<Integer, String>> myInOrder = myTree.inOrder();
        if (myInOrder.size() != myTree.getSize()) {
            throw new WrongImplementationException("InOrder size and getSize() are not equal", generator.getSeed());
        }

        if (myInOrder.size() != javaTree.size()) {
            throw new DifferentTreesException("Number of nodes is different!" +
                    "\nexpected number of nodes: " + javaTree.size() +
                    "\nactual number of nodes: " + myInOrder.size() +
                    "\nseed = " + generator.getSeed() +
                    "\nreason = " + operation
                    , generator.getSeed());
        }


        int i = 0;
        for (Map.Entry<Integer, String> javaNode : javaTree.entrySet()) {
            TreeNode<Integer, String> myNode = myInOrder.get(i++);
            if (! javaNode.getKey().equals(myNode.getKey()) || ! javaNode.getValue().equals(myNode.getValue())) {
                String expected = "expected: (key = " + javaNode.getKey() + ", value = " + javaNode.getValue() + ")";
                String actual = "\nactual: (key = " + myNode.getKey() + ", value = " + myNode.getValue() + ")";
                throw new DifferentTreesException("Nodes are not equal!\\n" +
                        expected +
                        actual +
                        "\nInOrder index: " + (i - 1) +
                        "\nseed = " + generator.getSeed() +
                        "\nreason = " + operation
                        , generator.getSeed());
            }
            else if (! myNode.isCorrect()) {
                throw new WrongImplementationException("Internal structure of node: " + myNode + " is incorect.\nseed = " + generator.getSeed(), generator.getSeed());
            }
        }
    }
}
