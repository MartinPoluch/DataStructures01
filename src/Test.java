import Apk.DataGenerator;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import structures.HeapNode;
import structures.PairingHeap;
import structures.SplayTree;
import structures.tester.PairingHeapTester;
import structures.tester.SplayTreeTester;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;

public class Test {

    public static void main(String[] args) {

        LocalDateTime date = LocalDateTime.of(2019, 10, 12, 20, 54);
        System.out.println(date);
        String str = date.toLocalDate() + " " + date.toLocalTime();
        System.out.println(str);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        System.out.println(dateTime);
        //LocalDateTime newDate = new LocalDateTime(s);
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

//        System.out.println("\nPairingheap tester");
//        for (int i = 0; i < 10; i++) {
//            try {
//                PairingHeapTester tester = new PairingHeapTester(50000, 10000, 10000,10000 , 10000);
//                if (tester.test()) {
//                    System.out.println("OK: " + i);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }


    }
}
