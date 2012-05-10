package cz.vutbr.fit.xzelin15.dp.servlet;



import javax.xml.soap.MessageFactory;
//import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;



public class PlainSOAPHeader {

	
	
	 
	 public static SOAPHeader makeElement()
	 {
		 
		 
		 MessageFactory messageFactory = null;
		 SOAPHeader soapHeader = null;
		 try
		 {
			 messageFactory = MessageFactory.newInstance();
			 SOAPMessage message = messageFactory.createMessage();
			 SOAPPart soapPart = message.getSOAPPart();
			 SOAPEnvelope soapEnvelope = soapPart.getEnvelope();

			 soapHeader = soapEnvelope.getHeader();
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		 
		 return soapHeader;
		 
		 
	 }
	
	
     
     
	
	
}
