package promote;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import org.w3c.dom.Document;

public class TestClient {
	private String ns = "http://wh.webservice/";
	private String wsdlUrl = "http://localhost:6666/ns?wsdl";

	public void createSoap() {
		try {
			// ������Ϣ����
			MessageFactory factory = MessageFactory.newInstance();
			// ������Ϣ��������SoapMessage
			SOAPMessage message = factory.createMessage();
			// ����SOAPPart
			SOAPPart part = message.getSOAPPart();
			// ��ȡSOAPEnvelope
			SOAPEnvelope envelope = part.getEnvelope();
			// ͨ��SoapEnvelope���Ի�ȡ����Ӧ��Body��Header����Ϣ
			SOAPBody body = envelope.getBody();
			// ����Qname������Ӧ�Ľڵ�,Qname��һ�����������ռ�Ľڵ�
			QName qname = new QName("http://java.wh/webservice/", "add", "ns");
			// <ns:add xmlns="http://java.wh/webservice/"
			SOAPBodyElement ele = body.addBodyElement(qname);
			ele.addChildElement("a").setValue("11");
			ele.addChildElement("b").setValue("22");
			// ��ӡ��Ϣ��Ϣ
			message.writeTo(System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void soapDispatch() {
		try {
			// ��������service
			URL url = new URL(wsdlUrl);
			QName sname = new QName(ns, "MyServiceImplService");
			Service service = Service.create(url, sname);

			// ����DIspatch
			Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(
					ns, "MyServiceImplPort"), SOAPMessage.class,
					Service.Mode.MESSAGE);

			// ����SOAPMessage
			SOAPMessage msg = MessageFactory.newInstance().createMessage();
			SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
			SOAPBody body = envelope.getBody();

			// ����QName��ָ����Ϣ�д�������
			QName ename = new QName(ns, "minus", "ns");
			// <nn:add xmlns="xx"/>
			SOAPBodyElement ele = body.addBodyElement(ename);
			ele.addChildElement("arg0").setValue("55");
			ele.addChildElement("arg1").setValue("33");
			msg.writeTo(System.out);
			System.out.println("\n invoking....");

			// ͨ��Dispatch������Ϣ,�᷵����Ӧ��Ϣ
			SOAPMessage response = dispatch.invoke(msg);
			response.writeTo(System.out);
			System.out.println();

			// ����Ӧ����Ϣת��Ϊdoc����
			Document doc = response.getSOAPPart().getEnvelope().getBody()
					.extractContentAsDocument();
			String str = doc.getElementsByTagName("return").item(0)
					.getTextContent();
			System.out.println(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		URL url = new URL("http://localhost:6666/ns?wsdl");
		InputStream stream = (url.openConnection()).getInputStream();
		byte[] byt = new byte[65535];
		StringBuffer sb = new StringBuffer();
		System.out.println(stream.read());
		while(stream.available()>0){
			stream.read(byt);
			sb.append(new String(byt));
			byt = new byte[65535];
		}
		System.out.println(sb);
		TestClient client = new TestClient();
//		client.createSoap();
		client.soapDispatch();
	}
}
