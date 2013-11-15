package common.resources;

/**
 * This class holds the translation strings.  It currently only supports English
 * however it can be adapted based on a selected language to extend more
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 */
public class Resources {
	
	/* * * Server General * * */
	public final static String Server_ServerInitialized = "Server has been fully initialized";
	public final static String Server_ServerAttemptingMedicalRecord = "Officer has requested to create a medical record";
	public final static String Server_ServerAttemptingCriminalRecord = "Officer has requested to create a criminal record";
	
	/* * * Server Missing Record * * */
	public final static String Server_SuccessMissingRecordCreation = "Successfully created a new missing record";
	public final static String Server_ErrorMissingRecordCreation = "Error: Could not create a new missing record";
	
	/* * * Server Criminal Record * * */
	public final static String Server_SuccessCriminalRecordCreation = "Successfully create a new criminal record";
	public final static String Server_ErrorCriminalRecordCreation = "Error: Could not create a new criminal record";
	
	/* * * Client Criminal Record * * */
	public final static String Client_CriminalRecordCreation = "Attempting to create a criminal record";
	public final static String Client_ErrorCriminalRecordCreation = "Error: Could not create a criminal record";
	public final static String Client_SuccessCriminalRecordCreation = "Successfully created a new criminal record";
	
	/* * * Client Missing Record * * */
	public final static String Client_MissingRecordCreation = "Attempting to create a missing record";
	public final static String Client_ErrorMissingRecordCreation = "Error: Could not create a missing record";
	public final static String Client_SuccessMissingRecordCreation = "Successfully created a new missing record";
}
