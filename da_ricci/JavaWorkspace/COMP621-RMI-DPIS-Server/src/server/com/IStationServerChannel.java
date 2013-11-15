package server.com;

public interface IStationServerChannel extends Runnable {
	/**
	 * The list of operations that can be performed on this channel
	 * 
	 * @author Daniel Ricci <thedanny09@gmail.com>
	 * @version Build 2
	 *
	 */
	public static enum operations { GET_RECORD_COUNT, TRANSFER_RECORD };
}


