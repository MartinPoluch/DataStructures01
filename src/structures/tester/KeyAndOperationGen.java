package structures.tester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
        for (int initial = 0; initial < numOfInserts / 2; initial++) { // polovicu insertov vykoname hned, aby sa naplnila struktura
            operations.add(Operation.INSERT);
        }

        if (numOfInserts % 2 == 1) {
            operations.add(Operation.INSERT);
        }

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
