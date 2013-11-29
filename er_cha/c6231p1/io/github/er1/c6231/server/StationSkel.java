package io.github.er1.c6231.server;

import io.github.er1.c6231.Log;
import io.github.er1.c6231.StationInterface;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Skeleton Methods for to map methods to UDP packets
 *
 * @author chanman
 */
public class StationSkel {

    Log log;
    StationInterface impl;
    int port;

    /**
     * Construct based on a log, port to publish on and implementation
     *
     * @param log object to log against
     * @param port port to publish methods on
     * @param implementation implementation of methods
     */
    public StationSkel(Log log, int port, StationInterface implementation) {
        this.log = log;
        this.port = port;
        impl = implementation;
    }

    /**
     * Publish the interface as a synchronous method
     */
    public void start() {
        // create the socket
        log.log("Attempting to publish UDP");

        int recvPort = port;
        DatagramSocket socket;
        try {
            socket = new DatagramSocket(null);
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(recvPort));
        } catch (SocketException ex) {
            log.log("RPC server could not be started. reason: " + ex.toString());
            return;
        }

        log.log("Published UDP on " + port);

        // wait for packets
        byte[] buffer = new byte[1500];
        while (true) {
            DatagramPacket recvPacket = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(recvPacket);

                InetAddress sendAddr = recvPacket.getAddress();
                int sendPort = recvPacket.getPort();

                // dirty java object serialization for compatibility with everyone else
                ByteArrayInputStream bais = new ByteArrayInputStream(recvPacket.getData());
                ObjectInputStream ois = new ObjectInputStream(bais);
                String requestString = (String) ois.readObject();

                log.log(requestString);
                
                String responseString = handle(requestString);
                
                log.log(responseString);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(responseString);
                byte[] response = baos.toByteArray();

                DatagramPacket sendPacket = new DatagramPacket(response, response.length, sendAddr, sendPort);
                socket.send(sendPacket);

            } catch (IOException | ClassNotFoundException ex) {
                log.log("Skel: " + ex.toString());
            }
        }
    }

    private String handle(String arglist) {
        String[] args = arglist.split(":");
        String method = args[0].toLowerCase(); // lowercase for team compatibility

        switch (method) {
            case "createcrecord":
                return impl.createCRecord(args[1], args[2], args[3], args[4]);
            case "createmrecord":
                return impl.createMRecord(args[1], args[2], args[3], Long.parseLong(args[4]), args[5], args[6]);
            case "editcrecord":
                return impl.editCRecord(args[1], args[2], args[3]);
            case "getrecordcounts":
                return impl.getRecordCounts();
            case "transferrecord":
                return impl.transferRecord(args[1], args[2], args[3]);
            default:
                throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
