package c6231;

import java.io.PrintWriter;
import java.util.Date;

/**
 * Logging class to provide logging facilities to other classes Logging provides
 * output to a file based on the name passed to the contructor as well as
 * standard out;
 *
 * @author chanman
 */
public class Log {

    String id;
    PrintWriter out;

    /**
     * Instantiate a Log for a given name;
     *
     * @param id name of log
     */
    public Log(String id) {
        this.id = id;
        try {
            String filename = id + ".txt";
            out = new PrintWriter(filename, "ASCII");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Log an entry with the name and date
     *
     * @param content content to log
     */
    public void log(String content) {
        Date date = new Date();
        out.print("[" + date.toString() + " " + id + "] " + content + "\n");
        //out.flush();
        //System.out.println("[" + date.toString() + " " + id + "] " + content);
    }
}
