import Apk.DataGenerator;
import structures.HeapNode;
import structures.PairingHeap;
import structures.tester.PairingHeapTester;
import structures.tester.SplayTreeTester;

import java.util.PriorityQueue;

public class Test {

    public static void main(String[] args) {
//        DataGenerator generator = new DataGenerator();
//        for (int i = 0; i < 20; i++) {
//            System.out.println(generator.generateRandomString());
//        }
//        generator.generateRandomString();




//        System.out.println("Splay tester");
//        for (int i = 0; i < 10; i++) {
//            try {
//                SplayTreeTester tester = new SplayTreeTester(1000, 400, 100, true);
//                if (tester.test()) {
//                    System.out.println("OK: " + i);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        System.out.println("\nPairingheap tester");
        for (int i = 0; i < 10; i++) {
            try {
                PairingHeapTester tester = new PairingHeapTester(5000, 1000, 1000,1000 , 1000);
                if (tester.test()) {
                    System.out.println("OK: " + i);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
