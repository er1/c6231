package c6231.Client;

import c6231.Log;
import c6231.StationInterface;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Officer Class to act against the RMI server logging events as they occur
 * @author chanman
 */
public class OfficerClient {

    String badgeId;
    String station;
    StationInterface instance;
    Log log;

    /**
     * Create an Officer client and its corresponding log
     * @param badgeId
     */
    public OfficerClient(String badgeId) {
        if (!badgeId.matches("[A-Z]+\\d\\d\\d\\d")) {
            throw new RuntimeException("Bad Badge ID");
        }
        this.log = new Log(badgeId);
        this.badgeId = badgeId;
        this.station = badgeId.substring(0, badgeId.length() - 4);
        this.log.log("Started!");
    }

    /**
     * Connect to the RMI server
     * @throws RemoteException
     */
    public void connect() throws RemoteException {
        try {
            instance = (StationInterface) Naming.lookup("rmi://localhost:9989/" + station);
        } catch (NotBoundException ex) {
            throw new RemoteException("Cannot Connect", ex);
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Bad Badge ID");
        }
        this.log.log("Connected!");
    }

    void createCRecord(String firstName, String lastName, String description, String status) throws RemoteException {
        log.log("createCRecord (" + firstName + " " + lastName + ": " + description + " [" + status + "])");
        instance.createCRecord(firstName, lastName, description, status);
        log.log("  createCRecord <OK>");
    }

    void createMRecord(String firstName, String lastName, String address, Date lastDate, String lastLocation, String status) throws RemoteException {
        log.log("createMRecord (" + firstName + " " + lastName + ": " + address + " " + lastLocation + "@" + lastDate.toString() + " [" + status + "])");
        instance.createMRecord(firstName, lastName, address, lastDate, lastLocation, status);
        log.log("  createMRecord <OK>");
    }

    String getRecordCounts() throws RemoteException {
        log.log("getRecordCounts ()");
        String retval = instance.getRecordCounts();
        log.log("  getRecordCounts <OK> " + retval);
        return retval;
    }

    void editCRecord(String lastName, String recordID, String newStatus) throws RemoteException {
        log.log("editCRecord (" + recordID + "("+lastName + ") --> " + newStatus);
        
        instance.editCRecord(lastName, recordID, newStatus);
        
        log.log("  editCRecord <OK>");
    }
    
    void logDump() throws RemoteException {
        log.log("logDump ()");
        instance.logDump();
        log.log("  logDump <OK>");
    }
}
