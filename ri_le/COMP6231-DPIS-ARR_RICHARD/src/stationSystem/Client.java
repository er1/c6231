package stationSystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client {

	public static void main(String[] args) {
		//testing
		DatagramSocket aSocket = null;
		String myRequest;
		myRequest= "getRecordCount:SPB7777";
		myRequest = "createCRecord:SPVM1234:John:Doe:Description Here:OnTheRun";
		String result = null;
		int portNumber = 1559;
		
		try{
			aSocket = new DatagramSocket();
			
			InetAddress aHost = InetAddress.getByName("localhost");
			
			//byte []m = myRequest.getBytes();
			byte []m = serialize(myRequest);
			
			// pass the returnOutput string so that the other server may append to it
			DatagramPacket request = 
					new DatagramPacket(m, m.length, aHost, portNumber);
			aSocket.send(request);
			
			// get the reply back from the server with the appended information
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			
			//convert the replied data back into the returnOutput string
			//result = new String(reply.getData(), 0, reply.getLength());
			
			ByteArrayInputStream in = new ByteArrayInputStream(buffer);
			 ObjectInputStream is = new ObjectInputStream(in);
			 try {
				result = (String)is.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}catch(SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}catch(IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally{
			if(aSocket != null)
				aSocket.close();
		}
		
		System.out.println("result: " + result);
		
		// end testing

	}
	
	public static byte[] serialize(Object obj) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(obj);
	    return out.toByteArray();
	}

}
