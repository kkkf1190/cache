package webservice.wh;

import javax.xml.ws.Endpoint;

public class MyService {
	public static void main(String[] args) {
		String address = "http://localhost:6666/ns";
		Endpoint.publish(address, new MyServiceImpl());
	}
}
