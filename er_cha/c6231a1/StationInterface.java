package c6231;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

/**
 *
 * @author chanman
 */
public interface StationInterface extends Remote {

    /**
     * Create a new Criminal Record
     * @param firstName First Name
     * @param lastName Last Name 
     * @param description Description of Criminal
     * @param status Status of Criminal
     * @return Record ID of Record;
     * @throws RemoteException
     */
    String createCRecord(String firstName, String lastName, String description, String status) throws RemoteException;

    /**
     * Create a new Missing Person Record
     * @param firstName First Name
     * @param lastName Last Name
     * @param address Address of Missing Person
     * @param lastDate Date Last Seen
     * @param lastLocation Last Location Seen
     * @param status Status of Missing Person
     * @return Record IF of Record
     * @throws RemoteException
     */
    String createMRecord(String firstName, String lastName, String address, Date lastDate, String lastLocation, String status) throws RemoteException;

    /**
     * Get Record Counts
     * @return String describing the number of records in each station
     * @throws RemoteException
     */
    String getRecordCounts() throws RemoteException;

    /**
     * Modify the Status of a Criminal Record
     * @param lastName Last Name on file
     * @param recordID Record ID
     * @param newStatus New Status
     * @return Record ID is Success, null otherwise
     * @throws RemoteException
     */
    String editCRecord(String lastName, String recordID, String newStatus) throws RemoteException;
    
    /**
     * Log Dump
     * Dump a stations records to its log
     * @throws RemoteException 
     */
    void logDump() throws RemoteException;
}
