package common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents the logger class 
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 * 
 */
public class Logger {
	
	/**
	 * The path to interact with
	 */
	private String path = null;
	
	/**
	 * The filename of the file
	 */
	private String fileName = null;
	
	/**
	 * The directory for the logs
	 */
	private static final String logsDirectory = "/logs";

	/**
	 * Instantiates an object of this class
	 * 
	 * @param path The path to interact with
	 * @param fileName The name of the file to create or reuse
	 */
	public Logger(String path, String fileName) {	
		this.path = path;
		this.fileName = fileName;
		try {
			// This will attempt to make the directory if it doesn't already exist
			new File(System.getProperty("user.dir") + logsDirectory).mkdir();	
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Synchronized function that will write a stream of text to a file
	 * 
	 * @param content The content to write
	 */
	public synchronized void write(String content) {
		PrintWriter pr = null;
		File file = new File(path + logsDirectory + "/" + fileName + ".txt");
		
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			pr = new PrintWriter(new FileWriter(file.getAbsoluteFile(), true));
			
			// Get the date of the operation
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			// Print a line in the text file
			pr.println(dateFormat.format(new Date()) + ": " + content);
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		finally {	
			// Close the stream
			pr.close();
		}
	}
}
