import structures.PairingHeap;
import structures.tester.KeyAndOperationGen;
import structures.tester.PairingHeapTester;
import structures.tester.SplayTreeTester;

public class Test {

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            try {
                PairingHeapTester tester = new PairingHeapTester(1000, 500, 100);
                if (tester.test()) {
                    System.out.println("OK: " + i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 100; i++) {
            try {
                SplayTreeTester tester = new SplayTreeTester(5000, 3000, 1000);
                if (tester.test()) {
                    System.out.println("OK: " + i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // starting branch test
    }
}