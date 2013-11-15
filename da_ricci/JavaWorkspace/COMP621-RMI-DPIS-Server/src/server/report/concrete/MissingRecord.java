package server.report.concrete;

import java.io.Serializable;
import java.util.Date;

import common.corba.recordInterfaceManager.ReportStatus;
import server.report.abstracts.AbstractRecord;

/**
 * Represents an instance of a missing record
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 *
 */
public class MissingRecord extends AbstractRecord implements Serializable {
	
	/**
	 * The last known address of the missing person
	 */
	private String lastKnownAddress = null;
	
	/**
	 * The date last seen of the missing person
	 */
	private Date dateLastSeen = null;
	
	/**
	 * The place last seen of the missing person
	 */
	private String placeLastSeen = null;
	
	/**
	 * /**
	 * Instantiates an object of this class
	 * 
	 * @param firstName The first name of the missing person
	 * @param lastName The last name of the missing person
	 * @param lastKnownAddress The last known address of the missing person 
	 * @param dateLastSeen The date last seen of the missing person
	 * @param placeLastSeen The place last seen of the missing person
	 * @param status The status of the missing person
	 * 
	 */
	protected MissingRecord(String firstName, String lastName, String lastKnownAddress, Date dateLastSeen, String placeLastSeen, common.corba.recordInterfaceManager.ReportStatus status) throws InstantiationException 
	{
		// Assigns the identification number
		super();
		
		//TODO - Normalize the first letter of the last name (A vs a, Z vs z, etc)
		this.firstName = firstName;
		
		if(lastName.length() == 1) {
			this.lastName = lastName.toString();
		} else {
			this.lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1);
		}
		
		this.lastKnownAddress = lastKnownAddress;
		this.dateLastSeen = dateLastSeen;
		this.placeLastSeen = placeLastSeen;
		this.status = status;
		
		// Set the records acronym
		this.recordAcronym = RecordAcronym.MR;
	}

	/**
	 * Gets the last known address
	 * 
	 * @return the last known address
	 * 
	 */
	public String getLastKnownAddress() {
		return lastKnownAddress;
	}

	/**
	 * Gets the date last seen
	 * 
	 * @return The date last seen
	 */
	public long dateLastSeen() {
		return dateLastSeen.getTime();
	}

	/**
	 * Gets the place last seen
	 * 
	 * @return The place last seen
	 */
	public String placeLastSeen() {
		// TODO Auto-generated method stub
		return null;
	}
}
