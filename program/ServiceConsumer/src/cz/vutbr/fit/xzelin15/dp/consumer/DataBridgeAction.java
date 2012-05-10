package cz.vutbr.fit.xzelin15.dp.consumer;

import java.util.HashMap;

import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

public class DataBridgeAction extends AbstractActionLifecycle
{
   protected ConfigTree _config;

   public DataBridgeAction (ConfigTree config) throws ConfigurationException
   {
      _config = config;
      
      setLocation(_config.getRequiredAttribute("dataLocation"));
   }

   public Message noOperation(Message message)
   {
      return message;
   }

   /*
    * Retrieve and output the webservice response.
    */
   public Message process(Message message) throws Exception
   {

      logHeader();
      
      /*
       * according to data location it picks up the hash map
       * and puts it into default location to send the arguments
       * to service - OGNL        
       */
	  HashMap dataMap = (HashMap)  message.getBody().get(getLocation());
	  message.getBody().add(dataMap); 
    	  
      
      
      System.out.println("To Service: " + dataMap);
      logFooter();
      return message;
   }

   public void exceptionHandler(Message message, Throwable exception)
   {
      logHeader();
      System.out.println("!ERROR!");
      System.out.println(exception.getMessage());
      System.out.println("For Message: ");
      System.out.println(message.getBody().get());
      logFooter();
   }

   // This makes it easier to read on the console
   private void logHeader()
   {
      System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\n");
   }

   private void logFooter()
   {
      System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\n");
   }
   
   private String location;
   
   private void setLocation(String str)
   {
	   this.location = str;
   }
   
   private String getLocation()
   {
	   return this.location;
   }

}
