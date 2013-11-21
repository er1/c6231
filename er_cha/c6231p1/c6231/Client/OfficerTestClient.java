package c6231.Client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class OfficerTestClient {

    static class StationTest extends Thread {

        String badge;
        final String[] names = {"Alice", "Bob", "Chuck", "Darryl", "Edmund", "Frank", "George", "Henry", "Igor", "Jake", "Kennith", "Leon", "Mike", "Neil", "Oscar", "Peter", "Queen", "Ronald", "Steve", "Tom", "Unit", "Victor", "Wendy", "Xray", "Yancy", "Zulu"};

        public StationTest(String badge) {
            this.badge = badge;
        }

        @Override
        public void run() {

            OfficerClient client = new OfficerClient(badge);
            client.connect();

            Random rand = new Random();

            for (int i = 0; i < RECS_PER_THREAD; i++) {

                int index = rand.nextInt(26);
                int index2 = rand.nextInt(26);

                String lname = names[index] + " (" + badge + ")";
                String fname = names[index2];
                String desc = "Someone";
                String addr = "123 Fake St.";
                String status;
                String lastseen = "";

                if (rand.nextBoolean()) {
                    status = "OnTheRun";
                    client.createCRecord(fname, lname, desc, status);
                } else {
                    status = "Missing";
                    client.createMRecord(fname, lname, addr, new Date(), lastseen, status);
                }
            }
        }
    }
    static final int THREADS = 20;

    static final int RECS_PER_THREAD = 200;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        // Create THREADS clients producing RECS_PER_THREAD records each
        final String[] stations = {"SPVM", "SPL", "SPB"};

        ArrayList<Thread> threads = new ArrayList<Thread>();

        // store THREADS threads
        for (String stationName : stations) {
            for (int i = 0; i < THREADS; i++) {
                StationTest t = new StationTest(stationName + Integer.toString(1001 + i));
                threads.add(t);
            }
        }

        // start each of them
        for (Thread t : threads) {
            t.start();
        }

        // wait for them all to finish
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException ex) {
            }
        }

        // check up on how many records they have
        OfficerClient client = new OfficerClient("SPVM9999");
        String ret;
        client.connect();
        ret = client.getRecordCounts();
        System.out.println(ret);
    }
}
