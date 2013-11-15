package c6231.Client;

import java.rmi.RemoteException;
import java.util.Date;

public class WebServiceAdapter {

	String badgeID = null;
	OfficerClient inst = null;

	public WebServiceAdapter() {
	}

	public String createCRecord(String badgeID, String firstName,
			String lastName, String description, String status) {
		try {
			return get(badgeID).createCRecord(firstName, lastName, description,
					status);
		} catch (RemoteException e) {
			return "__FAIL__";
		}
	}

	public String createMRecord(String badgeID, String firstName,
			String lastName, String address, Date lastDate,
			String lastLocation, String status) {
		try {
			return get(badgeID).createMRecord(firstName, lastName, address,
					lastDate, lastLocation, status);
		} catch (RemoteException e) {
			return "__FAIL__";
		}
	}

	public String editCRecord(String badgeID, String lastName, String recordID,
			String newStatus) {
		try {
			return get(badgeID).editCRecord(lastName, recordID, newStatus);
		} catch (RemoteException e) {
			return "__FAIL__";
		}
	}

	protected OfficerClient get(String badgeID) {
		if (this.badgeID != badgeID) {
			this.badgeID = badgeID;
			inst = new OfficerClient(badgeID);
			try {
				inst.connect();
			} catch (RemoteException e) {
				inst = null;
				badgeID = null;
			}
		}
		return inst;
	}

	public String getRecordCounts(String badgeID) {
		try {
			return get(badgeID).getRecordCounts();
		} catch (RemoteException e) {
			return "__FAIL__";
		}
	}

	public String transferRecord(String badgeID, String recordID,
			String remoteStationServerName) {
		try {
			return get(badgeID).transferRecord(badgeID, recordID,
					remoteStationServerName);
		} catch (RemoteException e) {
			return "__FAIL__";
		}
	}

}
