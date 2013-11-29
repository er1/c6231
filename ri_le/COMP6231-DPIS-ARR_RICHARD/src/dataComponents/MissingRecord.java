package dataComponents;

import stationServerIDL.Upcasted_Record;

public class MissingRecord extends Record{

	public MissingRecord(String firstName, String lastName, String address, long lastDate, String lastLocation, StatusType status) {
		super(firstName, lastName, status);
		this.setAddress(address);
		this.setLastDate(lastDate);
		this.setLastLocation(lastLocation);
		
		if(status == StatusType.ON_THE_RUN|| status == StatusType.CAPTURED){
			System.err.println("Error: A CriminalRecord cannot have a MISSING or FOUND status");
			System.exit(0);
		}
		
		synchronized(synchronizedObjectForInstantiation){
			if(idCount == 100000){
				System.out.println("Error: Maximum criminal records have been created");
				System.exit(1);
			}
			RecordID = "MR"+idCount;
			idCount++;
		}
	}
	
	/**
	 * This constructor copies the interested contents of theRecord
	 * @param theRecord
	 */
	@SuppressWarnings("deprecation")
	public MissingRecord(Upcasted_Record theRecord) {
		super(theRecord.firstName, theRecord.lastName, convertStatusString2StatusType(theRecord.status));
		this.address = theRecord.address;
		this.lastDate = theRecord.lastDate;
		this.lastLocation = theRecord.lastLocation;		
		synchronized(synchronizedObjectForInstantiation){
			if(idCount == 100000){
				System.out.println("Error: Maximum criminal records have been created");
				System.exit(1);
			}
			RecordID = "MR"+idCount;
			idCount++;
		}
	}

	public long getLastDate() {
		return lastDate;
	}

	public void setLastDate(long lastDate) {
		this.lastDate = lastDate;
	}

	public String getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(String lastLocation) {
		this.lastLocation = lastLocation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	private String address;
	private long lastDate;
	private String lastLocation;
	
}
