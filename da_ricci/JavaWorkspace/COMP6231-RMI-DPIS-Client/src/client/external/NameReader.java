package client.external;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that gets a random first or last name
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class NameReader {
	
	/**
	 * Gets a random name
	 * 
	 * @param filename The file to use
	 * 
	 * @return Random name
	 */
	public static List<String> GetNames(String filename) {

		List<String> firstNames = new ArrayList<String>();
		BufferedReader in = null;
		try {
			in = Files.newBufferedReader(Paths.get(System.getProperty("user.dir") + "/" + filename), Charset.forName("UTF-8"));
			String text = null;
			while((text = in.readLine()) != null) {
				// Removes the Byte Order Mark that Microsoft automatically puts into its text files.
				if(text.contains("﻿")) {
					text = text.replace("﻿", "");
				}
				firstNames.add(text);
			}
		}
		catch(Exception exception) {
			System.out.println("Error with the get names functionality");
			System.out.println(exception.getMessage());
		}
		finally {
			try {
				if(in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return firstNames;
	}
}
