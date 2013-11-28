package io.github.er1.c6231.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.github.er1.c6231.Log;
import io.github.er1.c6231.MapSerializer;
import io.github.er1.c6231.StationInterface;

/**
 * Station Server Class
 *
 * When run, provides servers for the station in the static `stations`
 *
 * @author chanman
 */
public class StationServer implements StationInterface {

    // Stations to start
    final static String[] stations = {"SPVM", "SPB", "SPL"};

    /**
     * Start all of the stations
     *
     * @param args
     */
    public static void main(String[] args) {
        // Create a general log for server starts and errors        
        Log serverlog = new Log("ServerMain");
        serverlog.log("Started...");

        // Start a thread of each station
        for (String stationName : stations) {
            StationServer station = new StationServer(stationName);

            station.startServerThreads();
        }
    }
    // Station specific variables
    String name;
    RecordContainer records;

    Log log;

    /**
     * Create a station server
     *
     * @param name
     */
    public StationServer(String name) {
        super();
        this.name = name;
        this.records = RecordContainer.getRecordContainer(name);
        log = new Log(name);
        log.log("Station Created");
    }

    /*
     * Required Methods
     */
    /**
     *
     * @param firstName
     * @param lastName
     * @param description
     * @param status
     * @return
     */
    @Override
    public String createCRecord(String firstName, String lastName, String description, String status) {
        log.log("createCRecord (" + firstName + " " + lastName + ": " + description + " [" + status + "])");

        CriminalRecord record = new CriminalRecord(records.getNextFreeId(), firstName, lastName, description, status);
        String id = record.getId();
        records.addRecord(record);

        log.log("  createCRecord created <" + id + "> for " + firstName + " " + lastName);

        return id;
    }

    /**
     *
     * @param firstName
     * @param lastName
     * @param address
     * @param lastDateLong
     * @param lastLocation
     * @param status
     * @return
     */
    @Override
    public String createMRecord(String firstName, String lastName, String address, long lastDateLong, String lastLocation, String status) {
        log.log("createMRecord (" + firstName + " " + lastName + ": " + address + " " + lastLocation + "@" + Long.toString(lastDateLong) + " [" + status + "])");

        Date lastDate = new Date(lastDateLong);
        MissingRecord record = new MissingRecord(records.getNextFreeId(), firstName, lastName, address, lastDate, lastLocation, status);
        String id = record.getId();
        records.addRecord(record);

        log.log("  createMRecord created <" + id + "> for " + firstName + " " + lastName);

        return id;
    }

    /**
     *
     * @param lastName
     * @param recordID
     * @param newStatus
     * @return
     */
    @Override
    public String editCRecord(String lastName, String recordID, String newStatus) {
        log.log("editCRecord (" + recordID + "(" + lastName + ") --> " + newStatus);

        Record record = records.getRecord(recordID, lastName);
        String id = null;

        if (record != null) {
            synchronized (record) {
                record.setStatus(newStatus);
                id = record.getId();
            }
            log.log("  editCRecord sucessfully set the status of record " + recordID + " to " + newStatus);
        } else {
            log.log("  editCRecord did not find record " + recordID);
        }

        if (id == null) {
            id = "";
        }

        return id;
    }

    /**
     * Publish this station to the RMI registry
     *
     */
    public void exportRPC() {
        log.log("Attempting to start RPC...");

        // hard coded magic number
        int port = 1447 + Arrays.asList(stations).indexOf(this.name);

        StationSkel skel = new StationSkel(log, port, this);
        skel.start();
    }

    /**
     * UDP request server loop
     */
    protected void exportUDP() {

        // create the socket
        int recvPort = portHash(name);

        log.log("Attempting to publish UDP for " + this.name + " on " + recvPort);

        DatagramSocket socket;
        try {
            socket = new DatagramSocket(null);
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(recvPort));
        } catch (SocketException ex) {
            log.log("Interstation server could not be started. reason: " + ex.toString());
            return;
        }

        log.log("Published UDP for " + name + " on " + recvPort);

        // wait for packets
        byte[] buffer = new byte[1500];
        while (true) {
            DatagramPacket recvPacket = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(recvPacket);

                InetAddress sendAddr = recvPacket.getAddress();
                int sendPort = recvPacket.getPort();
                String requestString = new String(buffer).substring(0, recvPacket.getLength());

                Map<String, String> request = MapSerializer.parse(requestString);
                Map<String, String> response = handleRequest(request);
                String responseString = MapSerializer.stringify(response);

                DatagramPacket sendPacket = new DatagramPacket(responseString.getBytes(), responseString.length(), sendAddr, sendPort);
                socket.send(sendPacket);

            } catch (IOException ex) {
                log.log(ex.toString() + " " + ex.getMessage());
            }
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getRecordCounts() {

        // dispatch udp request
        log.log("getRecordCounts ()");

        String counts = "";

        for (String stationName : stations) {
            try {
                // only kind of request but send it anyways
                String request = "action:recordCount";
                try (DatagramSocket socket = new DatagramSocket()) {
                    InetAddress addr = InetAddress.getByName("localhost");
                    int port = this.portHash(stationName);

                    DatagramPacket packet = new DatagramPacket(request.getBytes(), request.length(), addr, port);
                    socket.send(packet);

                    byte[] buffer = new byte[1500];
                    packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    //
                    String response = new String(buffer, 0, packet.getLength());

                    System.out.println(response);

                    if (response.startsWith("recordCount:")) {
                        String count = response.substring(response.indexOf(":") + 1);
                        counts += stationName + ":" + count + ";";
                    } else {
                        counts += stationName + ":-1;";
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return counts;
    }

    /**
     * Take a UDP request map and return a response map
     *
     * @param request
     * @return
     */
    protected Map<String, String> handleRequest(Map<String, String> request) {
        String action = request.get("action");
        HashMap<String, String> response = new HashMap<>();

        if (action == null) {
            action = "";
        }

        if ("recordCount".equals(action)) {
            int count = records.getRecordCount();
            response.put("recordCount", Integer.toString(count));
            return response;
        }

        if ("transfer".equals(action)) {
            long newid = records.getNextFreeId();
            request.put("id", Long.toString(newid));
            Record record = RecordFactory.createRecordFromMap(request);
            records.addRecord(record);
            response.put("status", "success");
            response.put("id", record.getId());
            return response;
        }

        response.put("error", "invalidAction");
        return response;
    }

    /**
     * Given a station stationName, determine which port its UDP server is on
     *
     * @param string Station stationName
     * @return port
     */
    protected int portHash(String string) {
        int bucket = 0;
        for (char ch : string.toCharArray()) {
            // totally awkward mixing function
            bucket = (bucket * 94 + ch - 33) % (94 * 94 - 36);
        }
        // make sure the port number is not restricted by adding the first
        // non restricted port
        return bucket + 1024;
    }

    public void startServerThreads() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                exportUDP();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                exportRPC();
            }
        }).start();
    }

    @Override
    public String transferRecord(String badgeID, String recordID, String remoteStationServerName) {
        log.log("transferRecord (" + badgeID + " transfers " + recordID + " --> " + remoteStationServerName + ")");

        if (!Arrays.asList(stations).contains(remoteStationServerName)) {
            log.log("transferRecord -X-> " + remoteStationServerName + " server is not whitelisted");
            return "";
        }

        try {
            Record record = records.getRecord(recordID);

            if (record == null) {
                log.log("  transferRecord <FAILED> record not found");
                return "";
            }

            Map<String, String> request = RecordFactory.createMapFromRecord(record);

            request.put("action", "transfer");
            Map<String, String> response;
            try (DatagramSocket socket = new DatagramSocket()) {
                InetAddress addr = InetAddress.getByName("localhost");
                String requeststr = MapSerializer.stringify(request);
                int port = this.portHash(remoteStationServerName);
                DatagramPacket packet = new DatagramPacket(requeststr.getBytes(), requeststr.length(), addr, port);
                socket.send(packet);
                byte[] buffer = new byte[1500];
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                response = MapSerializer.parse(new String(buffer, 0, packet.getLength()));
                String status = response.get("status");
                if ("success".equals(status)) {
                    records.removeRecord(record.getId(), record.getLastName());
                    log.log("  transferRecord <OK>");
                } else {
                    log.log("  transferRecord <FAIL> " + status);
                }
            }

            if (response.containsKey("id")) {
                return response.get("id"); // return new id
            }
            return "";

        } catch (IOException ex) {
            log.log("  transferRecord <FAILED> connection failed");
            return "";
        }
    }
}
