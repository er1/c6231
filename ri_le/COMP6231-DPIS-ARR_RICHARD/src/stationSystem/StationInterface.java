package stationSystem;

public interface StationInterface {

	public String createCRecord(String badgeId, String firstName,
			String lastName, String description, String status);
	
	public String createMRecord(String badgeId, String firstName,
			String lastName, String address, long lastDate,
			String lastLocation, String status);
	
	public String getRecordCounts(String badgeId);
	
	public String editRecord(String badgeId, String lastName,
			String recordID, String status);
	
	public String transferRecord(String badgeID, String recordID,
			String remoteStationServerName);
	
	
}
