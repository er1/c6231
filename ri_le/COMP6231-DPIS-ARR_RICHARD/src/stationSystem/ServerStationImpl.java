package stationSystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import stationServerIDL.StationInterface;
import stationServerIDL.StationInterfaceHelper;
import stationServerIDL.StationInterfacePOA;
import stationServerIDL.Upcasted_Record;
import dataComponents.CriminalRecord;
import dataComponents.MissingRecord;
import dataComponents.Record;
import dataComponents.Record.StatusType;

public class ServerStationImpl extends StationInterfacePOA implements Runnable{
	
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
	private UDPServer udpServer = null;//internal
	private int udpPortNumber;
	
	private int recordsCount = 0;
	
	/**
	 * transfer synchronization to prevent context switching when iterating through the hash table
	 */
	private Object transferRecordSynchronizedObject = new Object();
	
	/**
	 *  defines the UDP port
	 *  this is used like the #define of C / C++
	 */
	public static final int SPVM_PORT_NUMBER_internal = 15140;
	public static final int SPL_PORT_NUMBER_internal = 14150;
	public static final int SPB_PORT_NUMBER_internal = 15790;
	
	public static final int SPVM_PORT_NUMBER_external = 1557;
	public static final int SPL_PORT_NUMBER_external = 1558;
	public static final int SPB_PORT_NUMBER_external = 1559;
	
	private static ArrayList<Thread> THREADS = new ArrayList<Thread>();
	
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
		
		// start the internal run thread
		switch(udpPortNumber){
		case SPVM_PORT_NUMBER_external:
			udpServer = new UDPServer(stationName, SPVM_PORT_NUMBER_internal);
			break;
		case SPL_PORT_NUMBER_external:
			udpServer = new UDPServer(stationName, SPL_PORT_NUMBER_internal);
			break;
		case SPB_PORT_NUMBER_external:
			udpServer = new UDPServer(stationName, SPB_PORT_NUMBER_internal);
			break;
		default:
			System.out.println("Error: the provided UDP port number is not meant for this system's Replica");
			break;
		}
		
		if(udpServer != null){
			udpServer.start();
			THREADS.add(udpServer);
		}
		
		
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
		recordsCount++;
		udpServer.incRecordsCount();
	}
	
	private StatusType convertStatusString2StatusType(String s){
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
	
	private long convertDateString2Long(String d){
		return Long.parseLong(d);//default is milliseconds
	}

	/**
	 * Creates a criminal record
	 *  @param	badgeId			The officer's BadgeID who is calling this function
	 *  @param	firstName		First name of the criminal
	 *  @param	lastName		Last name of the criminal
	 *  @param	description		The description of his/her crime
	 *  @param	status			The status of this criminal
	 *  						it HAS to be either StatusType.ON_THE_RUN or Status.CAPTURED
	 *  @return	String			recordID if the criminal record has been successfully added to the hash table
	 *  						empty string if there are errors trying to add the record with the given parameters
	 */
	public String createCRecord(String badgeId, String firstName,
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
			//return false;
			return "";
		}
		try{
			// check that the StatusType is correct and that there is a first and last name
			if(convertedStatus == StatusType.MISSING || convertedStatus == StatusType.FOUND ||
					firstName.equals("") || lastName.equals("")){
				log(badgeId + " has FAILED to add a criminal record due to parameters: " +
						"\n\t\tName:  \t" + firstName + " " + lastName +
						"\n\t\tDescr.:\t" + description +
						"\n\t\tStatus:\t" + status);
				//return false;
				return "";
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
			//return true;
			return r.getRecordID();
		}catch(Exception e){
			//System.out.println("createCRecord: " + stationName + " could not create the criminal record.");
			e.printStackTrace(new PrintWriter(serverLogFile));
			log(badgeId + " has FAILED to add a criminal record due to exception: " +
					"\n\t\tName:  \t" + firstName + " " + lastName +
					"\n\t\tDescr.:\t" + description +
					"\n\t\tStatus:\t" + status);
			//return false;
			return "";
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
	 *  @return	String			recordID if the missing record has been successfully added to the hash table
	 *  						empty string if there are errors trying to add the record with the given parameters
	 */
	public String createMRecord(String badgeId, String firstName,
			String lastName, String address, long lastDate,
			String lastLocation, String status) {
		//convert the string status back to an enum
		StatusType convertedStatus = convertStatusString2StatusType(status);
		//convert the string lastDate back to a Date
		//if it's null return error
		if(convertedStatus == null){
			log(badgeId + " has FAILED to add a missing record due to parameters: " +
					"\n\t\tName:  \t" + firstName + " " + lastName +
					"\n\t\tAddr:  \t" + address +
					"\n\t\tSeen:  \t" + lastDate +
					"\n\t\t       \t" + lastLocation +
					"\n\t\tStatus:\t" + status);
			//return false;
			return "";
		}

		try{
			//check that the StatusType is correct and that there is a first and last name
			if(convertedStatus == StatusType.CAPTURED || convertedStatus == StatusType.ON_THE_RUN ||
					firstName.equals("") || lastName.equals("")){
				log(badgeId + " has FAILED to add a missing record due to parameters: " +
						"\n\t\tName:  \t" + firstName + " " + lastName +
						"\n\t\tAddr:  \t" + address +
						"\n\t\tSeen:  \t" + lastDate +
						"\n\t\t       \t" + lastLocation +
						"\n\t\tStatus:\t" + convertedStatus);
				//return false;
				return "";
			}
			
			// if the above passed, we can create a missing record
			Record r = new MissingRecord(firstName, lastName, address, lastDate, lastLocation, convertedStatus);
			
			// insert the record into the hash table
			insertRecordToHashtable(r);
			
			/*System.out.println("Missing record added: ");
			System.out.println("\t" + r.getRecordID() + "\t" + r.getStatus().toString() + 
					"\t" + r.getFirstName() + " " + r.getLastName());*/
			log(badgeId + " has added a missing record: " +
					"\n\t\tRecord:\t" + r.getRecordID() + " " + r.getStatus() +
					"\n\t\tName:  \t" + firstName + " " + lastName +
					"\n\t\tAddr:  \t" + address +
					"\n\t\tSeen:  \t" + lastDate +
					"\n\t\t       \t" + lastLocation);
			//return true;
			return r.getRecordID();
		}catch(Exception e){
			//System.out.println("createMRecord: " + stationName + " could not create a missing record.");
			e.printStackTrace(new PrintWriter(serverLogFile));
			log(badgeId + " has FAILED to add a missing record due to an exception: " +
					"\n\t\tName:  \t" + firstName + " " + lastName +
					"\n\t\tAddr:  \t" + address +
					"\n\t\tSeen:  \t" + lastDate +
					"\n\t\t       \t" + lastLocation +
					"\n\t\tStatus:\t" + status);
			//return false;
			return "";
		}
	}
	
	/**
	 *  Gets the record count of all the stations 
	 *  @param	badgeId		The officer's BadgeID who is calling this function
	 *  @return	String		returns a string of all the station names and their record count
	 *  					ie: SPVM 2, SPL 4, SPB 3
	 */
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
			serverPorts.add(SPVM_PORT_NUMBER_internal);
			serverPorts.add(SPL_PORT_NUMBER_internal);
			serverPorts.add(SPB_PORT_NUMBER_internal);
			
			for(int i = 0; i < serverPorts.size(); i++)
			{
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
	public String editRecord(String badgeId, String lastName,
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
			//return false;
			return "";
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
			//return false;
			return "";
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
			//return false;
			return "";
		}
		
		// set the new status for the found record
		synchronized(records.get(foundIndex)){
			records.get(foundIndex).setStatus(convertedStatus);
		}
		log(badgeId + " has SUCCESSFULLY modified a record: " +
				"\n\t\tRecord:\t" + records.get(foundIndex).getRecordID() + " "
					+ records.get(foundIndex).getLastName() +
				"\n\t\tStatus:\t" + convertedStatus);
		//return true;
		return records.get(foundIndex).getRecordID();
	}

	/**
	 * Transfer record recordID by officer badgeID to station remoteStationServerName
	 * Note that this function will make the current server act as a client
	 * @param badgeID					The officer's BadgeID who is calling this function
	 * @param recordID					The to be transferred record's RecordID
	 * @param remoteStationServerName	The station name which the record is to be transferred  
	 * @return boolean					returns true only if the record was correctly transferred
	 */
	public String transferRecord(String badgeID, String recordID,
			String remoteStationServerName) {
		// cannot transfer to itself
		if(remoteStationServerName.equals(stationName)){
			log(badgeID + " has FAILED to transfer a record from " + stationName + " to " + remoteStationServerName +
				"\n\t\tRecord: " + recordID);
			//return false;
			return "";
		}
		
		// =============== begin CORBA client ===============
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
					return "";
			}
		}catch(Exception e){
			e.printStackTrace();
			log(badgeID + " has FAILED to transfer a record from " + stationName + " to " + remoteStationServerName +
					"\n\t\tRecord: " + recordID);
			return "";
		}
		
		String ior;
		try {
			ior = br.readLine();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			log(badgeID + " has FAILED to transfer a record from " + stationName + " to " + remoteStationServerName +
					"\n\t\tRecord: " + recordID);
			return "";
		}
		
		// get the Interoperable Object Reference in CORBA object
		org.omg.CORBA.Object o = orb.string_to_object(ior);
		
		// convert the CORBA object as a Java Object
		StationInterface newStation = StationInterfaceHelper.narrow(o);
		
		// =============== end CORBA client ===============
		
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
		
		// if no record was found, log failure and return empty string
		if(foundRecord == false || toBeTransferredRecord == null){
			log(badgeID + " has FAILED to transfer a record due to record ID not found " +
					"\n\t\tRecord: " + recordID);
			//return false;
			return "";
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
					((MissingRecord)toBeTransferredRecord).getLastDate(),
					((MissingRecord)toBeTransferredRecord).getLastLocation());
		}else if(toBeTransferredRecord.getRecordID().contains("CR")){
			upRecord = new Upcasted_Record(
					toBeTransferredRecord.getRecordID(),
					toBeTransferredRecord.getLastName(),
					toBeTransferredRecord.getFirstName(),
					toBeTransferredRecord.getStatus().toString(),
					((CriminalRecord)toBeTransferredRecord).getDescription(),
					"", 0, "");
		}
				
		// calling the ORB server station so it locally transfers it 
		String transactionResult = newStation.transfer(upRecord);
		
		// decrement the record count assuming it has been transferred
		udpServer.decRecordsCount();
		recordsCount--;
		
		// if the transfer failed, put the record back
		if(transactionResult == ""){
			log("transferRecord function has failed, please verify the logs");
			// reinsert record
			insertRecordToHashtable(toBeTransferredRecord);
			return "";
		}
		
		// if the transfer failed, put the record back
		/*if(transactionResult == false){
			log("transferRecord function has failed, please verify the logs");
			// reinsert record
			insertRecordToHashtable(toBeTransferredRecord);
			return false;
		}*/
		
		log(badgeID + " has SUCCESSFULLY transferred a record" +
				"\n\t\tRecord:      " + recordID +
				"\n\t\tName:        " + toBeTransferredRecord.getFirstName() + " " + toBeTransferredRecord.getLastName() +
				"\n\t\tDestination: " + remoteStationServerName +
				"\n\t\tStatus:      " + toBeTransferredRecord.getStatus());
		//return true;
		return transactionResult;
		//consider return the new ID or no?
	}
	
	/**
	 *  This is called remotely by CORBA. The passed Upcasted_Record will be converted to the correct record type
	 *  @param	theRecord	The up-casted record with all information
	 *  @return String		returns the recordID of the new record	 
	 */
	public String transfer(Upcasted_Record theRecord) {
		
		log("Server receiving a record...");
		
		Record transferredRecord = null;
		if(theRecord.RecordID.contains("MR")){
			transferredRecord = new MissingRecord(theRecord);
		}else if(theRecord.RecordID.contains("CR")){
			transferredRecord = new CriminalRecord(theRecord);
		}else{
			log("transfer function has failed, please verify the logs");
			return "";
		}
		
		insertRecordToHashtable(transferredRecord);
		
		log("Server has received a record" +
				"\n\t\tRecord:             " + theRecord.RecordID + " >>> " + transferredRecord.getRecordID() +
				"\n\t\tName:               " + theRecord.firstName + " " + theRecord.lastName +
				"\n\t\tStatus:             " + theRecord.status +
				"\n\t\tDescription:        " + theRecord.description +
				"\n\t\tAddress:            " + theRecord.address+
				"\n\t\tLast Date Seen:     " + theRecord.lastDate +
				"\n\t\tLast Location Seen: " + theRecord.lastLocation);
		
		return transferredRecord.getRecordID();
	}
	
	// this run function acts as the thread which handles the server UDP transactions 
	public void run(){
		DatagramSocket aSocket = null;
		try{
			//aSocket = new DatagramSocket(this.udpPortNumber);
			aSocket = new DatagramSocket(null);
			aSocket.setReuseAddress(true);
			aSocket.bind(new InetSocketAddress(this.udpPortNumber));
			
					
			byte[] buffer = new byte[1000];
			
			String returnOutput, requestString = null;
			while(true){
				// reset the output for the next request
				returnOutput = "";
				
				// get the request from the other servers 
				DatagramPacket requestPacket = new DatagramPacket(buffer,  buffer.length);
				aSocket.receive(requestPacket);
				
				// the request data back into a string
				//requestString = new String(requestPacket.getData(), 0, requestPacket.getLength());
				
				 ByteArrayInputStream in = new ByteArrayInputStream(buffer);
				 ObjectInputStream is = new ObjectInputStream(in);
				 try {
					requestString = (String)is.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String[] tokens = requestString.split(":");
				
				//parse the requestString
				if(tokens[0].equalsIgnoreCase("createCRecord")){
					//createCRecord:SPVM1234:John:Doe:Description Here:OnTheRun
					returnOutput = createCRecord(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5]);
					
				}else if(tokens[0].equalsIgnoreCase("createMRecord")){
					//createMRecord:SPL4321:Ellie:Johnson:123 Fake St.:1022780000:Montreal:Missing
					returnOutput = createMRecord(tokens[1], tokens[2], tokens[3], tokens[4], convertDateString2Long(tokens[5]), tokens[6], tokens[7]);
					
				}else if(tokens[0].equalsIgnoreCase("editCRecord")){
					//editCRecord:SPL4545:John:CR10001:OnTheRun
					returnOutput = editRecord(tokens[1], tokens[2], tokens[3], tokens[4]);
					
				}else if(tokens[0].equalsIgnoreCase("getRecordCount")){
					//getRecordCount:SPMV1111
					returnOutput = getRecordCounts(tokens[1]);
					
				}else if(tokens[0].equalsIgnoreCase("transferRecord")){
					//transferRecord:SPVM7851:CR10000:SPL
					returnOutput = transferRecord(tokens[1], tokens[2], tokens[3]);
					
				}else{//assume an error happened
					returnOutput = "";
				}
				
				System.out.println(returnOutput);
				
				
				// turn the resultOutput into an array of bytes for the reply
				byte [] m = serialize(returnOutput);
				DatagramPacket reply = new DatagramPacket(m, m.length, requestPacket.getAddress(), requestPacket.getPort());
				aSocket.send(reply);
			}
		}catch(Exception e){
			System.out.println("Socket: " + e.getMessage());
		} finally{
			if(aSocket != null)
				aSocket.close();
		}
	}

	public static byte[] serialize(Object obj) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(obj);
	    return out.toByteArray();
	}
	
	public static boolean addThread(Thread t){
		return THREADS.add(t);
	}
	
	public static void killServer(){
		for(int i = 0; i < THREADS.size(); i++){
			try{
				System.out.println("killing " + THREADS.get(i).getName());
				THREADS.get(i).stop();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		THREADS.clear();
	}
	
	public static void main(String[] args) throws InvalidName, ServantAlreadyActive, WrongPolicy, ObjectNotActive, FileNotFoundException, AdapterInactive {

		final ORB orb = ORB.init(args, null);
		POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		
		ServerStationImpl spvmServerStation = new ServerStationImpl("SPVM", SPVM_PORT_NUMBER_external);
		Thread t1 = new Thread(spvmServerStation);
		t1.start();
		ServerStationImpl.addThread(t1);
		byte[] id1 = rootPOA.activate_object(spvmServerStation);
		org.omg.CORBA.Object ref1 = rootPOA.id_to_reference(id1);
		String ior1 = orb.object_to_string(ref1);
		System.out.println(ior1);
		PrintWriter file1 = new PrintWriter("spvm_ior.txt");
		file1.println(ior1);
		file1.close();
		
		ServerStationImpl splServerStation = new ServerStationImpl("SPL", SPL_PORT_NUMBER_external);
		Thread t2 = new Thread(splServerStation);
		t2.start();
		ServerStationImpl.addThread(t2);
		byte[] id2 = rootPOA.activate_object(splServerStation);
		org.omg.CORBA.Object ref2 = rootPOA.id_to_reference(id2);
		String ior2 = orb.object_to_string(ref2);
		System.out.println(ior2);
		PrintWriter file2 = new PrintWriter("spl_ior.txt");
		file2.println(ior2);
		file2.close();
		
		ServerStationImpl spbServerStation = new ServerStationImpl("SPB", SPB_PORT_NUMBER_external);
		Thread t3 = new Thread(spbServerStation);
		t3.start();
		ServerStationImpl.addThread(t3);
		byte[] id3 = rootPOA.activate_object(spbServerStation);
		org.omg.CORBA.Object ref3 = rootPOA.id_to_reference(id3);
		String ior3 = orb.object_to_string(ref3);
		System.out.println(ior3);
		PrintWriter file3 = new PrintWriter("spb_ior.txt");
		file3.println(ior3);
		file3.close();
		
		rootPOA.the_POAManager().activate();
		
		//need thread to run this in order to add it to the list of thread to be killed if reboot
		Thread orbThread = new Thread(new Runnable() {
			@Override
			public void run() {
				orb.run();
			}
		});
		ServerStationImpl.addThread(orbThread);
		
	}
}
