package stationServerIDL;

/**
 * Interface definition: StationInterface.
 * 
 * @author OpenORB Compiler
 */
public interface StationInterfaceOperations
{
    /**
     * Operation createCRecord
     */
    public boolean createCRecord(String badgeId, String firstName, String lastName, String description, String status);

    /**
     * Operation createMRecord
     */
    public boolean createMRecord(String badgeId, String firstName, String lastName, String address, String lastDate, String lastLocation, String status);

    /**
     * Operation getRecordCounts
     */
    public String getRecordCounts(String badgeId);

    /**
     * Operation editCRecord
     */
    public boolean editRecord(String badgeId, String lastName, String recordID, String status);

    /**
     * Operation transferRecord
     */
    public boolean transferRecord(String badgeID, String recordID, String remoteStationServerName);

    /**
     * Operation transfer
     */
    public boolean transfer(stationServerIDL.Upcasted_Record theRecord);

}
