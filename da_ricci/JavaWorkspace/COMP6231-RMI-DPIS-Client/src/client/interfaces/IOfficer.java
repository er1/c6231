package client.interfaces;

/**
 * Represents the interface for an officer
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 *
 */
public interface IOfficer {
	
	/**
	 * Prototype for creating a criminal record
	 */
	public void CreateCRecord();
	
	/**
	 * Prototype for creating a missing record
	 */
	public void CreateMRecord();
	
	/**
	 * Prototype for editing a record
	 */
	public void EditRecord(boolean automate);
	
	/**
	 * Prototype for getting the record count
	 */
	public void GetRecordCount();
	
	/**
	 * Prototype for transferring a record
	 */
	public void TransferRecord(boolean automate);
}
