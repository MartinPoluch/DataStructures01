package structures.tester;

import structures.PairingHeap;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class PairingHeapTester {

    private PairingHeap<Integer, Integer> myHeap;
    private PriorityQueue<Integer> javaHeap;
    private KeyAndOperationGen generator;

    public PairingHeapTester(int numOfInserts, int numOfDeletes, int numOfFounds) {
        this(numOfInserts, numOfDeletes, numOfFounds, System.currentTimeMillis());
    }

    public PairingHeapTester(int numOfInserts, int numOfDeletes, int numOfFounds, long seed) {
        this.myHeap = new PairingHeap<Integer, Integer>();
        this.javaHeap = new PriorityQueue<>();
        this.generator = new KeyAndOperationGen(seed);
        generator.generateKeys(numOfInserts);
        generator.generateOperations(numOfInserts, numOfDeletes, numOfFounds);
    }


    public boolean test() throws DifferentTreesException, WrongImplementationException {
        //TODO dorobit testovanie metod inccreaseKey, decreseKey and removeNode
        //initialize(numOfInserts, numOfDeletes, numOfFounds, generator);
        for (Operation  operation : generator.getOperations()) {
            switch (operation) {
                case INSERT: {
                    Integer key = removeRandomKey(generator.getFreeKeys());
//                    System.out.println("inserted key: " + key);
                    myHeap.insert(key, key);
                    javaHeap.add(key);
                    checkEquality("insert");
                    break;
                }
                case DELETE: {
                    Integer myVal = myHeap.deleteMin();
                    Integer javaVal = javaHeap.poll();
                    if (! myVal.equals(javaVal)) {
                        throw new WrongImplementationException("Deleted different minimal elements", generator.getSeed());
                    }
                    checkEquality("delete");
                    break;
                }
                case FIND: {
                    Integer myVal = myHeap.findMin();
                    Integer javaVal = javaHeap.peek();
                    if (! myVal.equals(javaVal)) {
                        throw new WrongImplementationException("Found different minimal elements", generator.getSeed());
                    }
                    checkEquality("delete");
                    break;
                }
            }
            //System.out.println(myTree.getSize());
        }
        return true;
    }


    private int removeRandomKey(List<Integer> keys) {
        int index = generator.getGenerator().nextInt(keys.size());
        return keys.remove(index);
    }

    private void checkEquality(String operation) throws DifferentTreesException, WrongImplementationException {
        if (myHeap.getSize() != javaHeap.size()) {
            throw new WrongImplementationException("Sizes of heaps are different for operation " + operation, generator.getSeed());
        }

        if (!myHeap.findMin().equals(javaHeap.peek())) {
            throw new WrongImplementationException("Min elements are difference for operation " + operation, generator.getSeed());
        }
    }
}
