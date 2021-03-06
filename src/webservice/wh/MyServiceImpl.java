package webservice.wh;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "TestService", serviceName = "TestService", targetNamespace = "http://www.mycompany.com")
public class MyServiceImpl implements IMyService {

	@WebMethod()
	@WebResult(name = "greeting")
	public String test(@WebParam(name = "in") String name) {
		return "hello " + name;
	}

}
