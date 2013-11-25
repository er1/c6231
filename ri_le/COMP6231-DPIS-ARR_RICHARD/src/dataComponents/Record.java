package dataComponents;

public class Record {
	
	public enum StatusType{
		CAPTURED, ON_THE_RUN, FOUND, MISSING
	}
	
	protected String RecordID;
	private String lastName;
	private  String firstName;
	private StatusType status;
	//to have unique ID numbers
	protected static int idCount = 10000;
	protected static Object synchronizedObjectForInstantiation = new Object();
	
	public Record(String firstName, String lastName, StatusType status){
		this.setLastName(lastName);
		this.setFirstName(firstName);
		if(status == null){
			System.out.println("Error: while creating a record, the status was received as null");
			System.exit(0);
		}
		this.setStatus(status); 
	}
	
	protected static StatusType convertStatusString2StatusType(String s){
		if(s.equalsIgnoreCase(StatusType.CAPTURED.toString())){
			return StatusType.CAPTURED;
		}else if(s.equalsIgnoreCase(StatusType.FOUND.toString())){
			return StatusType.FOUND;
		}else if(s.equalsIgnoreCase(StatusType.MISSING.toString())){
			return StatusType.MISSING;
		}else if(s.equalsIgnoreCase(StatusType.ON_THE_RUN.toString()) || s.equalsIgnoreCase("On The Run") || s.equalsIgnoreCase("OnTheRun")){
			return StatusType.ON_THE_RUN;
		}else{
			return null;
		}
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getRecordID() {
		return RecordID;
	}

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}
}
