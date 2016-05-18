package promote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.axis2.rpc.client.RPCServiceClient;

import org.w3c.dom.Document;   
import org.w3c.dom.Element;   
import org.w3c.dom.Node;   
import org.w3c.dom.NodeList;   
import org.xml.sax.InputSource;   

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
	
	private void invokeWebService(String uri,String method) throws UnsupportedEncodingException {		
		String namespace = getNameSpace(uri);
		String para = String.valueOf(1);
		if(namespace!=null){
			System.out.println(invokeWebService(uri,namespace,method,para));
		}
	}
	
	private String getNameSpace(String URI) throws UnsupportedEncodingException {
		InputStream is = downloadXML(URI+"?wsdl");
		String outfile = convertStreamToString(is);
		return parse(outfile);

	}
	
	private InputStream downloadXML(final String urlStr) {
		InputStream inStream = null;
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn.setConnectTimeout(5 * 1000);
		try {
			conn.setRequestMethod("GET");
			conn.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		try {
			inStream = conn.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inStream;
	}
	
	public String convertStreamToString(InputStream is)
			throws UnsupportedEncodingException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"utf-8"));

		StringBuilder sb = new StringBuilder();

		String line = null;

		try {

			while ((line = reader.readLine()) != null) {

				sb.append(line);

			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				is.close();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

		return sb.toString();

	}
	
	private String parse(String protocolXML) {
		String ns = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(
					protocolXML)));

			Element root = doc.getDocumentElement();
			if(root.hasAttribute("targetNamespace")){
				ns = root.getAttribute("targetNamespace");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ns;
	}

	public static void main(String[] args) throws AxisFault, InstantiationException, IllegalAccessException, UnsupportedEncodingException {
		MyServiceTest service = MyServiceTest.class.newInstance();
		service.invokeWebService("http://localhost:6666/ns","test");
//		service.invokeWebService("http://10.108.164.131:8080/GoodsProvider","getGoods");
	}

}
