package server.report.interfaces;

import java.io.Serializable;

import javax.management.InvalidAttributeValueException;

/**
 * The interface for generic report functionality
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 *
 */
public interface IRecord extends Serializable {
	
	/**
	 * Gets the records identification
	 * 
	 * @throws InvalidAttributeValueException if the id = -1 or the acronym is not set
	 * @return The records identification
	 */
	public String getId() throws InvalidAttributeValueException;
	
	/**
	 * Gets the numerical identification
	 * 
	 * @return The numerical identification
	 * 
	 * @throws InvalidAttributeValueException
	 */
	int getNumericalID() throws InvalidAttributeValueException;

	/**
	 * Gets the key of the record
	 * 
	 * @return The key
	 */
	public char getKey();
	
	/**
	 * Gets the full name of the person associated to the record
	 * 
	 * @return the full name of the person associated to the record
	 */
	public String getFullName();
	
	/**
	 * Gets the first name
	 * 
	 * @return The first name
	 */
	public String getFirstName();
	
	/**
	 * Gets the last name
	 * 
	 * @return The last name
	 */
	public String getLastName();
	
	/**
	 * Gets the status
	 * @return The status
	 */
	public common.corba.recordInterfaceManager.ReportStatus getStatus(); 
	
	/**
	 * Sets the status of this record
	 * 
	 * @param status The status
	 * 
	 * @return Success or failure
	 */
	public Boolean setStatus(common.corba.recordInterfaceManager.ReportStatus status);
}
