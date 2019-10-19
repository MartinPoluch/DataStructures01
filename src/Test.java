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

//        SplayTreeTester tester = new SplayTreeTester(0);
//        try {
//            if (tester.test(2000, 1000, 0)) {
//                System.out.println("OK: ");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }




//        SplayTree<Integer, String> tree = new SplayTree<Integer, String>();
//        tree.insert(4, "0");
//        tree.insert(2, "0");
//        tree.insert(9, "0");
//        tree.insert(1, "0");
//        tree.insert(3, "0");
//        TreeNode<Integer, String> node = tree.findLastNode(3);
//        tree.rotateToLeft(node);
//        tree.rotateToRight(node);
//
//        tree.printLevels();

//        SplayTree<Integer, String> tree = new SplayTree<Integer, String>();
//        tree.insert(2, "2");
//        tree.insert(4, "4");
//        tree.insert(1, "1");
//        tree.insert(3, "3");
//        tree.insert(10, "0");
//        tree.insert(0, "0");
//        tree.remove(4);
//        tree.printLevels();

//
//
//
//
//
//        for (TreeNode<Integer, String> n : tree.inOrder()) {
//            System.out.println(n.getKey());
//        }
//
//
//       System.out.println();
//        PairingHeap<Integer, Integer> heap = new PairingHeap();
//        heap.insert(6, 6);
//        heap.insert(4, 4);
//        heap.insert(3, 3);
//        heap.insert(8, 8);
//        heap.insert(9, 9);
//        System.out.println(heap.deleteMin());
//        System.out.println(heap.deleteMin());
//        System.out.println(heap.deleteMin());
//        System.out.println(heap.deleteMin());
//        System.out.println(heap.deleteMin());
//        System.out.println(heap.deleteMin());
//        System.out.println();

    }
}
