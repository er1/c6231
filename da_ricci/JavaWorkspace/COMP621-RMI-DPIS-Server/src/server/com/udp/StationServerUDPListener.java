package server.com.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import server.StationDatabase;
import server.com.AbstractStationServerChannel;
import server.report.concrete.RecordFactory;
import server.report.interfaces.IRecord;
import common.StationType;

/**
 * Class that opens a listener on the StationServer
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 *
 */
public class StationServerUDPListener extends AbstractStationServerChannel implements Runnable {

	/**
	 * The datagram socket for this station
	 */
	private DatagramSocket aSocket = null;
	
	/**
	 * A reference to the station database
	 */
	private StationDatabase stationDatabase = null;
	
	/**
	 * Instantiates an object of this class
	 * 
	 * @param serverReference A reference to the current server that this 
	 * @param stationPort The port to use
	 */
	public StationServerUDPListener(StationDatabase stationDatabase, StationType.StationServerUDP stationPort) {
		this.stationDatabase = stationDatabase;
		try {
			aSocket = new DatagramSocket(stationPort.port);
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Runnable method
	 */
	public void run() {
		while(true) {			
			try {
				byte[] buffer = new byte[1024 * 1024 * 10]; // Hopefully 10MB is enough
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				
				// Put our item to a station packet
				StationServerChannelPacket channelPacket = (StationServerChannelPacket)deserialize(buffer);
				
				if(channelPacket == null) {
					System.out.println("Communications error, invalid object format!");
					throw new Exception("Error, bad communications protocol!");
				}
				
				String data = "";
				
				// Get the operation that will be performed
				operations operation = channelPacket.getOperation();
				if(operation.equals(operations.GET_RECORD_COUNT)) {
					data = String.valueOf(stationDatabase.getRecordCount());
				} else if(operation.equals(operations.TRANSFER_RECORD)) {
					IRecord record = (IRecord)channelPacket.getPayload();
					record = RecordFactory.normalizeRecord(record);
					if(stationDatabase.newRecord(record)) {
						data = record.getId();
					} else {
						data =  "";
					}
				} else {
					System.out.println("There is a nice error here!");
				}
				
				StationServerChannelPacket listeningPacket = new StationServerChannelPacket(operations.TRANSFER_RECORD, data);
				byte[] flatData = serialize(listeningPacket);
				
				DatagramPacket reply = new DatagramPacket(flatData, flatData.length, request.getAddress(), request.getPort());
				aSocket.send(reply);
				
			} catch (Exception e) {
				System.out.println("Error: An exception happened when trying to send the message back");
				e.printStackTrace();
			}
		}
	}
}