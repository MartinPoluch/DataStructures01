import Apk.Airport;
import structures.PairingHeap;
import structures.SplayTree;
import structures.tester.KeyAndOperationGen;
import structures.tester.PairingHeapTester;
import structures.tester.SplayTreeTester;

public class Test {

    public static void main(String[] args) {

        PairingHeap<Integer, String> heap = new PairingHeap<>();
        heap.insert(2, "a2");
        heap.insert(2, "a1");
        heap.insert(2, "c");
        heap.insert(2, "d");
        heap.insert(2, "e");
        System.out.println(heap.deleteMin());
        System.out.println(heap.deleteMin());
        System.out.println(heap.deleteMin());
        System.out.println(heap.deleteMin());
        System.out.println(heap.deleteMin());
        System.out.println(heap.deleteMin());
        System.out.println(heap.deleteMin());

//        for (int i = 0; i < 10; i++) {
//            try {
//                SplayTreeTester tester = new SplayTreeTester(10000, 5000, 1000, true);
//                if (tester.test()) {
//                    System.out.println("OK: " + i);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

//        for (int i = 0; i < 100; i++) {
//            try {
//                PairingHeapTester tester = new PairingHeapTester(1000, 500, 100);
//                if (tester.test()) {
//                    System.out.println("OK: " + i);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (int i = 0; i < 100; i++) {
//            try {
//                SplayTreeTester tester = new SplayTreeTester(5000, 3000, 1000);
//                if (tester.test()) {
//                    System.out.println("OK: " + i);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }


    }
}
