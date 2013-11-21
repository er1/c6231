package c6231.Server;

import c6231.Log;
import c6231.StationInterface;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class StationSkel {

    Log log;
    StationInterface impl;
    int port;

    public StationSkel(Log log, int port, StationInterface implementation) {
        this.log = log;
        this.port = port;
        impl = implementation;
    }

    public void start() {
        // create the socket
        log.log("Attempting to publish UDP");

        int recvPort = port;
        DatagramSocket socket;
        try {
            socket = new DatagramSocket(recvPort);
        } catch (SocketException ex) {
            log.log(ex.toString() + " " + ex.getMessage());
            log.log("UDP server could not be started, UDP will be unavailable");
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
                String requestString = new String(buffer).substring(0, recvPacket.getLength());

                String responseString = handle(requestString);

                DatagramPacket sendPacket = new DatagramPacket(responseString.getBytes(), responseString.length(), sendAddr, sendPort);
                socket.send(sendPacket);

            } catch (IOException ex) {
                log.log(ex.toString() + " " + ex.getMessage());
            }
        }
    }

    private String handle(String arglist) {
        String[] args = arglist.split(":");
        String method = args[0];

        switch (method) {
            case "createCRecord":
                return impl.createCRecord(args[1], args[2], args[3], args[4]);
            case "createMRecord":
                return impl.createMRecord(args[1], args[2], args[3], Long.parseLong(args[4]), args[5], args[6]);
            case "editCRecord":
                return impl.editCRecord(args[1], args[2], args[3]);
            case "getRecordCounts":
                return impl.getRecordCounts();
            case "transferRecord":
                return impl.transferRecord(args[1], args[2], args[3]);
            default:
                throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
