package c6231.Server;

import c6231.Log;
import c6231.MapSerializer;
import c6231.StationInterface;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Station Server Class
 *
 * When run, provides servers for the station in the static `stations`
 *
 * @author chanman
 */
public class StationServer extends Thread implements StationInterface {

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

        // Create the Registry on port 9989. if that fails, exit
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(9989);
        } catch (RemoteException ex) {
            serverlog.log("Could not create RMI registry: " + ex.getMessage());
            serverlog.log("Exiting...");
            return;
        }

        // Start a thread of each station
        for (String stationName : stations) {
            StationServer station = new StationServer(stationName);
            try {
                station.export(registry);
                station.start();
            } catch (RemoteException ex) {
                serverlog.log(stationName + " could not be started: " + ex.getMessage());
            }
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

    /**
     * Publish this station to the RMI registry
     *
     * @param registry
     * @throws RemoteException
     */
    public void export(Registry registry) throws RemoteException {
        log.log("Attempting to publish " + this.name + ":9989");

        Remote remote = UnicastRemoteObject.exportObject(this, 9989);
        registry.rebind(this.name, remote);

        log.log("Published, UDP on port " + portHash(name));
    }

    /**
     * Action this thread is taking care of
     */
    @Override
    public void run() {
        packetManagement();
    }

    /**
     *
     */
    protected void packetManagement() {

        int recvPort = portHash(name);
        DatagramSocket socket;
        try {
            socket = new DatagramSocket(recvPort);
        } catch (SocketException ex) {
            log.log(ex.toString() + " " + ex.getMessage());
            log.log("UDP server could not be started, UDP will be unavailable");
            return;
        }
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
     * @param request
     * @return
     */
    protected Map<String, String> handleRequest(Map<String, String> request) {
        HashMap<String, String> response = new HashMap<String, String>();
        // all request are count requests, dont check for now;
        int count = records.getRecordCount();
        response.put("recordCount", Integer.toString(count));
        return response;
    }

    @Override
    public void logDump() throws RemoteException {
        records.dump(log);
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
    public String createCRecord(String firstName, String lastName, String description, String status) throws RemoteException {
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
    public String createMRecord(String firstName, String lastName, String address, Date lastDate, String lastLocation, String status) throws RemoteException {
        log.log("createMRecord (" + firstName + " " + lastName + ": " + address + " " + lastLocation + "@" + lastDate.toString() + " [" + status + "])");

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
    public String getRecordCounts() throws RemoteException {

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
    public String editCRecord(String lastName, String recordID, String newStatus) throws RemoteException {
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
        return id;
    }
}
