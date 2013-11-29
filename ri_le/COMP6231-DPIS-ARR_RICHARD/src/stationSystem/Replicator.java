package stationSystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 
 * @author chanman
 */
public class Replicator {

	public static void main(String args[]) {
		Replicator r = new Replicator();
		r.RestartServiceFromPort(1557);
		System.out.println("Done");
		
	}
	
    /**
     *
     * @param servicePort
     * @return
     */
    protected int getReplicationPort(int servicePort) {
        int replicationPort = (servicePort / 100) + 1537;
        return replicationPort;
    }

    /**
     *
     * @param servicePort
     * @return
     */
    public boolean RestartServiceFromPort(int servicePort) {
        int replicationPort = getReplicationPort(servicePort);
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress addr = InetAddress.getByName("localhost");

            DatagramPacket packet = new DatagramPacket(new byte[]{10}, 1, addr, replicationPort);
            socket.send(packet);

            byte[] buffer = new byte[1500];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
