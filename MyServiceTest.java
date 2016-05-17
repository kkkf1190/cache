package axis2Test;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.rpc.client.RPCServiceClient;

import javax.xml.namespace.QName;

import org.apache.axis2.rpc.client.RPCServiceClient;

public class MyServiceTest {
	
	public String invokeWebService(String URI,String targetNS,String methodName,String para){
		String res="0";
		try {
			String soapBindingAddress = URI;

			EndpointReference endpointReference = new EndpointReference(
					soapBindingAddress);

			// ����һ��OMFactory�������namespace�����������������������
			OMFactory factory = OMAbstractFactory.getOMFactory();

			// ���洴�������ռ䣬������WebServiceָ����targetNamespace���ԵĻ�����Ҫ�����
			// ��Ӧ��@WebService(targetNamespace = "http://www.mycompany.com")
			OMNamespace namespace = factory.createOMNamespace(
					targetNS, "xsd");

			// ���洴�����ǲ�����������Ӧ��@WebParam(name = "name")
			// ����@WebParamû��ָ��targetNamespace���������洴��name����ʱ������null��������ø�һ��namespace����
			OMElement nameElement = nameElement = factory.createOMElement(
					"in", null);
			nameElement.setText(para);  

			// ���洴��һ��method����"test"Ϊ������
			OMElement method = factory.createOMElement(methodName, namespace);
			method.addChild(nameElement);

			Options options = new Options();
			options.setAction(targetNS); // �˴���Ӧ��@WebMethod(action
																// =
																// "http://www.mycompany.com/test")
			options.setTo(endpointReference);

			ServiceClient sender = new ServiceClient();
			sender.setOptions(options);

			// �����������Ϊ<xsd:test
			// xmlns:xsd="http://www.mycompany.com"><name>java</name></xsd:test>
//			System.out.println(method.toString());

			// ���Ͳ��õ���������ˣ����óɹ������õ��˽��
			OMElement result = sender.sendReceive(method);
			res = result.getFirstElement().getText();
		} catch (AxisFault ex) {
			ex.printStackTrace();
		}
		return res;
	}
	
	private void invokeWebService() {
		String uri = "http://10.108.164.131:8080/GoodsProvider";
		String namespace = "http://www.example.org/goodsProvider/";
		String method = "getGoods";
		String para = String.valueOf(1);
		System.out.println(invokeWebService(uri,namespace,method,para));
	}

	public static void main(String[] args) throws AxisFault, InstantiationException, IllegalAccessException {
		MyServiceTest.class.newInstance().invokeWebService();

	}

}
