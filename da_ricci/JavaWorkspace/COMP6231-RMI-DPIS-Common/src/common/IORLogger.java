package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Represents the way to interface with IOR (Independent Object Reference) files 
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 2
 * 
 */
public class IORLogger {
	
	/**
	 * The directory for the ior's
	 */
	private static final String iorDirectory = "/_ior";
	
	private static final String iorExtension = "_IOR.txt";
	
	private static final String iorPath = IORLogger.class.getProtectionDomain().getCodeSource().getLocation().getPath() + iorDirectory;
	
	/**
	 * Instantiates an object of this class
	 * 
	 * @param path The path to interact with
	 * @param fileName The name of the file to create or reuse
	 */
	public static void write(String fileName, String data) {	
		try {
			// Ensures that the path exists before writing on
			new File(iorPath).mkdir();
			PrintWriter prFile = new PrintWriter(iorPath + "/" + fileName + iorExtension);
			prFile.println(data);
			prFile.close();
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Reads out an IOR reference
	 *  
	 * @param serverName The name of the server

	 * @return IOR Reference 
	 * 
	 * @throws IOException
	 */
	public static String getReference(StationType.StationServerName serverName) throws IOException {
		if(serverName != null &&! serverName.name().isEmpty()) {
			BufferedReader reader = null; 
			String ior = null;
			try {
				reader = new BufferedReader(new FileReader(iorPath + "/" + serverName.name() + iorExtension));
				synchronized(reader) {
					ior = reader.readLine();	
				}
			}
			catch(IOException exception) {
				exception.printStackTrace();
			} 
			finally {
				if(reader != null) {
					reader.close();					
				}
			}
			return ior;
		}
		return null;
	}
}