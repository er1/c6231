package client.concrete;

import java.rmi.RMISecurityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.omg.CORBA.ORB;

import client.interfaces.IOfficer;
import common.StationType;

/**
 * The client application; when an officer on a machine signs onto the client it
 * would normally create a single officer object, however we will be using
 * multiple threads to replicate the workload as if many officers were using the
 * client to communicate to the various servers.
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 * 
 */
public class OfficerClient {

	/**
	 * The scanner object for the client
	 */
	public static Scanner scanner = new Scanner(System.in);
	
	/**
	 * Main method of execution for the application
	 * 
	 * @param args
	 *            The arguments passed from the console if any
	 * 
	 */
	public static void main(String args[]) {
		
		// Create our communications channel
		ORB orb = ORB.init(args, null);
		
		// The default station
		StationType.StationServerName setting = StationType.StationServerName.SPVM;
		
		// Sets the security permissions for the current user
		System.setSecurityManager(new RMISecurityManager());
		try {
			System.out.println("*****************************************");
			System.out.println("*                                       *");
			System.out.println("*                                       *");
			System.out.println("Welcome to the Officer Client Application");
			System.out.println("*                                       *");
			System.out.println("*                                       *");
			System.out.println("*****************************************");

			Boolean done = false;
			
			// Note: To change the officer (use a different one), you need to set the station, even if its
			// the same station.
			IOfficer officer = null;
			while (!done) {
				System.out.println("1. Create a Criminal Record");
				System.out.println("2. Create a Missing Record");
				System.out.println("3. Edit Record");
				System.out.println("4. Record Count");
				System.out.println("5. Transfer Record");
				System.out.println("6. Benchmark Test (automated)");
				System.out.println("7. Set Station for Non-Automated Tests");
				System.out.println("8. Exit");
				System.out.print("> ");
				
				// Prevent the user from typing a non valid numerical value
				if (!scanner.hasNextInt()) {
					scanner.next();
					continue;
				}
				
				// Perform the desired operation
				switch (scanner.nextInt()) {
					case 1: { // Create a criminal record
						if(officer == null) {
							officer = new Officer(orb, setting);
						}
						officer.CreateCRecord();
						break;
					} 
					case 2: { // Create a medical record
						if(officer == null) {
							officer = new Officer(orb, setting);
						}
						officer.CreateMRecord();
						break;
					}
					case 3: { // edit the status of a record
						if(officer == null) {
							officer = new Officer(orb, setting);
						}
						officer.EditRecord(false);
						break;
					}
					case 4: { // get the record count
						if(officer == null) {
							officer = new Officer(orb, setting);
						}
						officer.GetRecordCount();
						break;
					}
					case 5: { // transfer record
						if(officer == null) {
							officer = new Officer(orb, setting);
						}
						officer.TransferRecord(false);
						break;
					}
					case 6: { // perform a benchmark test 
						List<Thread> threadInvokers = new ArrayList<Thread>();
						for(int i = 0; i < 20; ++i) {
							Officer threadOfficer = new Officer(orb, StationType.StationServerNameRandom());	
							Thread thread = new Thread(threadOfficer);
							thread.start();
							threadInvokers.add(thread);
						}
						
						for(int i = 0; i < threadInvokers.size(); ++i) {
							try {
								threadInvokers.get(i).join();
							}
							catch(Exception exception) {
								exception.printStackTrace();
							}
						}
						System.out.println("All done, you can breathe now!");
						break;
					}
					case 7: { // perform a benchmark test
						officer = null;
						System.out.println("1. " + StationType.StationServerName.SPVM.name());
						System.out.println("2. " + StationType.StationServerName.SPB.name());
						System.out.println("3. " + StationType.StationServerName.SPL.name());
						System.out.print(">");
						try {
							switch(OfficerClient.scanner.nextInt()) {
								case 1: {
									setting = StationType.StationServerName.SPVM;
									break;
								}
								case 2: {
									setting = StationType.StationServerName.SPB;
									break;
								}
								case 3: {
									setting = StationType.StationServerName.SPL;
									break;
								}
							}
						}
						catch(Exception exception) {
							OfficerClient.scanner.next();
							System.out.println("Invalid input");
						}
						System.out.println("Welcome to the " + setting.name() + " Station");
						break;
					}
					case 8: { // exit the system
						System.out.println("Exiting the system");
						done = true;
						break;
					}
					default: {
						System.out.println("Invalid option selected, please try again.");
						break;
					}
				}
			}
			scanner.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
