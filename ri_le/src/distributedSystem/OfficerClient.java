package distributedSystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import org.omg.CORBA.ORB;

import stationServerIDL.StationInterface;
import stationServerIDL.StationInterfaceHelper;
import dataComponents.Record.StatusType;

public class OfficerClient extends Thread{
	
	// Badge id that is using this officerClient
	private String BadgeID;

	// the associated server station that this officerClient should be connected to
	private StationInterface associatedStation;
	
	// data members used to log the information of the server transaction
	// used http://stackoverflow.com/questions/2885173/java-how-to-create-and-write-to-a-file
	// as a reference
	private Writer officerLogFile = null;
	
	/**
	 *  OfficerClient constructor which sets up the associated server
	 *  station interface with the officer BadgeID
	 *  @param stationName		name of the station server
	 *  @param badgeID			the badge ID of the officer using this client
	 * @throws IOException 
	 */
	public OfficerClient(
			String stationName,
			String badgeID,
			String ior_file) throws IOException
	{
		BadgeID = badgeID;
		
		//random array string for the init
		String args[] = null;
		ORB orb = ORB.init(args, null);
		
		BufferedReader br = new BufferedReader(new FileReader(ior_file));
		String ior = br.readLine();
		br.close();
		
		org.omg.CORBA.Object o = orb.string_to_object(ior);
		associatedStation = StationInterfaceHelper.narrow(o);
		
		// set up the logging of the information of this officer client
		try {
			//create the log file
			officerLogFile = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream("bin/" + BadgeID + ".txt"), "utf-8")); 
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("OfficerClient constructor error");
			System.exit(1);
		}
	}
	
	/**
	 *  Log the the passed String s with
	 *  the date +  badgeID + "\n" +
	 *  s
	 *  @param s		The String to be appended with the time stamp and the badge ID
	 */
	private void log(String s)
	{
		try {
			officerLogFile.write( (new Date()).toString() + "\t<" + (new Date()).getTime() + ">\n\t" +
									s + "\n\t\t...");
			officerLogFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  Take the String s as is without appending or formating extra information
	 *  @param s		The String to be put in the log files
	 */
	private void logNoFormat(String s)
	{
		try {
			officerLogFile.write(s);
			officerLogFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  After officer client sends a request, we make use of this function to
	 *  log whether or not the transaction was successful or not 
	 *  @param b
	 */
	private void logResult(boolean b)
	{
		try {
			String outputResult;
			if(b){
				outputResult = "SUCCESS";
			}else{
				outputResult = "FAILURE";
			}
			officerLogFile.write(outputResult + " " + (new Date()).toString() + "\t<" + (new Date()).getTime() + ">\n\n");
			officerLogFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  create a criminal record with the associated server.
	 *  get the transaction result and log it 
	 *  @param firstName	first name of the criminal
	 *  @param lastName		last name of the criminal
	 *  @param description	description of his/her crime
	 *  @param status		the status of the criminal
	 *  @return				return whether or not the transaction was successful
	 */
	public boolean createCRecord(
			String firstName,
			String lastName,
			String description,
			StatusType status)
	{
		boolean result;
		
		log("tries to create a criminal record:" + 
			"\n\t\tName:  \t" + firstName + " " + lastName + 
			"\n\t\tDescr.:\t" + description +
			"\n\t\tStatus:\t" + status);
		
		result = associatedStation.createCRecord(BadgeID, firstName, lastName, description, status.toString());
		
		logResult(result);
		
		return result;
	}
	
	/**
	 *  create a missing record with the associated server.
	 *  get the transaction result and log it.
	 *  @param firstName		first name of the missing person
	 *  @param lastName			last name of the missing person
	 *  @param address			address of the missing person
	 *  @param lastDate			last date the missing person was last seen
	 *  @param lastLocation		last location the missing person was last seen
	 *  @param status			the status of the missing person
	 *  @return					return whether or not the transaction was successful
	 */
	public boolean createMRecord(
			String firstName,
			String lastName,
			String address,
			Date lastDate,
			String lastLocation,
			StatusType status)
	{
		boolean result;
		
		log("tries to create a missing record:"+
			"\n\t\tName:  \t" + firstName + " " + lastName +
			"\n\t\tAddr:  \t" + address +
			"\n\t\tSeen:  \t" + lastDate +
			"\n\t\t       \t" + lastLocation +
			"\n\t\tStatus:\t" + status);
		
		result = associatedStation.createMRecord(
				BadgeID,
				firstName,
				lastName,
				address,
				lastDate.toString(),
				lastLocation,
				status.toString());
		
		logResult(result);
		
		return result;
	}
	
	/**
	 *  Get the record count of all the servers
	 *  @return		returns the each server name followed by the number of record
	 */
	public String getCRecordCounts(){
		log("tries to get criminal record count");
		
		String result = associatedStation.getRecordCounts(BadgeID);
		
		// not the best way to check if the received results was good or not,
		// but it will suffice for now 
		if(result.contains("SPVM") && result.contains("SPL") && result.contains("SPB")){
			logNoFormat(result + "\n");
			logResult(true);
		}else{
			logResult(false);
		}
		return result;
	}
	
	/**
	 *  Edit the record's status with the associated person's last name and the record's RecordID
	 *  @param lastName		Last name of the
	 *  @param recordID		RecordID of the record with associated person 
	 *  @param status		new status to be put in the record
	 *  @return				return whether or not the transaction was successful
	 */
	public boolean editCRecord(String lastName, String recordID, StatusType status){
		boolean result;
		
		log("tries to edit a record:"+
				"\n\t\tName:    \t" + lastName +
				"\n\t\trecordID:\t" + recordID +
				"\n\t\tStatus:  \t" + status);
		
		result = associatedStation.editRecord(BadgeID, lastName, recordID, status.toString());
		
		logResult(result);
		
		return result;
	}
	
	/**
	 * transfer a record by its recordID to a station. The station is referenced by its name
	 * @param recordID					RecordID of the record to be transferred
	 * @param remoteStationServerName	Name of the station to be transferred
	 * @return
	 */
	public boolean transferRecord(String recordID, String remoteStationServerName) {
		boolean result;
		
		log("tries to transfer a record:"+
				"\n\t\trecordID: " + recordID +
				"\n\t\tto:       " + remoteStationServerName);
		
		result = associatedStation.transferRecord(BadgeID, recordID, remoteStationServerName);
		
		logResult(result);
		
		return result;
	}
	
}
