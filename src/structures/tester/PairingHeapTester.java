package structures.tester;

import structures.HeapNode;
import structures.PairingHeap;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class PairingHeapTester {

    private PairingHeap<Integer, Integer> myHeap;
    private PriorityQueue<Integer> javaHeap;
    private KeyAndOperationGen generator;
    private List<HeapNode<Integer, Integer>> usedNodes;

    public PairingHeapTester(int numOfInserts, int numOfPops, int numOfIncreases, int numOfDecreases, int namOfDeletes) {
        this(numOfInserts, numOfPops, numOfIncreases, numOfDecreases, namOfDeletes , System.currentTimeMillis());
    }

    public PairingHeapTester(int numOfInserts, int numOfPops, int numOfIncreases, int numOfDecreases, int numOfDeletes, long seed) {
        this.myHeap = new PairingHeap<Integer, Integer>();
        this.javaHeap = new PriorityQueue<>();
        this.generator = new KeyAndOperationGen(seed);
        this.usedNodes = new ArrayList<>(numOfInserts);
        generator.generateKeys(numOfInserts);
        generator.generateOperations(numOfInserts, numOfPops, 0, numOfIncreases, numOfDecreases, numOfDeletes);
    }


    public boolean test() throws DifferentTreesException, WrongImplementationException {
        //initialize(numOfInserts, numOfDeletes, numOfFounds, generator);
        for (Operation  operation : generator.getOperations()) {
            switch (operation) {
                case INSERT: {
                    Integer key = removeRandomKey(generator.getFreeKeys());
                    HeapNode<Integer, Integer> node = myHeap.insert(key, key);
                    javaHeap.add(key);
                    usedNodes.add(node);
                    checkEquality("insert");
                    break;
                }
                case DELETE: {
                    Integer myVal = myHeap.deleteMin();
                    Integer javaVal = javaHeap.poll();
                    usedNodes.remove(new HeapNode<>(myVal, myVal, null));
                    if (! myVal.equals(javaVal)) {
                        throw new WrongImplementationException("Deleted different minimal elements", generator.getSeed());
                    }
                    checkEquality("delete");
                    break;
                }
                case INCREASE: {
                    HeapNode<Integer, Integer> node = getRandomNode();
                    int oldPriority = node.getKey();
                    int newPriority = oldPriority - generator.getGenerator().nextInt(myHeap.getSize());
                    myHeap.increasePriority(newPriority, node);
                    javaHeap.remove(oldPriority);
                    javaHeap.add(newPriority);
                    checkEquality("increase");
                    break;
                }
                case DECREASE: {
                    HeapNode<Integer, Integer> node = getRandomNode();
                    int oldPriority = node.getKey();
                    int newPriority = oldPriority + generator.getGenerator().nextInt(myHeap.getSize());
                    myHeap.decreasePriority(newPriority, node);
                    javaHeap.remove(oldPriority);
                    javaHeap.add(newPriority);
                    checkEquality("decrease");
                    break;
                }
                case DELETE_HEAP_NODE: {
                    HeapNode<Integer, Integer> node = getRandomNode();
                    myHeap.deleteNode(node);
                    javaHeap.remove(node.getKey());
                    checkEquality("delete heap node");
                    break;
                }
            }
        }
        return true;
    }



    private HeapNode<Integer, Integer> getRandomNode() {
        int index = generator.getGenerator().nextInt(usedNodes.size());
        return usedNodes.get(index);
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
