package server.report.concrete;

import java.util.Date;

import server.report.interfaces.IRecord;

/**
 * The factory that creates references to unique Report objects
 * This class also abstracts the verification of the validity of the
 * initial arguments BEFORE creating the Report
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 *
 */
public class RecordFactory {

	/**
	 * Creates and returns an instance of a Criminal Record
	 * 
	 * @param firstName The first name of the criminal
	 * @param lastName The last name of the criminal
	 * @param description The description of the criminal
	 * @param status The status of the criminal 
	 * 
	 * @return IRecord reference
	 */
	public static IRecord getCriminalRecord(String firstName, String lastName, String description, common.corba.recordInterfaceManager.ReportStatus status) {
		if(isValidName(firstName, lastName) && (status == common.corba.recordInterfaceManager.ReportStatus.CAPTURED || status == common.corba.recordInterfaceManager.ReportStatus.ON_THE_RUN)) {
			try {
				return new CriminalRecord(firstName, lastName, description, status);	
			} catch(InstantiationException ie) {
				System.out.println(ie.getMessage());
				return null;
			}
		} else {
			System.out.println("Could not create the criminal report!");
			return null;
		}
	}
	
	/**
	 * Creates and returns an instance of a Criminal Record
	 * 
	 * @param firstName The first name of the missing person
	 * @param lastName The last name of the missing person
	 * @param lastKnownAddress The last known address of the missing person 
	 * @param dateLastSeen The date last seen of the missing person
	 * @param placeLastSeen The place last seen of the missing person
	 * @param status The status of the missing person
	 * 
	 * @return IRecord reference
	 */
	public static IRecord getMissingRecord(String firstName, String lastName, String lastKnownAddress, long dateLastSeen, String placeLastSeen, common.corba.recordInterfaceManager.ReportStatus status) {
		Date date = new Date(dateLastSeen);
		if(isValidName(firstName, lastName) && (status == common.corba.recordInterfaceManager.ReportStatus.FOUND || status == common.corba.recordInterfaceManager.ReportStatus.MISSING)) {
			try {
				return new MissingRecord(firstName, lastName, lastKnownAddress, date, placeLastSeen, status);
			} catch(InstantiationException ie) {
				System.out.println(ie.getMessage());
				return null;
			}
		} else {
			System.out.println("Could not create the missing record");
			System.out.println(firstName);
			System.out.println(lastName);
			System.out.println(lastKnownAddress);
			System.out.println(dateLastSeen);
			System.out.println(placeLastSeen);
			System.out.println(status);
			return null;
		}
	}
	
	public static IRecord normalizeRecord(IRecord record) {
			if(record.getClass() == CriminalRecord.class) {
				CriminalRecord cr = (CriminalRecord)record;
				return getCriminalRecord(record.getFirstName(), record.getLastName(), cr.getDescription(), record.getStatus());
			} else if(record.getClass() == MissingRecord.class) {
				MissingRecord mr = (MissingRecord)record;
				return getMissingRecord(record.getFirstName(), record.getLastName(), mr.getLastKnownAddress(), mr.dateLastSeen(), mr.placeLastSeen(), record.getStatus());
			} else {
				return null;
			}
	}
	
	/**
	 * Verifies the validity of the first and last name
	 * 
	 * @param firstName The persons first name
	 * @param lastName The persons last name
	 * 
	 * @return Boolean if its successful or not
	 */
	private static Boolean isValidName(String firstName, String lastName) {		
		// Ensure that the first name
		// 1. Is not null
		// 2. Is not empty
		if(firstName == null || firstName.isEmpty()) {
			return false;
		}
		
		// Ensure that the last name
		// 1. Is not null
		// 2. Is not empty
		// 3. The first character is a valid Alphabetical character
		if(lastName == null || lastName.isEmpty() || !Character.isAlphabetic(lastName.charAt(0)))
		{
			return false;
		}
		
		return true;
	}
}
