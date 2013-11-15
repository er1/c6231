package server.report.concrete;

import java.io.Serializable;

import server.report.abstracts.AbstractRecord;


/**
 * Represents an instance of a criminal record
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 *
 */
public class CriminalRecord extends AbstractRecord implements Serializable {

	/**
	 * The description of the criminal
	 */
	private String description = null;
			
	/**
	 * Instantiates an object of this class
	 * 
	 * @param firstName The first name of the criminal
	 * @param lastName The last name of the criminal
	 * @param description The description of the criminal
	 * @param status The status of the criminal 
	 * 
	 */
	protected CriminalRecord(String firstName, String lastName, String description, common.corba.recordInterfaceManager.ReportStatus status) throws InstantiationException 
	{
		// Calling this will properly assign the identification number
		super();
		
		this.firstName = firstName;
		this.description = description;
		this.status = status;
		
		// The assumption here is that the length is > 0 based
		// on the factory functionality
		if(lastName.length() == 1) {
			this.lastName = lastName.toString();
		} else {
			this.lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1);
		}
				
		// Set the records acronym
		this.recordAcronym = RecordAcronym.CR;
	}
	
	public String getDescription() {
		return description;
	}
}