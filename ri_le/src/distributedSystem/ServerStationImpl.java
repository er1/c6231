package distributedSystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import org.omg.CORBA.ORB;

import stationServerIDL.StationInterface;
import stationServerIDL.StationInterfaceHelper;
import stationServerIDL.StationInterfacePOA;
import stationServerIDL.Upcasted_Record;
import dataComponents.CriminalRecord;
import dataComponents.MissingRecord;
import dataComponents.Record;
import dataComponents.Record.StatusType;

public class ServerStationImpl extends StationInterfacePOA{
	
	//Records has the hash table to store the records with
	// the last name of the record as the key and
	// the record itself as the value
	// used http://mindprod.com/jgloss/hashtable.html
	// as a reference for duplicate keys
	private Hashtable<String, ArrayList<Record> > Records =
			new Hashtable<String, ArrayList<Record> >();
	
	// String to store the name of this StationServer object
	private String stationName;
	
	// data members used to log the information of the server transaction
	// used http://stackoverflow.com/questions/2885173/java-how-to-create-and-write-to-a-file
	// as a reference
	private Writer serverLogFile = null;
	
	/** 
	 *  keep track the udpPortNumber of the StationServer object
	 */
	private UDPServer udpServer;
	private int udpPortNumber;
	
	/**
	 * transfer synchronization to prevent context switching when iterating through the hash table
	 */
	private Object transferRecordSynchronizedObject = new Object();
	
	/**
	 *  defines the UDP port
	 *  this is used like the #define of C / C++
	 */
	public static final int SPVM_PORT_NUMBER = 15140;
	public static final int SPL_PORT_NUMBER = 14500;
	public static final int SPB_PORT_NUMBER = 15790;
	
	/**
	 *  constructor of the ServerStation
	 *  this also prevent the use of the default constructor
	 *  because we need to know the name of the server and its UDP port number
	 *  @param stationName		the name of the station
	 *  @param udpPortNumber	the port number used for the UDP server 
	 */
	public ServerStationImpl(String stationName, int udpPortNumber){
		
		// copy the station name and udp port number
		this.stationName = stationName;
		this.udpPortNumber = udpPortNumber;
		
		// start the run thread
		udpServer = new UDPServer(stationName, udpPortNumber);
		
		try {
			//create the log file
			serverLogFile = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream("bin/" + stationName + ".txt"), "utf-8")); 
			
			log("StationServer " + stationName + " is up and running");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("StationServer constructor error");
			System.exit(1);
		}
	}
	
	private void log(String s){
		try {
			serverLogFile.write( (new Date()).toString() + "\t<" + (new Date()).getTime() + ">\n\t" + s + "\n\n");
			serverLogFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  private function used only by createCRecord and createMRecord
	 *  @param r
	 */
	private void insertRecordToHashtable(Record r){
		//get the first letter of lastName
		String firstLetterOfLastName = r.getLastName().substring(0, 1);
		
		//add record to Records accordingly
		ArrayList<Record> recordList = Records.get(firstLetterOfLastName);
		
		//if that key has not been assignment yet, the return value of the hash table is null
		if(recordList == null){
			synchronized (Records) {
				//if so, create a new array list with the record 
				recordList = new ArrayList<Record>();
				recordList.add(r);
				
				//and put new entry in the hash table
				Records.put(firstLetterOfLastName, recordList);
			}
		}else{//that key exists already
			//entry already exist in hash table, just modify the value <V> recordList
			synchronized (recordList) {
				recordList.add(r);
			}
		}
		udpServer.incRecordsCount();
	}
	
	private StatusType convertStatusString2StatusType(String s){
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
	
	@SuppressWarnings("deprecation")
	private Date convertDateString2Date(String d){
		return new Date(d);
	}

	/**
	 * Creates a criminal record
	 *  @param	badgeId			The officer's BadgeID who is calling this function
	 *  @param	firstName		First name of the criminal
	 *  @param	lastName		Last name of the criminal
	 *  @param	description		The description of his/her crime
	 *  @param	status			The status of this criminal
	 *  						it HAS to be either StatusType.ON_THE_RUN or Status.CAPTURED
	 *  @return	boolean			true if the criminal record has been successfully added to the hash table
	 *  						false if there are errors trying to add the record with the given parameters
	 */
	@Override
	public boolean createCRecord(String badgeId, String firstName,
			String lastName, String description, String status) {
		//convert the string status back to an enum
		StatusType convertedStatus = convertStatusString2StatusType(status);
		//if it's null return error
		if(convertedStatus == null){
			System.out.println("Error in createCRecord: passed parameter 5 status is not of type StatusType");
			log(badgeId + " has FAILED to add a criminal record due to parameters: " +
					"\n\t\tName:  \t" + firstName + " " + lastName +
					"\n\t\tDescr.:\t" + description +
					"\n\t\tStatus:\t" + status);
			return false;
		}
		try{
			// check that the StatusType is correct and that there is a first and last name
			if(convertedStatus == StatusType.MISSING || convertedStatus == StatusType.FOUND ||
					firstName.equals("") || lastName.equals("")){
				log(badgeId + " has FAILED to add a criminal record due to parameters: " +
						"\n\t\tName:  \t" + firstName + " " + lastName +
						"\n\t\tDescr.:\t" + description +
						"\n\t\tStatus:\t" + status);
				return false;
			}
			
			// if the above passed, we can create a criminal record
			Record r = new CriminalRecord(firstName, lastName, description, convertedStatus);
			
			// insert the record into the hash table
			insertRecordToHashtable(r);
			
			/*System.out.println("Criminal record added: ");
			System.out.println("\t" + r.getRecordID() + "\t" + r.getStatus().toString() +
					"\t" + r.getFirstName() + " " + r.getLastName());*/
			log(badgeId + " has added a criminal record: " +
					"\n\t\tRecord:\t" + r.getRecordID() + " " + r.getStatus() +
					"\n\t\tName:  \t" + firstName + " " + lastName +
					"\n\t\tDescr.:\t" + description);
			return true;
		}catch(Exception e){
			//System.out.println("createCRecord: " + stationName + " could not create the criminal record.");
			e.printStackTrace(new PrintWriter(serverLogFile));
			log(badgeId + " has FAILED to add a criminal record due to exception: " +
					"\n\t\tName:  \t" + firstName + " " + lastName +
					"\n\t\tDescr.:\t" + description +
					"\n\t\tStatus:\t" + status);
			return false;
		}
	}

	/**
	 *  Creates a Missing Record
	 *  function so only the one client can call this function at a time
	 *  @param	badgeId			The officer's BadgeID who is calling this function
	 *  @param	firstName		First name of the missing person
	 *  @param	lastName		Last name of the missing person
	 *  @param	address			The last known address
	 *  @param	lastDate		The last date he/she has been seen
	 *  @param	lastLocation	the last location he/she has been seen
	 *  @param	status			The status of this missing person
	 *  						it HAS to be either StatusType.MISSING or Status.FOUND
	 *  @return	boolean			true if the missing record has been successfully added to the hash table
	 *  						false if there are errors trying to add the record with the given parameters
	 */
	@Override
	public boolean createMRecord(String badgeId, String firstName,
			String lastName, String address, String lastDate,
			String lastLocation, String status) {
		//convert the string status back to an enum
		StatusType convertedStatus = convertStatusString2StatusType(status);
		//convert the string lastDate back to a Date
		Date convertedDate = convertDateString2Date(lastDate);
		//if it's null return error
		if(convertedStatus == null){
			log(badgeId + " has FAILED to add a missing record due to parameters: " +
					"\n\t\tName:  \t" + firstName + " " + lastName +
					"\n\t\tAddr:  \t" + address +
					"\n\t\tSeen:  \t" + convertedDate +
					"\n\t\t       \t" + lastLocation +
					"\n\t\tStatus:\t" + status);
			return false;
		}

		try{
			//check that the StatusType is correct and that there is a first and last name
			if(convertedStatus == StatusType.CAPTURED || convertedStatus == StatusType.ON_THE_RUN ||
					firstName.equals("") || lastName.equals("")){
				log(badgeId + " has FAILED to add a missing record due to parameters: " +
						"\n\t\tName:  \t" + firstName + " " + lastName +
						"\n\t\tAddr:  \t" + address +
						"\n\t\tSeen:  \t" + convertedDate +
						"\n\t\t       \t" + lastLocation +
						"\n\t\tStatus:\t" + convertedStatus);
				return false;
			}
			
			// if the above passed, we can create a missing record
			Record r = new MissingRecord(firstName, lastName, address, convertedDate, lastLocation, convertedStatus);
			
			// insert the record into the hash table
			insertRecordToHashtable(r);
			
			/*System.out.println("Missing record added: ");
			System.out.println("\t" + r.getRecordID() + "\t" + r.getStatus().toString() + 
					"\t" + r.getFirstName() + " " + r.getLastName());*/
			log(badgeId + " has added a missing record: " +
					"\n\t\tRecord:\t" + r.getRecordID() + " " + r.getStatus() +
					"\n\t\tName:  \t" + firstName + " " + lastName +
					"\n\t\tAddr:  \t" + address +
					"\n\t\tSeen:  \t" + convertedDate +
					"\n\t\t       \t" + lastLocation);
			return true;
		}catch(Exception e){
			//System.out.println("createMRecord: " + stationName + " could not create a missing record.");
			e.printStackTrace(new PrintWriter(serverLogFile));
			log(badgeId + " has FAILED to add a missing record due to an exception: " +
					"\n\t\tName:  \t" + firstName + " " + lastName +
					"\n\t\tAddr:  \t" + address +
					"\n\t\tSeen:  \t" + convertedDate +
					"\n\t\t       \t" + lastLocation +
					"\n\t\tStatus:\t" + status);
			return false;
		}
	}
	
	/**
	 *  Gets the record count of all the stations 
	 *  @param	badgeId		The officer's BadgeID who is calling this function
	 *  @return	String		returns a string of all the station names and their record count
	 *  					ie: SPVM 2, SPL 4, SPB 3
	 */
	@Override
	public String getRecordCounts(String badgeId) {
		log(badgeId + " is requesting getRecordCounts");
		// keep the return output in returnOutput. this is reset at the beginning of this function count 
		String returnOutput = "";
		
		// get all the servers' port number and and start getting the information of each of them
		DatagramSocket aSocket = null;
		try{
			aSocket = new DatagramSocket();
			
			InetAddress aHost = InetAddress.getByName("localhost");
			
			ArrayList<Integer> serverPorts = new ArrayList<Integer>();
			serverPorts.add(SPVM_PORT_NUMBER);
			serverPorts.add(SPL_PORT_NUMBER);
			serverPorts.add(SPB_PORT_NUMBER);
			
			for(int i = 0; i < serverPorts.size(); i++)
			{
				// if looking for itself, don't send a UDP message since we can get the results directly
				if(udpPortNumber == serverPorts.get(i)){
					// append the results of getCurrentServerRecordsCount to returnOutput
					returnOutput += udpServer.getCurrentServerRecordsCount();
				}else{
					byte []m = returnOutput.getBytes();
					
					// pass the returnOutput string so that the other server may append to it
					DatagramPacket request = 
							new DatagramPacket(m, returnOutput.length(), aHost, serverPorts.get(i));
					aSocket.send(request);
					
					// get the reply back from the server with the appended information
					byte[] buffer = new byte[1000];
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(reply);
					
					//convert the replied data back into the returnOutput string
					returnOutput = new String(reply.getData(), 0, reply.getLength());
				}
				// append a comma<,> only if it is not the last server to get the record count from
				if(i != serverPorts.size() - 1){
					returnOutput += ", ";
				}
			}//end for loop
			
		}catch(SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}catch(IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally{
			if(aSocket != null)
				aSocket.close();
		}
		log(badgeId + " is receiving getRecordCounts output: " +
				"\n\t\t" + returnOutput);
		return returnOutput;
	}

	/**
	 * 	 edit a record
	 *  @param	badgeId			The officer's BadgeID who is calling this function
	 *  @param	lastName		The last name of the associated person's record to be modified
	 *  @param	recordID		The record ID of the record
	 *  @param	newStatus		The status of which the record will be modified.
	 *  						It HAS to be either StatusType.ON_THE_RUN or Status.CAPTURED for CriminalRecord.
	 *  						It HAS to be either StatusType.MISSING or Status.FOUND for MissingRecord
	 *  @return boolean			Returns true only if the record was successfully modified
	 *  						even if the newStatus is the same as the old status
	 *  						Returns false if:
	 *  						the record was not found with the given parameters OR
	 *    						the status is not a valid status for a criminal

	 */
	@Override
	public boolean editRecord(String badgeId, String lastName,
			String recordID, String status) {
		//convert the string status back to an enum
		StatusType convertedStatus = convertStatusString2StatusType(status);
		
		if(
			// cannot be empty last name or empty recordID
			lastName.equals("") || recordID.equals("") ||
			// HAS to have the right parameters
			!(
			( (convertedStatus == StatusType.MISSING || convertedStatus == StatusType.FOUND) && recordID.contains("MR")) ||
			( (convertedStatus == StatusType.CAPTURED || convertedStatus == StatusType.ON_THE_RUN) && recordID.contains("CR"))
			)){
			log(badgeId + " has FAILED to modify a record due to parameters: " +
					"\n\t\tRecord:\t" + recordID + " " + lastName +
					"\n\t\tStatus:\t" + convertedStatus);
			return false;
		}
		
		// get the first letter of the last name
		String firstLetterofLastName = lastName.substring(0, 1);
		
		// check the hash table with the first letter of the last name as the key 
		ArrayList<Record> records = Records.get(firstLetterofLastName);
		
		// if the record was not found with this key, return false
		if(records == null){
			log(badgeId + " has FAILED to modify a record due to last name (key): " +
					"\n\t\tRecord:\t" + recordID + " " + lastName +
					"\n\t\tStatus:\t" + convertedStatus);
			return false;
		}
		
		boolean found = false;
		int foundIndex = -1;
		
		// check the array list for the provided recordID and last name
		for(int i = 0; i < records.size(); i++){
			if(records.get(i).getRecordID().equals(recordID) &&
				records.get(i).getLastName().equals(lastName)){
				foundIndex = i;
				found = true;
				break;
			}
		}
		
		// if the record was not found the array list, return false
		if(found == false || foundIndex == -1){
			log(badgeId + " has FAILED to modify a record due \"not found\": " +
					"\n\t\tRecord:\t" + recordID + " " + lastName +
					"\n\t\tStatus:\t" + convertedStatus);
			return false;
		}
		
		// set the new status for the found record
		synchronized(records.get(foundIndex)){
			records.get(foundIndex).setStatus(convertedStatus);
		}
		log(badgeId + " has SUCCESSFULLY modified a record: " +
				"\n\t\tRecord:\t" + records.get(foundIndex).getRecordID() + " "
					+ records.get(foundIndex).getLastName() +
				"\n\t\tStatus:\t" + convertedStatus);
		return true;
	}

	/**
	 * Transfer record recordID by officer badgeID to station remoteStationServerName
	 * Note that this function will make the current server act as a client
	 * @param badgeID					The officer's BadgeID who is calling this function
	 * @param recordID					The to be transferred record's RecordID
	 * @param remoteStationServerName	The station name which the record is to be transferred  
	 * @return boolean					returns true only if the record was correctly transferred
	 */
	@Override
	public boolean transferRecord(String badgeID, String recordID,
			String remoteStationServerName) {
		// cannot transfer to itself
		if(remoteStationServerName.equals(stationName)){
			log(badgeID + " has FAILED to transfer a record from " + stationName + " to " + remoteStationServerName +
				"\n\t\tRecord: " + recordID);
			return false;
		}
		
		// null array string for the ORB.init function
		String args[] = null;
		ORB orb = ORB.init(args, null);
		
		BufferedReader br;
		
		// create a new file for the server
		// it must be one of the assignment specification's servers
		try{
			if(remoteStationServerName.equals("SPVM")){
				br = new BufferedReader(new FileReader("spvm_ior.txt"));
			} else if(remoteStationServerName.equals("SPL")){
				br = new BufferedReader(new FileReader("spl_ior.txt"));
			} else if(remoteStationServerName.equals("SPB")){
				br = new BufferedReader(new FileReader("spb_ior.txt"));
			}else{
				log(badgeID + " has FAILED to transfer a record from " + stationName + " to " + remoteStationServerName +
						"\n\t\tRecord: " + recordID);
					return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			log(badgeID + " has FAILED to transfer a record from " + stationName + " to " + remoteStationServerName +
					"\n\t\tRecord: " + recordID);
			return false;
		}
		
		String ior;
		try {
			ior = br.readLine();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			log(badgeID + " has FAILED to transfer a record from " + stationName + " to " + remoteStationServerName +
					"\n\t\tRecord: " + recordID);
			return false;
		}
		
		// get the Interoperable Object Reference in CORBA object
		org.omg.CORBA.Object o = orb.string_to_object(ior);
		
		// convert the CORBA object as a Java Object
		StationInterface newStation = StationInterfaceHelper.narrow(o);
		
		// assume the record is not found yet
		boolean foundRecord = false;
		Record toBeTransferredRecord = null;
		
		// get all records of the hash table 
		Enumeration<ArrayList<Record> > allArrayListsFromRecords = Records.elements();
		
		// go through the records since we don't have a key <k>, last name, to search
		synchronized(transferRecordSynchronizedObject){
			while(allArrayListsFromRecords.hasMoreElements()){
				// for all elements in the hash table,
				ArrayList<Record> recordsList = allArrayListsFromRecords.nextElement();
				// check the ArrayList <v>
				for(int i = 0; i < recordsList.size(); i++){
					// don't allow someone else try to transfer or modify it
					synchronized(recordsList.get(i)){
						// if one of the record's ID matches, then we have found the record
						if(recordsList.get(i).getRecordID().equals(recordID)){
							foundRecord = true;
						
							toBeTransferredRecord = recordsList.get(i);
							recordsList.remove(toBeTransferredRecord);
							// don't decrement yet or else output will be confusing
							// also because technically it's still on the server
							// udpServer.decRecordsCount(); // don't do this yet
						}
					}
				}
				if(foundRecord == true) break;
			}
		}
		
		// if no record was found, log failure and return false
		if(foundRecord == false || toBeTransferredRecord == null){
			log(badgeID + " has FAILED to transfer a record due to record ID not found " +
					"\n\t\tRecord: " + recordID);
			return false;
		}
		
		// at this point the record checks out and has been removed from the hash table
		// we need to up cast the record to transfer it
		Upcasted_Record upRecord = null;
		if(toBeTransferredRecord.getRecordID().contains("MR")){
			upRecord = new Upcasted_Record(
					toBeTransferredRecord.getRecordID(),
					toBeTransferredRecord.getLastName(),
					toBeTransferredRecord.getFirstName(),
					toBeTransferredRecord.getStatus().toString(),
					"",
					((MissingRecord)toBeTransferredRecord).getAddress(),
					((MissingRecord)toBeTransferredRecord).getLastDate().toString(),
					((MissingRecord)toBeTransferredRecord).getLastLocation());
		}else if(toBeTransferredRecord.getRecordID().contains("CR")){
			upRecord = new Upcasted_Record(
					toBeTransferredRecord.getRecordID(),
					toBeTransferredRecord.getLastName(),
					toBeTransferredRecord.getFirstName(),
					toBeTransferredRecord.getStatus().toString(),
					((CriminalRecord)toBeTransferredRecord).getDescription(),
					"", "", "");
		}
		
		// calling the ORB server station so it locally transfers it 
		boolean transactionResult = newStation.transfer(upRecord);
		
		// decrement the record count assuming it has been transferred
		udpServer.decRecordsCount();
		
		// if the transfer failed, put the record back
		if(transactionResult == false){
			log("transferRecord function has failed, please verify the logs");
			// reinsert record
			insertRecordToHashtable(toBeTransferredRecord);
			return false;
		}
		
		log(badgeID + " has SUCCESSFULLY transferred a record" +
				"\n\t\tRecord:      " + recordID +
				"\n\t\tName:        " + toBeTransferredRecord.getFirstName() + " " + toBeTransferredRecord.getLastName() +
				"\n\t\tDestination: " + remoteStationServerName +
				"\n\t\tStatus:      " + toBeTransferredRecord.getStatus());
		return true;
	}

	/**
	 *  This is called remotely by CORBA. The passed Upcasted_Record will be converted to the correct record type
	 *  @param	theRecord	The up-casted record with all information
	 *  @return boolean		returns true only if the record was correctly received	 
	 */
	@Override
	public boolean transfer(Upcasted_Record theRecord) {
		
		log("Server receiving a record...");
		
		Record transferredRecord = null;
		if(theRecord.RecordID.contains("MR")){
			transferredRecord = new MissingRecord(theRecord);
		}else if(theRecord.RecordID.contains("CR")){
			transferredRecord = new CriminalRecord(theRecord);
		}else{
			log("transfer function has failed, please verify the logs");
			return false;
		}
		
		insertRecordToHashtable(transferredRecord);
		
		log("Server has received a record" +
				"\n\t\tRecord:             " + theRecord.RecordID +
				"\n\t\tName:               " + theRecord.firstName + " " + theRecord.lastName +
				"\n\t\tStatus:             " + theRecord.status +
				"\n\t\tDescription:        " + theRecord.description +
				"\n\t\tAddress:            " + theRecord.address+
				"\n\t\tLast Date Seen:     " + theRecord.lastDate +
				"\n\t\tLast Location Seen: " + theRecord.lastLocation);
		
		return true;
	}

}
