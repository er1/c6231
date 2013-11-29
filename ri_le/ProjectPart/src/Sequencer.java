import java.util.LinkedList;
import java.util.Queue;


public class Sequencer {
	
	// singleton instance
	private static Sequencer instance = null;
	// used only for synchronization of creating new singleton instance
	private static Object instanceSync = new Object();
	
	// queue of requests with type Request (it just holds the request number and string request)
	private Queue<Request> requestQueue;
	// used when adding requests 
	private Object enqueueSync = new Object();
	
	private Sequencer(){
		requestQueue = new LinkedList<Request>();
	}
	
	public static Sequencer getInstance(){
		synchronized(instanceSync){
			if(instance == null){
				instance = new Sequencer();
			}
		}
		
		return instance;
	}
	
	public void enqueue(String r){
		synchronized(enqueueSync){
			requestQueue.add(new Request(r));
		}
	}
	
	public String dequeue(){
		Request r = requestQueue.poll();
		return r.getStringRequest();
	}
}
