package common.corba.recordInterfaceManager;

/**
 * Interface definition for the remote functionality being given by the server to the client
 * 
 * @author  Daniel Ricci <thedanny09 * @gmail .com>
 * 
 */
public interface IRecordManagerOperations
{
    /**
     * Interface definition for creating a criminal record
     * 
     * @param  badgeId The badge identification of the officer 
     * @param  firstName The first name of the person
     * @param  lastName The last name of the person
     * @param  description The description of the crime
     * @param  status The status of the crime
     * 
     * @return  An identifier reference to the id of the created medical record
     * 
     */
    public String createCRecord(String badgeId, String firstName, String lastName, String description, common.corba.recordInterfaceManager.ReportStatus status);

    /**
     * Interface definition for creating a missing record
     * 
     * @param  badgeId The badge identification of the officer
     * @param  firstName The first name of the person
     * @param  lastName The last name of the person
     * @param  address The address of the missing person
     * @param  lastDate The last date seen of the missing person
     * @param  lastLocation The last location seen of the missing person
     * @param  status The status of the missing person
     * 
     * @return  An identifier reference to the id of the created medical record
     * 
     */
    public String createMRecord(String badgeId, String firstName, String lastName, String address, long lastDate, String lastLocation, common.corba.recordInterfaceManager.ReportStatus status);

    /**
     * Interface definition for editing the status of a record
     * 
     * @param  badgeId The badge identification of the officer
     * @param  lastName The last name used for lookup, the first character is capitalized automatically
     * @param  recordID The record id
     * @param  status The status to change on
     * 
     * @return  A confirmation to the success of the operation
     * 
     */
    public boolean editRecord(String badgeId, String lastName, String recordId, common.corba.recordInterfaceManager.ReportStatus status);

    /**
     * Gets the record count of the current server
     * 
     * @param  badgeId The badge identification of the officer
     * 
     * @return  The string representation of the number of records in the current server
     * 
     */
    public String getRecordCount(String badgeId);

    /**
     * Transfers a record to a specified station
     * 
     * @param  badgeId The badge identification of the officer (This includes the sccronym of the station)
     * @param  recordId The records idenfication (This includes the accronym of the record type)
     * @param  stationName (The name of the station, it doesnt matter if its written in upper or lower we will tolower() + trim() it)
     * 
     * @return  A confirmation to the success of the operation
     * 
     */
    public String transferRecord(String badgeId, String recordId, String stationName);

}
