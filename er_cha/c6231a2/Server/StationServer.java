package c6231.Server;

import c6231.Log;
import c6231.MapSerializer;
import c6231._StationInterfaceImplBase;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.omg.CORBA.*;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

/**
 * Station Server Class
 *
 * When run, provides servers for the station in the static `stations`
 *
 * @author chanman
 */
public class StationServer extends _StationInterfaceImplBase {

    // Stations to start
    final static String[] stations = {"SPVM", "SPL", "SPB"};
    // Station specific variables
    String name;
    RecordContainer records;
    Log log;

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

    /**
     * Publish this station to the RMI registry
     *
     * @param registry
     * @throws RemoteException
     */
    public void exportRPC() {

        log.log("Attempting to publish RPC for " + this.name);

        // ghetto hardcode the parameters
        ORB orb = ORB.init(new String[]{"-ORBInitialHost", "localhost", "-ORBInitialPort", "8989"}, null);

        orb.connect(this);

        org.omg.CORBA.Object objRef;
        try {
            objRef = orb.resolve_initial_references("NameService");
        } catch (Exception ex) {
            throw new RuntimeException("NameService", ex);
        }
        NamingContext ncRef = NamingContextHelper.narrow(objRef);

        // bind the Object Reference in Naming
        NameComponent nc = new NameComponent(this.name, "");
        NameComponent path[] = {nc};
        try {
            ncRef.rebind(path, this);
        } catch (Exception ex) {
            throw new RuntimeException("bind", ex);
        }

        log.log("Published RPC for " + name);

        log.log("Attempting to start ORB...");
        orb.run();

    }

    /**
     * UDP request server loop
     */
    protected void exportUDP() {

        // create the socket
        log.log("Attempting to publish UDP for " + this.name);

        int recvPort = portHash(name);
        DatagramSocket socket;
        try {
            socket = new DatagramSocket(recvPort);
        } catch (SocketException ex) {
            log.log(ex.toString() + " " + ex.getMessage());
            log.log("UDP server could not be started, UDP will be unavailable");
            return;
        }

        log.log("Published UDP for " + name + " on " + portHash(name));

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
     * Take a UDP request map and return a response map
     *
     * @param request
     * @return
     */
    protected Map<String, String> handleRequest(Map<String, String> request) {
        String action = request.get("action");
        HashMap<String, String> response = new HashMap<String, String>();

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

        response.put("error", "invalidAct^ion");
        return response;
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
     * @throws RemoteException
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
     * @param lastDate
     * @param lastLocation
     * @param status
     * @return
     * @throws RemoteException
     */
    @Override
    public String createMRecord(String firstName, String lastName, String address, String lastDateStr, String lastLocation, String status) {
        log.log("createMRecord (" + firstName + " " + lastName + ": " + address + " " + lastLocation + "@" + lastDateStr + " [" + status + "])");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date lastDate = new Date();
        try {
            lastDate = df.parse(lastDateStr);
        } catch (ParseException ex) {
            log.log(lastDateStr + " isn't parsable");
        }

        MissingRecord record = new MissingRecord(records.getNextFreeId(), firstName, lastName, address, lastDate, lastLocation, status);
        String id = record.getId();
        records.addRecord(record);

        log.log("  createMRecord created <" + id + "> for " + firstName + " " + lastName);

        return id;
    }

    /**
     *
     * @return @throws RemoteException
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

                DatagramSocket socket = new DatagramSocket();
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
                    counts += stationName + ": " + count + ", ";
                } else {
                    counts += stationName + " responded badly, ";
                }

                socket.close();

            } catch (SocketException ex) {
                throw new RuntimeException(ex);
            } catch (UnknownHostException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                // station unavailible
                throw new RuntimeException(ex);
            }

        }

        return counts;
    }

    /**
     *
     * @param lastName
     * @param recordID
     * @param newStatus
     * @return
     * @throws RemoteException
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
            id = "_NONE_";
        }

        return id;
    }

    @Override
    public String transferRecord(String badgeID, String recordID, String remoteStationServerName) {
        log.log("transferRecord (" + badgeID + " transfers " + recordID + " --> " + remoteStationServerName + ")");

        if (!Arrays.asList(stations).contains(remoteStationServerName)) {
            log.log("transferRecord -X-> " + remoteStationServerName + " server is not whitelisted");
            return "_FAIL_";
        }

        try {
            Record record = records.getRecord(recordID);

            if (record == null) {
                log.log("  transferRecord <FAILED> record not found");
                return "_NONE_";
            }

            Map<String, String> request = RecordFactory.createMapFromRecord(record);

            request.put("action", "transfer");

            DatagramSocket socket = new DatagramSocket();
            InetAddress addr = InetAddress.getByName("localhost");

            String requeststr = MapSerializer.stringify(request);

            int port = this.portHash(remoteStationServerName);

            DatagramPacket packet = new DatagramPacket(requeststr.getBytes(), requeststr.length(), addr, port);
            socket.send(packet);

            byte[] buffer = new byte[1500];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            // 
            Map<String, String> response = MapSerializer.parse(new String(buffer, 0, packet.getLength()));

            String status = response.get("status");

            if ("success".equals(status)) {
                records.removeRecord(record.getId(), record.getLastName());
                log.log("  transferRecord <OK>");
            } else {
                log.log("  transferRecord <FAIL> " + status);
            }

            socket.close();

            if (response.containsKey("id")) {
                return response.get("id"); // return new id
            }
            return "_NONE_";

        } catch (Exception ex) {
            log.log("  transferRecord <FAILED> connection failed");
            return "_FAIL_";
        }
    }
}
