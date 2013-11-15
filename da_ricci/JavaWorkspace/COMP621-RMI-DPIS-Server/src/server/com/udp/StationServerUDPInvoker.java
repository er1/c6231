package server.com.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import server.com.AbstractStationServerChannel;
import server.report.interfaces.IRecord;
import common.StationType;

/**
 * Class that invokes a message to other StationServer objects
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 *
 */
public class StationServerUDPInvoker extends AbstractStationServerChannel implements Runnable {
	
	/**
	 * The socket for this class
	 */
	private DatagramSocket aSocket = null;
		
	/**
	 * The port to invoke on
	 */
	private StationType.StationServerUDP stationPort = null;
	
	/**
	 * The number of 
	 */
	private Object payload = null;
	
	/**
	 * The status
	 */
	private Status status = null;
	
	/**
	 * 
	 * @author Daniel Ricci <thedanny09@gmail.com>
	 * 
	 * @version Build 2
	 *
	 */
	public enum Status { COMPLETED, FAILED }
	/**
	 * Instantiates an object of this class
	 * 
	 * @param serverReference Station Server reference
 	 * @param stationPort Port to interact upon
	 */
	public StationServerUDPInvoker(StationType.StationServerUDP stationPort, Object message, AbstractStationServerChannel.operations operation) {
		super(operation, message);
		this.stationPort = stationPort;	
		try {
			aSocket = new DatagramSocket();
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Runnable method
	 */
	public void run() {
		
		byte[] messageBufferSender = null;
		try {
			messageBufferSender = serialize(super.channelPacket);	
		} catch(Exception exception) {
			exception.printStackTrace();
			return;
		}
		
		InetAddress aHost;
		try {	
			byte[] messageBufferReceiver = new byte[1024 * 1024 * 10]; 	
			aHost = InetAddress.getByName("localhost");
			
			// Set up the datagram packet and send it off
			DatagramPacket request = new DatagramPacket(messageBufferSender, messageBufferSender.length, aHost, stationPort.port);
			aSocket.send(request);
			
			// Receive it back
			DatagramPacket reply = new DatagramPacket(messageBufferReceiver, messageBufferReceiver.length);
			aSocket.receive(reply);
			
			StationServerChannelPacket packet = (StationServerChannelPacket)deserialize(messageBufferReceiver);
			payload = packet.getPayload();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	//TODO - Abstract the below operation functions
	/*
	 * public T GetResult(T t) where T : class {
	 * 	return payLoad(T); or if t.belongs..... like a switch statement
	 * }
	 */
	/**
	 * Gets the number of records as a string message
	 * 
	 * @return the message
	 */
	public String getCountAsMessage() {
		String message = null;
		try {
			message = stationPort.name() + ": " + Integer.parseInt(new String(payload.toString()).trim());
			status = Status.COMPLETED;
			return message;
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		status = Status.FAILED;
		return null;
	}

	/**
	 * Gets the Record
	 * 
	 * @return The record to be inserted
	 */
	public String getRecord() {
		try {
			String record = new String(payload.toString()).trim();
			status = Status.COMPLETED;
			return record;
		} catch(Exception exception) {
			exception.printStackTrace();
			status = Status.FAILED;
			return null;
		}
	}
	
	/**
	 * Gets the status
	 * 
	 * @return The status
	 */
	public Status getStatus() {
		return status;
	}

}
