package c6231;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StationInterface extends Remote {

    String createCRecord(String firstName, String lastName, String description, String status) throws RemoteException;

    String createMRecord(String firstName, String lastName, String address, String lastDate, String lastLocation, String status) throws RemoteException;

    String editCRecord(String lastName, String recordID, String newStatus) throws RemoteException;

    String getRecordCounts() throws RemoteException;

    String transferRecord(String badgeID, String recordID, String remoteStationServerName) throws RemoteException;
}