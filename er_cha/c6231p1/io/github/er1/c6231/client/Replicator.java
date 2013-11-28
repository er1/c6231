package io.github.er1.c6231.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Replicator {

    protected int getReplicationPort(int servicePort) {
        int replicationPort = (servicePort / 100) + 1537;
        return replicationPort;
    }

    public boolean RestartServiceFromPort(int servicePort) {
        int replicationPort = getReplicationPort(servicePort);
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress addr = InetAddress.getByName("localhost");

            DatagramPacket packet = new DatagramPacket(new byte[]{10}, 1, addr, replicationPort);
            socket.send(packet);

            byte[] buffer = new byte[1500];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
