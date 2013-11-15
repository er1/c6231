package server;

import java.util.ArrayList; 
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.InvalidAttributeValueException;

import server.report.interfaces.IRecord;

/**
 * Represents the database of records
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 *
 */
public class StationDatabase {

	/**
	 * The database structure used to store and retrieve records
	 */
	private HashMap<Character, ArrayList<IRecord>> database = new HashMap<Character, ArrayList<IRecord>>();

	/**
	 * Adds a new record to the database
	 * 
	 * @param record The record to add
	 * 
	 * @return Success of the operation
	 */
	public Boolean newRecord(IRecord record) {
		
		// Get the character key for lookup
		char key = record.getKey();
		
		List<IRecord> recordPtr = null;
		
		// Lock the database so we can verify if the key is there
		synchronized(database) {
			// If its there then remove our lock, now we just need
			// a reference to the ArrayList that the key is hashed on
			if(database.containsKey(key)) {
				// Reference our value and then lock the ArrayList so we can add
				// a new object
				recordPtr = database.get(key);
			} else {
				database.put(key, new ArrayList<IRecord>(Arrays.asList(record)));
				return true;
			}
		}
		
		if(recordPtr != null) {
			synchronized(recordPtr) {
				recordPtr.add(record);
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Edit a records status based on the key and the record id
	 * 
	 * @param key The key to use for a lookup
	 * @param recordId The record id to look up
	 * @param status The status to put towards
	 * 
	 * @return Success of the operation
	 * 
	 * @throws InvalidAttributeValueException Based on if the record id was properly set
	 */
	public boolean editRecord(char key, String recordId, common.corba.recordInterfaceManager.ReportStatus status) throws InvalidAttributeValueException {
		
		//ITs possible here that we need to lock!
		
		if(!database.containsKey(key)) {
			return false;
		}
		
		List<IRecord> recordPtr = database.get(key);
		for(int i = 0; i < recordPtr.size(); ++i) {
			if(recordPtr.get(i).getId().equals(recordId)) {
				IRecord record = recordPtr.get(i);
				synchronized(record) {
					return record.setStatus(status);
				}
			}
		}
		return false;
	}
	
	/**
	 * Gets the record count of this database
	 * 
	 * @return The number of records in the database 
	 */
	public int getRecordCount() {
		Iterator it = database.entrySet().iterator();
		int counter = 0;
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			List<IRecord> list = (List<IRecord>) pairs.getValue();
			synchronized(list) {
				for(int i = 0; i < list.size(); ++i) {
					++counter;
				}
			}
		}
		return counter;
	}

	/**
	 * Gets the specified IRecord from the lookup table
	 * Note: This record will be removed from the lookup table
	 * 
	 * @param badgeId The badge id
	 * @param recordId The record id

	 * @return The record
	 */
	public IRecord getRecord(String recordId) {
		// Go through all of the entries in the database
		Iterator it = database.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			List<IRecord> list = (List<IRecord>)pairs.getValue();
			synchronized(list) { //lock on each list of records, this is needed because remove will resize the list
				for(int i = 0; i < list.size(); ++i) {
					try {
						if(list.get(i).getId().equals(recordId)) {
							return list.remove(i);
						}
					} catch(Exception exception) {
						exception.printStackTrace();
						return null;
					}
				}
			}
		}
		return null;
	}	
}