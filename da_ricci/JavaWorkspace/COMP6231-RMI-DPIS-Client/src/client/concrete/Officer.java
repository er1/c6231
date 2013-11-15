package client.concrete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.naming.directory.InvalidAttributesException;

import org.omg.CORBA.ORB;

import common.IORLogger;
import common.Logger;
import common.StationType;
import common.corba.recordInterfaceManager.IRecordManager;
import common.corba.recordInterfaceManager.IRecordManagerHelper;
import common.corba.recordInterfaceManager.ReportStatus;
import common.resources.Resources;
import client.external.NameReader;
import client.interfaces.IOfficer;

/**
 * The officer class that represents a single officer
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 *
 */
public class Officer implements IOfficer, Runnable {

	/**
	 * A system counter to help automate the unique identifiers
	 */
	private static int officerCount = 1000; 
	
	/**
	 * The officers badge identifier
	 */
	private int officerId = -1;
		
	/**
	 * The list of first names for demoing purposes
	 */
	private static List<String> firstNames = NameReader.GetNames("firstnames.txt");
	
	/**
	 * The list of last names for demoing purposes
	 */
	private static List<String> lastNames = NameReader.GetNames("lastnames.txt");
	
	/**
	 * The log file reference for this officer
	 */
	private Logger log = null; 
	
	/**
	 * The IOR of the file
	 */
	private String ior = null;
	
	/**
	 * The IRecord manager
	 */
	private IRecordManager manager = null;
	
	/**
	 * The station name
	 */
	private StationType.StationServerName stationName = null;
	
	/**
	 * Instantiates a new object of this class
	 * 
	 * @param station The station name identifier (such as SPVM, etc)
	 */
	public Officer(ORB orb, StationType.StationServerName stationName) throws IOException, InvalidAttributesException
	{
		this.stationName = stationName;
		ior = IORLogger.getReference(stationName);
		if(ior == null || ior.isEmpty()) {
			throw new InvalidAttributesException("There was an error with the IOR for this client");
		}
		synchronized(this) {
			officerId = officerCount++;
		}
		log = new Logger(System.getProperty("user.dir") + "/", stationName.name() + officerId);
		
		org.omg.CORBA.Object obj = orb.string_to_object(ior);
		
		IRecordManager myManager = IRecordManagerHelper.narrow(obj);
		this.manager = myManager;
	}
	
	/**
	 * Gets the officers full identification
	 * 
	 * @return The officers full identification
	 */
	private String getOfficerFullId() {
		return (stationName.name() + officerId);
	}
	
	/**
	 * Implements the Runnable interface to mimic concurrent calls for demoing purposes
	 */
	public void run() {
		
		this.CreateCRecord();
		this.CreateCRecord();
		this.CreateMRecord();
		this.GetRecordCount();
		this.EditRecord(true); //true means automate
		this.CreateCRecord();
		this.CreateMRecord();
		this.EditRecord(true); //true means automate
		this.CreateMRecord();
		this.EditRecord(true); //true means automate
		this.GetRecordCount();
		this.TransferRecord(true);
	}
		
	/**
	 * Makes a call to the client stub to create a new criminal record
	 */
	public void CreateCRecord() {
		Random rand = new Random();
		String firstName = firstNames.get(rand.nextInt(firstNames.size())); 
		String lastName = lastNames.get(rand.nextInt(lastNames.size())); 
		String description = "The criminal is considered extremely dangerous";
		ReportStatus status = ReportStatus.ON_THE_RUN;
			
		try {
			log.write(Resources.Client_CriminalRecordCreation);
			
			String identification = manager.createCRecord(getOfficerFullId(), firstName, lastName, description, status);
			if(identification != null) {
				log.write(Resources.Client_SuccessCriminalRecordCreation + "\n" +
						"ID: " + identification + "\n" +
						"First Name: " + firstName + "\n" +
						"Last Name: " + lastName + "\n" + 
						"Description: " + description + "\n" + 
						"Status: " + status.toString());
			} else {
				log.write(Resources.Client_ErrorCriminalRecordCreation);
			}
		} catch (Exception e) {
			log.write(Resources.Client_ErrorCriminalRecordCreation);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Creates a new medical record
	 */
	public void CreateMRecord() {
		
		Random rand = new Random();
		String firstName = firstNames.get(rand.nextInt(firstNames.size())); 
		String lastName = lastNames.get(rand.nextInt(lastNames.size())); 
		String address = "1000 Eat Street";
		Date lastDate = new Date();
		String lastLocation = "1234 Wellington Drive";
		ReportStatus status = ReportStatus.MISSING;
			
		try {
			log.write(Resources.Client_MissingRecordCreation);
			String identification = manager.createMRecord(getOfficerFullId(), firstName, lastName, address, lastDate.getTime(), lastLocation, status);
			if(identification != null) {
				log.write(Resources.Client_SuccessMissingRecordCreation + "\n" + 
						"ID: " + identification + "\n" +
						"First Name: " + firstName + "\n" +
						"Last Name: " + lastName + "\n" + 
						"Address: " + address + "\n" +
						"Last Date: " + lastDate + "\n" +
						"Last Location: " + lastLocation + "\n" +
						"Status: " + status.toString());
			} else {
				log.write(Resources.Client_ErrorCriminalRecordCreation);
			}
		} catch (Exception exception) {
			log.write(Resources.Client_ErrorCriminalRecordCreation);
			exception.printStackTrace();
		}
	}
		
	/**
	 * Edits the status of a defined record
	 */

	public void EditRecord(boolean automate) {
		
		Random rand = new Random();
		String lastName = null;
		String recordID = "";
		int choice = 0;
		
		if(automate) {
			lastName = lastNames.get(rand.nextInt(lastNames.size()));
			String[] nums = {
				"10000",
				"10001",
				"10002",
				"10003",
				"10004",
				"10005",
				"10006",
				"10007",
				"10008",
				"10009",
				"10010",
				"10011",
				"10012",
				"10013",
				"10014",
				"10015",
				"10016",
				"10017",
				"10018",
				"10019",
				"10020",
			};
			recordID = "CR" + nums[new Random().nextInt(20)];
			choice = (new Random().nextInt(2) + 1); // because nextint doesnt include the last number
		} else {
			System.out.print("Last Name: ");
			OfficerClient.scanner.nextLine();
			lastName = OfficerClient.scanner.nextLine();
				
			System.out.print("Record ID: ");		
			recordID = OfficerClient.scanner.nextLine();
			
			System.out.println("--Status--");
			System.out.println("1. On The Run");
			System.out.println("2. Captured");
			System.out.println("3. Missing");
			System.out.println("4. Found");
			
			
			try {
				choice = OfficerClient.scanner.nextInt(); 
			} catch (Exception exception) {
				// not necessary
			}
			 
		}
		ReportStatus status = null;
		switch(choice) {
			case 1: {
				status = ReportStatus.ON_THE_RUN;
				break;
			}
			case 2: {
				status = ReportStatus.CAPTURED;
				break;
			}
			case 3: {
				status = ReportStatus.MISSING;
				break;
			}
			case 4: {
				status = ReportStatus.FOUND;
				break;
			}
		}
		try {
			if(manager.editRecord(getOfficerFullId(), lastName, recordID, status)) {
				log.write("Client successfully edited the record");
				log.write("Record ID: " + recordID);
				log.write("Status: " + status.toString());
			} else {
				log.write("Could not edit the record");
				log.write("Record ID: " + recordID);
			}
		} catch (Exception e) {
			log.write("Client unsuccessfully edited the record");
			e.printStackTrace();
		}
	}

	/**
	 * Gets the record count from the server
	 */
	public void GetRecordCount() {
		try {
			log.write("Requesting the number of records from the server");
			String countMessage = manager.getRecordCount(getOfficerFullId());
			if(countMessage != null && !countMessage.isEmpty()) {
				log.write(countMessage);
			} else {
				log.write("There was an error trying to get the record count of the server");
			}
		}
		catch(Exception exception) {
			log.write("There was an exception on the client when requesting for the number of records");
			exception.printStackTrace();
		}
	}

	/**
	 * Transfers a record from one station to another
	 */
	public void TransferRecord(boolean automate) {
		
		if(automate) {
			String[] nums = {
				"10000",
				"10001",
				"10002",
			};
			String recordId = "CR" + nums[new Random().nextInt(3)];
			String newIdentificationNumber = manager.transferRecord(getOfficerFullId(), recordId, StationType.StationServerName.SPL.name());
			
			if(newIdentificationNumber != null && !newIdentificationNumber.isEmpty()) {
				log.write("Successfully transfered the record to the new station, your new identification number is " + newIdentificationNumber);
			} else {
				log.write("Could not transfer the record to the desired station server");
			}
		} else {
		
			System.out.print("Record ID: ");
			OfficerClient.scanner.nextLine();
			String recordId = OfficerClient.scanner.nextLine();
			System.out.println("Record id is " + recordId);
			
			// We don't let the user select a server thats their own, try to make this actually behave like a real application should...
			StationType.StationServerName[] stations = StationType.StationServerName.values();
			ArrayList<StationType.StationServerName> dynamicStationsList = new ArrayList<StationType.StationServerName>(Arrays.asList(stations));
			dynamicStationsList.remove(stationName);
			
			for(int i = 0; i < dynamicStationsList.size(); ++i) {
				System.out.println((i+1) + ": " + dynamicStationsList.get(i).name());	
			}
			
			int choice = Integer.MIN_VALUE;
			try {
				choice = OfficerClient.scanner.nextInt();	
			} catch(Exception exception) { 
				//not necessary to catch it here!
			}
			
			
			if((choice-1) >= 0 && (choice-1) < dynamicStationsList.size()) {
				String newIdentificationNumber = manager.transferRecord(getOfficerFullId(), recordId, dynamicStationsList.get(choice-1).name());
				if(newIdentificationNumber != null && !newIdentificationNumber.isEmpty()) {
					log.write("Successfully transfered the record to the new station, your new identification number is " + newIdentificationNumber);
				} else {
					log.write("Could not transfer the record to the desired station server");
				}
			} else {
				System.out.println("Bad selection, operation cancelled");
				OfficerClient.scanner.nextLine();
			}
		}
	}
}