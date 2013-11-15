package common;

import java.util.Random;

/**
 * Class that represents the types that can be used throughout the application
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 *
 */
public class StationType {
	
	/**
	 * Station Server names
	 * 
	 * @author Daniel Ricci <thedanny09@gmail.com>
	 * @version Build 1
	 * 
	 */
	public enum StationServerName { SPVM, SPL, SPB };
	
	/**
	 * Gets a random station server name for demoing purposes usually
	 * @return
	 */
	public static StationServerName StationServerNameRandom() {
		Random rand = new Random();	
		StationServerName[] values = StationServerName.values();
		return values[rand.nextInt(values.length)];
	}
	
	/**
	 * Stores the enum objects for UDP listeners and invokers
	 * 
	 * @author Daniel Ricci <thedanny09@gmail.com>
	 * @version Build 1
	 *
	 */
	public enum StationServerUDP {
		SPVM(1400), SPL(1401), SPB(1402);
		public final int port; 
		StationServerUDP(int port) {
			this.port = port;
		}
	}
}
