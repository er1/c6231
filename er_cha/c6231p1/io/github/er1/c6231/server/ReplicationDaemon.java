package io.github.er1.c6231.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Replication Daemon
 *
 * Used as a wrapper for starting another class with it's own main method This
 * will then kill and restart the process on a given signal form a UDP port
 *
 * @author chanman
 */
public class ReplicationDaemon {

    /**
     * This is the class with the static main method to start
     */
    public static final Class SERVERCLASS = StationServer.class;

    /**
     * This is the port that will trigger a reboot
     */
    public static final int DAEMONPORT = 1551;

    /*
    
     Please replace all instances of:
    
     DatagramSocket socket = new DatagramSocket(PORTNUM);

     with:
    
     DatagramSocket socket = new DatagramSocket(null);
     socket.setReuseAddress(true);
     socket.bind(new InetSocketAddress(PORTNUM));
    
     */
    
    /**
     * Wrapper main method
     *
     * @param args
     */
    public static void main(String[] args) {

        Process process = boot(SERVERCLASS);

        try (DatagramSocket socket = new DatagramSocket(null)) {
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(DAEMONPORT));

            byte[] buffer = new byte[1500];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (true) {
                try {
                    socket.receive(packet);

                    System.err.println("[Replicator] reboot requested");

                    if (process != null) {
                        try {
                            process.exitValue();
                            System.err.println("[Replicator] instance was dead");
                        } catch (IllegalThreadStateException ex) {
                            process.destroy();
                            System.err.println("[Replicator] instance was killed");
                        }
                    }

                    System.err.println("[Replicator] instance starting...");
                    process = boot(SERVERCLASS);

                    InetAddress sendAddr = packet.getAddress();
                    int sendPort = packet.getPort();
                    packet = new DatagramPacket(packet.getData(), packet.getLength(), sendAddr, sendPort);
                    socket.send(packet);

                } catch (IOException ex) {
                    System.err.println("[Replicator] Recv failed on socket: " + ex.toString());
                }
            }
        } catch (SocketException ex) {
            System.err.println("[Replicator] Socket failed: " + ex.toString());
        }
    }

    /**
     * start a new process based on the given class
     *
     * @param realmain real class to start
     * @return process object
     */
    public static Process boot(Class realmain) {
        try {
            ProcessBuilder pb;

            String cname = realmain.getCanonicalName();

            // get system specific path separator '/' or '\'
            String separator = System.getProperty("file.separator");

            // get where the classes used with this will be
            String classpath = System.getProperty("java.class.path");

            // get where the jvm executable on this machine should be
            String jvmbin = System.getProperty("java.home") + separator + "bin" + separator + "java";

            Process proc = new ProcessBuilder(jvmbin, "-cp", classpath, cname).inheritIO().start();

            return proc;

        } catch (IOException ex) {
            System.err.println("[Replicator] Replication has failed irrecoverably: " + ex.toString());
        }
        return null;
    }
}
