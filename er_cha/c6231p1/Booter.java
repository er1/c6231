
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Booter {

    public static void main(String[] args) {
        System.out.println("Hello, Booter!");
        Object o = new Object();
        
        Process process;

        synchronized (o) {
            while (true) {

                System.out.println("Start a process...");
                process = boot(Hello.class);
                try {
                    o.wait(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Booter.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (process != null) {

                    try {
                        System.out.println("Process ended with: " + process.exitValue());                        
                    } catch (IllegalThreadStateException ex) {
                        process.destroy();
                        System.out.println("KILL attempted");
                    }
                }

            }
        }
    }

    public static Process boot(Class realmain) {
        try {
            ProcessBuilder pb;

            String cname = realmain.getCanonicalName();

            // get system specific path separator '/' or '\'
            String separator = System.getProperty("file.separator");

            // get where the classes used with this will be
            String classpath = System.getProperty("java.class.path");

            // get where the jvm executable on this machine should be
            // 1337h4x omgwtfbbq
            String jvmbin = System.getProperty("java.home") + separator + "bin" + separator + "java";

            Process proc = new ProcessBuilder(jvmbin, "-cp", classpath, cname).inheritIO().start();

            return proc;

        } catch (IOException ex) {
            System.err.println("Epic fail!");
        }
        return null;
    }
}
