package server.report.abstracts;

import javax.management.InvalidAttributeValueException;

import common.corba.recordInterfaceManager.ReportStatus;

import server.report.concrete.CriminalRecord;
import server.report.concrete.MissingRecord;
import server.report.interfaces.IRecord;

/**
 * Abstract implementation of the record functionality
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 * 
 */
public abstract class AbstractRecord implements IRecord 
{ 	
	/**
	 * The record count for all the records
	 */
	protected static int recordCount = 10000;
	
	/**
	 * The record id
	 */
	protected int recordId = -1;
			
	/**
	 * The first name of the record representing the person
	 */
	protected String firstName = null;
		
	/**
	 * The last name of the record representing the person
	 */
	protected String lastName = null;
	
	/**
	 * The status of the record representing the person
	 */
	protected common.corba.recordInterfaceManager.ReportStatus status = null;	
	
	/**
	 * The acronyms of a record, part of the identification
	 * Eg: CR10000, MR10001
	 * 
	 * @author Daniel Ricci <thedanny09@gmail.com>
	 * @version Build 1
	 *
	 */
	protected enum RecordAcronym {
		CR,
		MR
	}
	
	/**
	 * The acronym of the record
	 */
	protected RecordAcronym recordAcronym = null;
	
	/**
	 * Properly assigns the record identifier
	 */
	protected AbstractRecord() throws InstantiationException {
		synchronized(this) {
			if(recordCount < 10000 || recordCount > 99999) {
				throw new InstantiationException("Could not create this record because the identification number has reached its maximum limit");
			}
			recordId = recordCount;
			++recordCount;
		}
	}
	
	/**
	 * Gets the identification of the record
	 * 
	 * @throws InvalidAttributeValueException if the id = -1 or the acronym is not set
	 * @return The identification of the record
	 */
	public String getId() throws InvalidAttributeValueException {
		if(recordAcronym == null || recordId > 99999 || recordId < 10000) {
			System.out.println("Error, the records accronym was not properly set");
			throw new InvalidAttributeValueException("Error, the records accronym was not properly set");
		}
		return (recordAcronym.name() + recordId);
	}
	
	public int getNumericalID() throws InvalidAttributeValueException {
		if(recordId == -1) {
			System.out.println("Error, the records accronym was not properly set");
			throw new InvalidAttributeValueException("Error, the records accronym was not properly set");
		}
		return recordId;
	}
	
	/**
	 * Gets the key 
	 */
	public char getKey() {
		return lastName.charAt(0);
	}
	
	/**
	 * Gets the full name
	 */
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	/**
	 * Sets the status of this record
	 */
	public Boolean setStatus(common.corba.recordInterfaceManager.ReportStatus status) {
		if(MissingRecord.class == this.getClass()) {
			if(status == common.corba.recordInterfaceManager.ReportStatus.MISSING || status == common.corba.recordInterfaceManager.ReportStatus.FOUND) {
				this.status = status;
				return true;
			}
		} else if(CriminalRecord.class == this.getClass()) {
			if(status == common.corba.recordInterfaceManager.ReportStatus.CAPTURED || status == common.corba.recordInterfaceManager.ReportStatus.ON_THE_RUN) {
				this.status = status;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the first name
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Gets the last name
	 */
	public String getLastName() {
		return lastName;
	}

	@Override
	public ReportStatus getStatus() {
		return status;
	}
}