
public class Request{
		
	private int order;
	private String stringRequest;
	private static int count = 0;
	private static Object countSync = new Object();
	
	public Request(String r){
		synchronized(countSync){
			setOrder(count);
			count++;
		}
		setRequest(r);
	}

	public String getStringRequest() {
		return stringRequest;
	}

	private void setRequest(String request) {
		this.stringRequest = request;
	}

	public int getOrder() {
		return order;
	}

	private void setOrder(int order) {
		this.order = order;
	}
}