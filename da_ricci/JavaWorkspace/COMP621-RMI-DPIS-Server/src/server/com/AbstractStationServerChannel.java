package server.com;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import server.com.udp.StationServerChannelPacket;

/**
 * Abstracts the contents of a channel that is used between two stationsd
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 2
 *
 */
public abstract class AbstractStationServerChannel implements IStationServerChannel {
	
	/**
	 * The channelPacket object, if TCP ever gets supported we will have a much better idea for abstracting the server.com package
	 */
	protected StationServerChannelPacket channelPacket = null;
	
	protected AbstractStationServerChannel() { /* This is not needed at the moment but we wont have to call super at least in children */ }
	
	/**
	 * Instantiates an object of this class
	 * 
	 * @param operation The operation to perform
	 * @param payload The payload to send over
	 * 
	 */
	public AbstractStationServerChannel(AbstractStationServerChannel.operations operation, Object payload) {
		channelPacket = new StationServerChannelPacket(operation, payload);
	}
	
	/**
	 * Serializes an object 
	 * 
	 * @param obj The object to serialize
	 * 
	 * @return a byte array representation
	 * 
	 * @throws IOException
	 */
	public static byte[] serialize(Object obj) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(obj);
	    return out.toByteArray();
	}
	
	/**
	 * Deserializes a byte array to an object
	 * 
	 * @param data The data to deserialize
	 * 
	 * @return The object
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return is.readObject();
	}
}