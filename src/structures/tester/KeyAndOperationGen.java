package structures.tester;

import java.util.*;

public class KeyAndOperationGen {

    private List<Integer> freeKeys;
    private List<Operation> operations;
    private Random generator;
    private long seed;

    public KeyAndOperationGen() {
        this(System.currentTimeMillis());
    }

    public KeyAndOperationGen(long seed) {
        freeKeys = new ArrayList<>();
        operations = new ArrayList<>();
        this.seed = seed;
        generator = new Random(seed);
    }

    public void generateKeys(int numOfKeys) {
        for (int key = 1; key <= numOfKeys; key++) {
            freeKeys.add(key);
        }
    }

    public void generateOperations(int numOfInserts, int numOfDeletes, int numOfFinds) {
        addFixedInserts(numOfInserts);
        List<Operation> randomOperations = generateRandomOperations(numOfInserts, numOfDeletes, numOfFinds);
        Collections.shuffle(randomOperations, generator);
        operations.addAll(randomOperations);
    }

    private void addFixedInserts(int numOfInserts) {
        for (int initial = 0; initial < numOfInserts / 2; initial++) { // polovicu insertov vykoname hned, aby sa naplnila struktura
            operations.add(Operation.INSERT);
        }
        if (numOfInserts % 2 == 1) {
            operations.add(Operation.INSERT);
        }
    }

    private List<Operation> generateRandomOperations(int numOfInserts, int numOfDeletes, int numOfFinds) {
        List<Operation> randomOperations = new ArrayList<>();
        for (int insert = 0; insert < numOfInserts / 2; insert++) { // zvysne inserty
            randomOperations.add(Operation.INSERT);
        }
        for (int delete = 0; delete < numOfDeletes; delete++) {
            randomOperations.add(Operation.DELETE);
        }
        for (int find = 0; find < numOfFinds; find++) {
            randomOperations.add(Operation.FIND);
        }
        return randomOperations;
    }

    public void generateOperations(int numOfInserts, int numOfPops, int numOfPeeks, int numOfIncreases, int numOfDecreases, int numOfDeletes) {
        addFixedInserts(numOfInserts);
        List<Operation> randomOperations = generateRandomOperations(numOfInserts, numOfPops, numOfPeeks);

        for (int increase = 0; increase < numOfIncreases; increase++) {
            randomOperations.add(Operation.INCREASE);
        }

        for (int decrease = 0; decrease < numOfDecreases; decrease++) {
            randomOperations.add(Operation.DECREASE);
        }

        for (int delete = 0; delete < numOfDeletes; delete++) {
            randomOperations.add(Operation.DELETE_HEAP_NODE);
        }

        Collections.shuffle(randomOperations, generator);
        operations.addAll(randomOperations);

    }

    public List<Integer> getFreeKeys() {
        return freeKeys;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public Random getGenerator() {
        return generator;
    }

    public long getSeed() {
        return seed;
    }
}
