package c6231;

public interface StationInterface {

    String createCRecord(String firstName, String lastName, String description, String status);

    String createMRecord(String firstName, String lastName, String address, long lastDate, String lastLocation, String status);

    String editCRecord(String lastName, String recordID, String newStatus);

    String getRecordCounts();

    String transferRecord(String badgeID, String recordID, String remoteStationServerName);
}
