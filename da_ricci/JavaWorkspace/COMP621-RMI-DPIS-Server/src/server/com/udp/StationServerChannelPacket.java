package server.com.udp;

import java.io.Serializable;

import server.com.IStationServerChannel.operations;

/**
 * Intermediate object used in between UDP channels throughout the application
 *  
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 2
 *
 */
public class StationServerChannelPacket implements Serializable {		
	
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The operation to perform
	 */
	private operations operation = null;
	
	/**
	 * The payload
	 */
	private Object payload = null;
	
	/**
	 * Constructs an object of this class
	 * 
	 * @param operation The operation to perform 
	 * @param payload The payload
	 */
	public StationServerChannelPacket(operations operation, Object payload) {
		this.operation = operation;
		this.payload = payload;
	}
	
	/**
	 * Gets the operation
	 * 
	 * @return The operation
	 */
	public operations getOperation() {
		return operation;
	}
	
	/**
	 * Gets the payload
	 * 
	 * @return The payload
	 */
	public Object getPayload() {
		return payload;
	}
}
