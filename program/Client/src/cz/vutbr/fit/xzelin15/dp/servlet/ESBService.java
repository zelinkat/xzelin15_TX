package cz.vutbr.fit.xzelin15.dp.servlet;



import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;





public class ESBService {
	
	private Message _outMessage;
	private Message _inMessage;
	
	private  ServiceInvoker deliveryAdapter;
	
	public ESBService(String category, String name) throws Exception
	{
		System.setProperty("javax.xml.registry.ConnectionFactoryClass",
	            "org.apache.ws.scout.registry.ConnectionFactoryImpl");
		
		 deliveryAdapter = new ServiceInvoker(category,
	            name);
		
		 _outMessage = MessageFactory.getInstance().getMessage(
		            MessageType.JBOSS_XML);
		 
		 _inMessage = null;
		 
	}
	
	public void addMessageContent(String content)
	{
		_outMessage.getBody().add(content);
		
		
	}
	
	public void addMessageContent(String key, Object content)
	{
		_outMessage.getBody().add(key,content);
		
		
	}
	public void setMessage(Message message)
	{
		_outMessage = message;
	}
	
	
	public Message sendMessage() throws Exception
	{
		try
		{
			 deliveryAdapter.deliverAsync(getOutMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		return _inMessage;
	}
	
	
	
	
	
	private Message getOutMessage()
	{
		return _outMessage;
	}
	   
	  
	
}
