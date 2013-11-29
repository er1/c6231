package stationSystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer extends Thread{
	
	private int udpPortNumber;
	private String stationName;
	private int recordsCount = 0;
	private Object synchronizedRecordsCountObject = new Object();
	
	
	public void incRecordsCount() {
		synchronized(synchronizedRecordsCountObject){
			recordsCount++;
		}
	}

	public void decRecordsCount() {
		synchronized (synchronizedRecordsCountObject) {
			recordsCount--;
		}
	}

	UDPServer(String stationName, int udpPortNumber){
		this.udpPortNumber = udpPortNumber;
		this.stationName = stationName;
	}
	
	public String getCurrentServerRecordsCount(){
		String result = stationName + ":" + recordsCount + ";";
		return result;
	}
	
	// this run function acts as the thread which handles the server UDP transactions 
	public void run(){
		DatagramSocket aSocket = null;
		try{
			aSocket = new DatagramSocket(this.udpPortNumber);
					
			byte[] buffer = new byte[1000];
			
			String returnOutput;
			while(true){
				// reset the output for the next request
				returnOutput = "";
				
				// get the request from the other servers 
				DatagramPacket request = new DatagramPacket(buffer,  buffer.length);
				aSocket.receive(request);
				
				// the request data back into a string
				returnOutput = new String(request.getData(), 0, request.getLength());
				
				// append the server name and records count to the  returnOutput string
				returnOutput += getCurrentServerRecordsCount();
				
				// turn the resultOutput into an array of bytes for the reply
				byte [] m = returnOutput.getBytes();
				DatagramPacket reply = new DatagramPacket(m, returnOutput.length(),
							request.getAddress(), request.getPort());
				aSocket.send(reply);
			}
		}catch(SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}catch(IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally{
			if(aSocket != null)
				aSocket.close();
		}
	}

}
