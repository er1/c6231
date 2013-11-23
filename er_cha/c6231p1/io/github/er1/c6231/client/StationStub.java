package io.github.er1.c6231.client;

import io.github.er1.c6231.StationInterface;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class StationStub implements StationInterface {

    int port;

    public StationStub(int port) {
        this.port = port;
    }

    @Override
    public String createCRecord(String firstName, String lastName, String description, String status) {
        StringBuilder args = new StringBuilder();
        args.append("createCRecord");
        String[] arglist = new String[]{firstName, lastName, description, status};
        for (String arg : arglist) {
            args.append(":");
            args.append(arg);
        }
        return handoff(args.toString());
    }

    @Override
    public String createMRecord(String firstName, String lastName, String address, long lastDate, String lastLocation, String status) {
        StringBuilder args = new StringBuilder();
        args.append("createMRecord");
        String[] arglist = new String[]{firstName, lastName, address, Long.toString(lastDate), lastLocation, status};
        for (String arg : arglist) {
            args.append(":");
            args.append(arg);
        }
        return handoff(args.toString());
    }

    @Override
    public String editCRecord(String lastName, String recordID, String newStatus) {
        StringBuilder args = new StringBuilder();
        args.append("editCRecord");
        String[] arglist = new String[]{lastName, recordID, newStatus};
        for (String arg : arglist) {
            args.append(":");
            args.append(arg);
        }
        return handoff(args.toString());
    }

    @Override
    public String getRecordCounts() {
        StringBuilder args = new StringBuilder();
        args.append("getRecordCounts");
        return handoff(args.toString());
    }

    @Override
    public String transferRecord(String badgeID, String recordID, String remoteStationServerName) {
        StringBuilder args = new StringBuilder();
        args.append("transferRecord");
        String[] arglist = new String[]{badgeID, recordID, remoteStationServerName};
        for (String arg : arglist) {
            args.append(":");
            args.append(arg);
        }
        return handoff(args.toString());
    }

    String handoff(String request) {
        String response;
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress addr = InetAddress.getByName("localhost");
            DatagramPacket packet = new DatagramPacket(request.getBytes(), request.length(), addr, port);
            socket.send(packet);
            byte[] buffer = new byte[1500];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            response = new String(buffer, 0, packet.getLength());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return response;
    }
}
