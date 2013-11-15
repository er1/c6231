package dataComponents;

import stationServerIDL.Upcasted_Record;

public class CriminalRecord extends Record{

	public CriminalRecord(String firstName, String lastName, String description, StatusType status) {
		super(firstName, lastName, status);
		this.setDescription(description);
		
		if(status == StatusType.MISSING || status == StatusType.FOUND){
			System.err.println("Error: A CriminalRecord cannot have a MISSING or FOUND status");
			System.exit(0);
		}
		
		synchronized (synchronizedObjectForInstantiation){
			if(idCount == 100000){
				System.out.println("Error: Maximum criminal records have been created");
				System.exit(1);
			}
			RecordID = "CR"+idCount;
			idCount++;
		}
	}
	
	
	/**
	 * This constructor copies the interested contents of theRecord
	 * @param theRecord
	 */
	public CriminalRecord(Upcasted_Record theRecord) {
		super(theRecord.firstName, theRecord.lastName, convertStatusString2StatusType(theRecord.status));
		this.description = theRecord.description;
		this.RecordID = theRecord.RecordID;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	//to have unique ID numbers
	private static int idCount = 10000;
	private String description;
	
	private static Object synchronizedObjectForInstantiation = new Object();
}
