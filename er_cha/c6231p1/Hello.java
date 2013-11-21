
import java.util.Random;

public class Hello {

    public static void main(String[] args) {
        System.out.println("Hello, World!");
        Object o = new Object();
        synchronized (o) {
            try {
                Random rand = new Random();
                if (rand.nextBoolean()) {
                    System.out.println("...");
                    o.wait(2000);
                    System.out.println("Bad!!!");
                } else {
                    System.out.println(": )");
                }
                //o.wait();
            } catch (InterruptedException ex) {
            }
        }
    }
}
