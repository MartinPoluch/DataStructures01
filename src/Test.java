import structures.HeapNode;
import structures.PairingHeap;
import structures.tester.PairingHeapTester;
import structures.tester.SplayTreeTester;

public class Test {

    public static void main(String[] args) {
//        PairingHeap<Integer, String> heap = new PairingHeap<>();
//        HeapNode<Integer, String> node1 = heap.insert(10, "a");
//        HeapNode<Integer, String> node2 = heap.insert(1, "b");
//        HeapNode<Integer, String> node3 = heap.insert(8, "c");
//        HeapNode<Integer, String> node4 = heap.insert(9, "d");
//        HeapNode<Integer, String> node5 = heap.insert(4, "e");
//        HeapNode<Integer, String> node6 = heap.insert(2, "f");
//
//        heap.increasePriority(0, node1);
//        heap.increasePriority(3, node3);
//        heap.decreasePriority(100, node6);
//        heap.decreasePriority(11, node5);
//        heap.deleteNode(node4);
//        heap.deleteNode(node2);
//
//        System.out.println(heap.deleteMin());
//        System.out.println(heap.deleteMin());
//        System.out.println(heap.deleteMin());
//        System.out.println(heap.deleteMin());
//        System.out.println(heap.deleteMin());
//        System.out.println(heap.deleteMin());
//        System.out.println(heap.deleteMin());

//        PairingHeap<Integer, String> heap = new PairingHeap<>();
//        HeapNode<Integer, String> node1 = heap.insert(1, "a");
//        HeapNode<Integer, String> node2 = heap.insert(5, "b");
//        HeapNode<Integer, String> node3 = heap.insert(9, "c");
//        HeapNode<Integer, String> node4 = heap.insert(7, "d");
//        HeapNode<Integer, String> node5 = heap.insert(3, "e");
//        heap.decreasePriority(10,node1);
//        heap.decreasePriority(20, node4);
//        System.out.println();





//        for (int i = 0; i < 10; i++) {
//            try {
//                SplayTreeTester tester = new SplayTreeTester(10000, 10000, 1000, false);
//                if (tester.test()) {
//                    System.out.println("OK: " + i);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        for (int i = 0; i < 10; i++) {
            try {
                PairingHeapTester tester = new PairingHeapTester(10000, 5000, 2000, 2000, 2000);
                if (tester.test()) {
                    System.out.println("OK: " + i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
