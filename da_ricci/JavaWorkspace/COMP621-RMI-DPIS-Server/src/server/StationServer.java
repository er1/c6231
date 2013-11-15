package server;

import java.util.ArrayList;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import server.com.AbstractStationServerChannel;
import server.com.IStationServerChannel.operations;
import server.com.udp.StationServerUDPInvoker;
import server.com.udp.StationServerUDPInvoker.Status;
import server.com.udp.StationServerUDPListener;
import server.report.concrete.CriminalRecord;
import server.report.concrete.MissingRecord;
import server.report.concrete.RecordFactory;
import server.report.interfaces.IRecord;
import common.IORLogger;
import common.StationType;
import common.Logger;
import common.StationType.StationServerName;
import common.StationType.StationServerUDP;
import common.corba.recordInterfaceManager.IRecordManagerPOA;
import common.corba.recordInterfaceManager.ReportStatus;
import common.resources.Resources;

/**
 * Implements the shared interface between the client and the server
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 *
 */
public class StationServer extends IRecordManagerPOA  {
	
	/**
	 * The broker used on this process shared amongst all objects
	 */
	private static ORB orb = null;
	
	/**
	 * The portable object adapter on this process shared amongst all objects
	 */
	private static POA rootPOA = null;
	
	/**
	 * The database object for records
	 */
	private StationDatabase database = null;
	
	/**
	 * The listener for this server;
	 */
	private StationServerUDPListener listener = null;
	
	/**
	 * The logging object
	 */
	protected Logger log = null;
	
	/**
	 * The station name, used as an identifier for the server operations
	 */ 
	protected StationType.StationServerName stationName = null;
	
	/**
	 * The UDP POrt
	 */
	protected StationServerUDP udpPort = null;
	
	/**
	 * Instantiates an object of this class
	 * 
	 * @param stationName The name of the station
	 * 
	 * @throws Exception If something went wrong during instantiation
	 */
	public StationServer(StationType.StationServerName stationName) throws Exception {
	
		// Set the stations name
		this.stationName = stationName;
		
		// Sets the database for this station
		this.database = new StationDatabase();

		// Creates a Logging object for this station
		log = new Logger(System.getProperty("user.dir") + "/", stationName.name());
			
		if(stationName.equals(StationType.StationServerName.SPVM)) {
			udpPort = StationServerUDP.SPVM;
		} else if(stationName.equals(StationType.StationServerName.SPL)) {
			udpPort = StationServerUDP.SPL;
		} else if(stationName.equals(StationType.StationServerName.SPB)) {
			udpPort = StationServerUDP.SPB;
		} else {
			log.write("Error: Could not properly configure a listener");
			throw new Exception("Error: Could not properly configure a listener");
		}
		
		// Initialize the listener (UDP) for this server
		listener = new StationServerUDPListener(database, udpPort);
		Thread thread = new Thread(listener);
		thread.start();
		log.write("Server has registered " + udpPort + " and is listening on incoming requests");
		
		// Creates the IOR for the client to reference
		org.omg.CORBA.Object ref = rootPOA.id_to_reference(rootPOA.activate_object(this));
		
		// Create the ior String
		String ior = orb.object_to_string(ref);
		System.out.println(ior);
		// Create the IOR logger
		IORLogger.write(stationName.name(), ior);
		
		log.write(Resources.Server_ServerInitialized);
	}

	/**
	 * Creates a criminal record
	 */
	public String createCRecord(String badgeId, String firstName, String lastName, String description, ReportStatus status) {		
		log.write(Resources.Server_ServerAttemptingCriminalRecord + ": [" + badgeId + "]");
		IRecord rec = RecordFactory.getCriminalRecord(firstName, lastName, description, status);
		try {
			if(createRecord(rec)) {
				return rec.getId();
			}
			return null;
		}
		catch(Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Creates a missing record
	 */
	public String createMRecord(String badgeId, String firstName, String lastName, String lastKnownAddress, long dateLastSeen, String placeLastSeen, common.corba.recordInterfaceManager.ReportStatus status) {
		log.write(Resources.Server_ServerAttemptingMedicalRecord + ": [" + badgeId + "]");
		IRecord rec = RecordFactory.getMissingRecord(firstName, lastName, lastKnownAddress, dateLastSeen, placeLastSeen, status);
		try {
			if(createRecord(rec)) {
				return rec.getId();
			} 
			return null;
		}
		catch(Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Abstracts the functionality of creating all records
	 * 
	 * @param record The record to add
	 * 
	 * @return Success or failure
	 * 
	 * @throws InvalidAttributeValueException
	 */
	private Boolean createRecord(IRecord record) throws InvalidAttributeValueException  {
		if(record == null) {
			return false;
		}
		Boolean completed = database.newRecord(record);
		String msg = null;
		if(record.getClass() == MissingRecord.class) {
			msg = completed ? 
					Resources.Server_SuccessMissingRecordCreation : 
					Resources.Server_ErrorMissingRecordCreation;
		} else if(record.getClass() == CriminalRecord.class) {
			msg = completed ? 
					Resources.Server_SuccessCriminalRecordCreation : 
					Resources.Server_ErrorCriminalRecordCreation;
		}
		log.write(msg + "\n" + record.getFullName() + "\nID: " + record.getId());
		return completed;		
	}
	
	/**
	 * Edit the status of a record
	 */
	public boolean editRecord(String badgeId, String lastName, String recordID, common.corba.recordInterfaceManager.ReportStatus status) {
		log.write("Record is trying to be edited: [" + badgeId + "]");
		 
		if(badgeId == null || lastName == null || lastName.isEmpty() || recordID == null || recordID.length() < 3 || status == null) {
			log.write("Error: Server could not edit record because of invalid value(s) \n Record ID: " + recordID);
			log.write("\n" + lastName);
			log.write("\n" + String.valueOf(recordID));
			log.write("\n" + status.toString());
			return false;
		}
		
		try {
			if(database.editRecord(Character.toUpperCase(lastName.charAt(0)), recordID, status))
			{
				log.write("Successfully edited the records status\n Record ID: " + recordID + "\nStatus: " + status.toString());
				return true;
			} else {
				log.write("Error: Could not update the requested record\n Record ID: " + recordID);
				return false;
			}
		} catch (InvalidAttributeValueException e) {
			log.write("Error: Server encountered an exception while trying to edit record " + recordID);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Gets the record count of this server
	 */
	public String getRecordCount(String badgeId) {
		log.write("Record count request by " + badgeId);
		
		// These hold the inner wrappings of the thread invokers below
		List<StationServerUDPInvoker> udpInvokers = new ArrayList<StationServerUDPInvoker>();
		
		// We go through all the station server objects, and we wrap the thread object around 
		// it and then we invoke a call to that stations UDP port to get its set of records.
		List<Thread> threadInvokers = new ArrayList<Thread>();
						
		// This could be a bit better done, instead of turning the values into an array we could 
		// use a memory reference for the values.  I think though that we should normalize the 
		// way we add new servers by referencing them through a common enum area
		for(int i = 0; i < StationType.StationServerName.values().length; ++i) {
			StationType.StationServerName stationName = StationType.StationServerName.values()[i];				
			// Create a UDP Invoker and add it to our list so we can join on them later below
			StationServerUDPInvoker udpInvoker = new StationServerUDPInvoker(StationType.StationServerUDP.valueOf(stationName.name()), null, AbstractStationServerChannel.operations.GET_RECORD_COUNT); 
			udpInvokers.add(udpInvoker);
			
			// Create a thread and add them in
			Thread thread = new Thread(udpInvoker);
			thread.start();
			threadInvokers.add(thread);
		}
		
		// Ensures that all the thread object calls that have been performed above 
		// halt all together so that we will only be using the main thread once we reach here.
		for(int i = 0; i < threadInvokers.size(); ++i) {
			try {
				threadInvokers.get(i).join();
			}
			catch(Exception exception) {
				exception.printStackTrace();
			}
		}
		
		String message = "";
		for(int i = 0; i < udpInvokers.size(); ++i) {
			message += udpInvokers.get(i).getCountAsMessage();
			if(i+1 >= udpInvokers.size()) {
				message += ".";
			} else {
				message += ", ";
			}
		}
		
		return message;
	}

	/**
	 * Transfers a record to a specified station
	 */
	public String transferRecord(String badgeId, String recordId, String stationName) {

		// We turn the string into a stationServerName, it will be of correct value provided that it isn't null and
		// if the station name transfer is not ourselves.
		StationServerName serverName = StationServerName.valueOf(stationName);
		IRecord record = null;
		if(isValidBadgeId(badgeId) && isValidRecordId(recordId) && serverName != null && !serverName.equals(stationName)) {
			log.write("Officer " + badgeId + " has requested to transfer record " + recordId + " to station " + stationName);

			// Get the record from the database; this will remove the record from the database
			record = database.getRecord(recordId);	
		}
		
		if(record != null) {
			StationServerUDPInvoker invoker = new StationServerUDPInvoker(StationServerUDP.valueOf(serverName.name()), record,  operations.TRANSFER_RECORD);
			Thread thread = new Thread(invoker);
			try {
				thread.start();
				thread.join();
				
				String newReference = invoker.getRecord();
				if(invoker.getStatus() == Status.COMPLETED && newReference != null) {
					return newReference;
				} 
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}		
		log.write("There was an error; officer " + badgeId + " couldn't complete the transfer of record " + recordId + " to " + stationName);
		return "";
	}
	

	/**
	 * Helper function to get the recordId
	 * 
	 * @param recordId The record id
	 * 
	 * @return if its valid
	 */
	public static boolean isValidRecordId(String recordId) {
		return
			(
				recordId != null &&  //cannot be null
				!recordId.isEmpty() &&  // cannot be empty
				recordId.length() > 6 // must contain AT LEAST 5 digits AND at least 1 letter acronym
			);
	}
	
	public static boolean isValidBadgeId(String badgeId) {
		return
			(
				badgeId != null &&  //cannot be null
				!badgeId.isEmpty() &&  // cannot be empty
				badgeId.length() > 5 // must contain AT LEAST 4 digits AND at least 1 letter acronym
			);		
	}
	
	
	/**
	 * Main method
	 * 
	 * @param args The arguments
	 */
	public static void main(String[] args) {
		try {
			// Define the ORB endpoint and the portable object wrapper
			orb = ORB.init(args, null);
			rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			
			new StationServer(StationType.StationServerName.SPVM);
			new StationServer(StationType.StationServerName.SPL);
			new StationServer(StationType.StationServerName.SPB);
		
			rootPOA.the_POAManager().activate();
			orb.run();
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
	}
}
