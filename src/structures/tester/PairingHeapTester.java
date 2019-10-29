package structures.tester;

import org.omg.PortableInterceptor.INACTIVE;
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
        for (Operation  operation : generator.getOperations()) {
            switch (operation) {
                case INSERT: {
                    Integer key = removeRandomKey(generator.getFreeKeys());
                    HeapNode<Integer, Integer> node = myHeap.insert(key, key);
                    javaHeap.add(key);
                    usedNodes.add(node);
                    checkEquality("insert");
                    //System.out.println(operation);
                    break;
                }
                case DELETE: {
                    if (!usedNodes.isEmpty()) {
                        HeapNode<Integer, Integer> removedMinNode = myHeap.deleteMinNode();
                        Integer myVal = removedMinNode.getValue();
                        Integer javaVal = javaHeap.poll();
                        usedNodes.remove(removedMinNode);
                        if (! myVal.equals(javaVal)) {
                            throw new WrongImplementationException("Deleted different minimal elements seed:" + generator.getSeed(), generator.getSeed());
                        }
                        checkEquality("delete");
                    }
                    //System.out.println(operation);
                    break;
                }
                case INCREASE: {
                    if (! usedNodes.isEmpty()) {
                        HeapNode<Integer, Integer> node = getRandomNode();
                        int oldPriority = node.getKey();
                        int newPriority = oldPriority - generator.getGenerator().nextInt(myHeap.getSize());
                        node.setValue(newPriority);
                        myHeap.increasePriority(newPriority, node);
                        javaHeap.remove(oldPriority);
                        javaHeap.add(newPriority);
                        checkEquality("increase");
                    }
                    //System.out.println(operation);
                    break;
                }
                case DECREASE: {
                    if (! usedNodes.isEmpty()) {
                        HeapNode<Integer, Integer> node = getRandomNode();
                        int oldPriority = node.getKey();
                        int newPriority = oldPriority + generator.getGenerator().nextInt(myHeap.getSize());
                        node.setValue(newPriority);
                        myHeap.decreasePriority(newPriority, node);
                        javaHeap.remove(oldPriority);
                        javaHeap.add(newPriority);
                        checkEquality("decrease");
                    }
                   //System.out.println(operation);
                    break;
                }
                case DELETE_HEAP_NODE: {
                    if (! usedNodes.isEmpty()) {
                        HeapNode<Integer, Integer> node = removeRandomNode();
                        myHeap.deleteNode(node);
                        javaHeap.remove(node.getKey());
                        checkEquality("delete heap node");
                    }
                    //System.out.println(operation);
                    break;
                }
            }
        }
        checkEquality("End");
        return true;
    }


    private HeapNode<Integer, Integer> getRandomNode() {
        int index = generator.getGenerator().nextInt(usedNodes.size());
        return usedNodes.get(index);
    }

    private HeapNode<Integer, Integer> removeRandomNode() {
        int index = generator.getGenerator().nextInt(usedNodes.size());
        return usedNodes.remove(index);
    }

    private int removeRandomKey(List<Integer> keys) {
        int index = generator.getGenerator().nextInt(keys.size());
        return keys.remove(index);
    }

    private void checkEquality(String operation) throws DifferentTreesException, WrongImplementationException {
        if (myHeap.getSize() != javaHeap.size()) {
            throw new WrongImplementationException("Sizes of heaps are different for operation " + operation + " seed: " + generator.getSeed(), generator.getSeed());
        }

        if (myHeap.getSize() == 0) {
            if (myHeap.findMin() != null) {
                throw new WrongImplementationException("Heap is empty findMin should return null" + operation + " seed: " + generator.getSeed(), generator.getSeed());
            }
        }
        else if (!myHeap.findMin().equals(javaHeap.peek())) {
            throw new WrongImplementationException("Min elements are difference for operation " + operation + "\nexpected: " + javaHeap.peek()
                    + "\nactual: " + myHeap.findMin() + "\n"  + " seed: " + generator.getSeed(), generator.getSeed());
        }
    }
}
