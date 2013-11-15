package dataComponents;

public class Record {
	
	public enum StatusType{
		CAPTURED, ON_THE_RUN, FOUND, MISSING
	}
	
	protected String RecordID;
	private String lastName;
	private  String firstName;
	private StatusType status;
	
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
		if(s.equals(StatusType.CAPTURED.toString())){
			return StatusType.CAPTURED;
		}else if(s.equals(StatusType.FOUND.toString())){
			return StatusType.FOUND;
		}else if(s.equals(StatusType.MISSING.toString())){
			return StatusType.MISSING;
		}else if(s.equals(StatusType.ON_THE_RUN.toString())){
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
