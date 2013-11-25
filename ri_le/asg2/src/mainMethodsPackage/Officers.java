package mainMethodsPackage;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import dataComponents.Record.StatusType;
import distributedSystem.OfficerClient;


public class Officers extends Thread{

	private static int idCount = 1000;
	private String prefix;
	private String BadgeID;
	private int taskPatternNumber;
	
	private OfficerClient computer;
		
	public Officers(String stationName, int taskPatternNumber, String ior_file){
		prefix = stationName;
		BadgeID = prefix+idCount;
		idCount++;
		this.taskPatternNumber = taskPatternNumber;
		try {
			computer = new OfficerClient(stationName, BadgeID, ior_file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public OfficerClient getOfficerClientForStaticCreation(){
		return computer;
	}
	
	public void run(){
		
		switch(taskPatternNumber){
		
		case 0:
			// does nothing, this pattern is for the static creation of records in Servers.java 
			// in fact the start() function should not even run
			break;
		
		// use case 1
		case 10:
			// officer from SPVM
			computer.transferRecord("MR10000", "SPL");
			computer.getCRecordCounts();
			break;
			
		// use case 2
		case 20:
			//officer from SPVM
			computer.transferRecord("CR10002", "SPB");
			computer.getCRecordCounts();
			break;
			
		// use case 3
			
		case 30:
			// officer from SPVM
			computer.transferRecord("MR10001", "SPL");
			computer.getCRecordCounts();
			break;
			
		case 31:
			// officer from SPVM
			computer.transferRecord("CR10000", "SPB");
			computer.getCRecordCounts();
			break;
			
		case 32:
			// officer from SPL
			computer.transferRecord("MR10002", "SPVM");
			computer.getCRecordCounts();
			break;
			
		case 33:
			// officer from SPL
			computer.transferRecord("CR10003", "SPB");
			computer.getCRecordCounts();
			break;
			
		case 34:
			// officer from SPB
			computer.transferRecord("CR10008", "SPVM");
			computer.getCRecordCounts();
			break;
			
		case 35:
			// officer from SPB
			computer.transferRecord("MR10004", "SPL");
			computer.getCRecordCounts();
			break;
			
		// use case 4
		case 40:
			// this thread will compete (edit) with case 31
			// SPVM
			computer.editCRecord("Scherbatsky", "CR10001", StatusType.CAPTURED);
			computer.getCRecordCounts();
			break;
			
		case 41:
			// this thread will compete (transfer) with case 30
			// SPVM
			computer.transferRecord("CR10001", "SPL");
			computer.getCRecordCounts();
			break;
			
		case 42:
			// this thread will compete (edit) with case 33
			// SPL
			computer.editCRecord("Ehrmantraut", "CR10005", StatusType.CAPTURED);
			computer.getCRecordCounts();
			break;
			
		case 43:
			// SPL
			// this thread will compete (transfer) with case 32
			computer.transferRecord("CR10005", "SPB");
			computer.getCRecordCounts();
			break;
			
		case 44:
			// this thread will compete (edit) with case 35
			// SPB
			computer.editCRecord("Cooper", "CR10007", StatusType.CAPTURED);
			computer.getCRecordCounts();
			break;
			
		case 45:
			// this thread will compete (transfer) with case 34
			// SPB
			computer.transferRecord("CR10007", "SPVM");
			computer.getCRecordCounts();
			break;
			
			
		// use case 5
		case 50:
			computer.editCRecord("Scherbatsky", "CR10001", StatusType.ON_THE_RUN);
			computer.transferRecord("CR10001", "SPVM");
			break;

		case 51:
			computer.editCRecord("Ehrmantraut", "CR10005", StatusType.ON_THE_RUN);
			computer.transferRecord("CR10005", "SPL");
			break;
			
		case 52:
			computer.editCRecord("Cooper", "CR10007", StatusType.ON_THE_RUN);
			computer.transferRecord("CR10007", "SPB");
			break;
			
		default:
			System.out.println("DPIS tesst error: test pattern " + taskPatternNumber + " does not exist");
		}
	}
	
	public static void showMenu()
	{
		System.out.println("Please select an option (1-4)");
		System.out.println("1. Single record transfer.");
		System.out.println("2. Simultaneous transfer of same record");
		System.out.println("3. Simultaneous transfer of records.");
		System.out.println("4. Simultaneous edit and transfer records.");
		System.out.println("5. Undo #4 to try again");
		System.out.println("6. Exit");
	}
	
	@SuppressWarnings("deprecation")//to remove the (new Date(...)) warning
	public static void main(String[] args) {
		
		
		System.out.println("\n****Welcome to the DPIS test cases****\n");
		
		System.out.println("Creating initial records...");
		//have computer to statically create records
		OfficerClient computer = null;
		
		computer = (new Officers("SPVM", 0, "spvm_ior.txt")).getOfficerClientForStaticCreation();
		computer.createMRecord("Ted", "Mosby", "123 Main Avenue", (new Date(2013, 9, 30, 9, 30)), "MacLaren's Bar", StatusType.MISSING);
		computer.createMRecord("Marshall", "Eriksen", "456 New York", (new Date(2013, 9, 30, 9, 30)), "MacLauren's Bar", StatusType.MISSING);
		computer.createCRecord("Barney", "Stinson", "Sexual Assault", StatusType.ON_THE_RUN);
		computer.createCRecord("Robin", "Scherbatsky", "Unauthorized use of licensed weapon", StatusType.ON_THE_RUN);
		computer.createCRecord("Lily", "Aldrin", "Shoplifting", StatusType.ON_THE_RUN);
		/* SPVM
		 * MR10000 Ted Mosby
		 * MR10001 Marshall Eriksen
		 * CR10000 Barney Stinson
		 * CR10001 Robin Scherbatsky
		 * CR10002 Lily Aldrin
		 */
		
		computer = (new Officers("SPL", 0, "spl_ior.txt")).getOfficerClientForStaticCreation();
		computer.createCRecord("Walter", "White", "Mass Production of Crystal Meth", StatusType.ON_THE_RUN);
		computer.createMRecord("Jesse", "Pinkman", "852 Albuquerque Avenue", (new Date(2013, 9, 30, 9, 30)), "SPL Station", StatusType.MISSING);
		computer.createMRecord("Hank", "Schrader", "554 Dallas Street", (new Date(2013, 9, 30, 9, 30)), "MacLauren's Bar", StatusType.MISSING);
		computer.createCRecord("Gustavo", "Fring", "Drug Kingpin", StatusType.ON_THE_RUN);
		computer.createCRecord("Mike", "Ehrmantraut", "Murder", StatusType.ON_THE_RUN);
		/* SPL
		 * MR10002 Jesse Pinkman
		 * MR10003 Hank Schrader
		 * CR10003 Walter White
		 * CR10004 Gustavo Fring
		 * CR10005 Mike Ehrmantraut
		 */
		
		computer = (new Officers("SPB", 0, "spb_ior.txt")).getOfficerClientForStaticCreation();
		computer.createCRecord("Leonard", "Hofstadter", "Illegal Protest", StatusType.ON_THE_RUN);
		computer.createCRecord("Sheldon", "Cooper", "Breached Restraining Order on Stan Lee", StatusType.ON_THE_RUN);
		computer.createCRecord("Penny", "Unknown", "Assault", StatusType.ON_THE_RUN);
		computer.createMRecord("Howard", "Wolowitz", "297 Riverdale Court", (new Date(2013, 9, 30, 9, 30)), "Caltech University", StatusType.MISSING);
		computer.createMRecord("Rajesh", "Koothrappali", "541 Woodland Avenue Apt. 302", (new Date(2013, 9, 30, 9, 30)), "MacLauren's Bar", StatusType.MISSING);
		/* SPB
		 * CR10006 Leonard Hofstadter
		 * CR10007 Sheldon Cooper
		 * CR10008 Penny Unknown
		 * MR10004 Howard Wolowitz
		 * MR10005 Rajesh Koothrappali
		 */
		
		System.out.println();
		
		int userChoice=0;
		Scanner keyboard = new Scanner(System.in);
		
		showMenu();
		
		while(true)
		{
			Boolean valid = false;
			
			// Enforces a valid integer input.
			while(!valid)
			{
				try{
					userChoice = keyboard.nextInt();
					valid=true;
				}
				catch(Exception e)
				{
					System.out.println("Invalid Input, please enter an Integer");
					valid=false;
					keyboard.nextLine();
				}
			}//end of while(!valid)
			
			switch(userChoice)
			{
			case 1:
				(new Officers("SPVM", 10, "spvm_ior.txt")).start();
				showMenu();

				break;
				
			case 2:
				(new Officers("SPVM", 20, "spvm_ior.txt")).start();
				(new Officers("SPVM", 20, "spvm_ior.txt")).start();
				showMenu();
				break;
				
			case 3:
				(new Officers("SPVM", 30, "spvm_ior.txt")).start();
				(new Officers("SPVM", 31, "spvm_ior.txt")).start();
				(new Officers("SPL", 32, "SPL_ior.txt")).start();
				(new Officers("SPL", 33, "SPL_ior.txt")).start();
				(new Officers("SPB", 34, "SPB_ior.txt")).start();
				(new Officers("SPB", 35, "SPB_ior.txt")).start();
				showMenu(); 
				break;
				
			case 4:
				(new Officers("SPVM", 40, "spvm_ior.txt")).start();
				(new Officers("SPVM", 41, "spvm_ior.txt")).start();
				(new Officers("SPL", 42, "SPL_ior.txt")).start();
				(new Officers("SPL", 43, "SPL_ior.txt")).start();
				(new Officers("SPB", 44, "SPB_ior.txt")).start();
				(new Officers("SPB", 45, "SPB_ior.txt")).start();
				showMenu();
				break;
				
			case 5:
				//undo transfer to try again
				(new Officers("SPL", 50, "spl_ior.txt")).start();
				(new Officers("SPB", 51, "spb_ior.txt")).start();
				(new Officers("SPVM", 52, "spvm_ior.txt")).start();
				showMenu();
				break;
				
			case 6:
				System.out.println("Goodbye!");
				keyboard.close();
				System.exit(0);
				break;
			default:
				System.out.println("Invalid Input, please try again.");
			}
		}//end of while true
	}//end of main function

}
