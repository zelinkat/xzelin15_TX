package cz.vutbr.fit.xzelin15.dp.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;

public class ClientServlet extends HttpServlet 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5764396519237289122L;
	
	/**
	 * 
	 */
	private ServletContext context;
	
	/**
	 * 
	 */
	public void init(ServletConfig config)  throws ServletException 
	{ 
		super.init(config); 
		context = config.getServletContext();
    } 
	
	
			     
	
	/**
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		processRequest(request, response);
    }
	
	/**
	 * 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		processRequest(request, response);
    }
	
	/**
	 * 
	 */
	protected void processRequest(HttpServletRequest request,HttpServletResponse response) 
		    throws ServletException, java.io.IOException 
    {
		
		/* debug prints */
		System.out.println("Restaurant" + request.getParameter(ClientConstants.RESTAURANT_ARG));
		System.out.println("Theatre" + request.getParameter(ClientConstants.THEATRE_STALLS_ARG));
		System.out.println("Theatre" + request.getParameter(ClientConstants.THEATRE_CIRCLE_ARG));
		System.out.println("Theatre" + request.getParameter(ClientConstants.THEATRE_BALCONY_ARG));
		System.out.println("Taxi" + request.getParameter(ClientConstants.TAXI_ARG));
		System.out.println("TransactionType " + request.getParameter(ClientConstants.TX_TYPE_ARG));
		
		
        Message message = createESBMessage(request);
        
       
        String result = "";
        
        
		
      
        
       if("AtomicTransaction".equals(request.getParameter(ClientConstants.TX_TYPE_ARG)))
       {
    	   /*
    	    * add to message the tx type and the closing command (commit, rollback) 
    	    */
    	   
    	   message.getBody().add(ClientConstants.TX_TYPE_ARG,request.getParameter(ClientConstants.TX_TYPE_ARG));
    	   message.getBody().add(ClientConstants.TX_COMMAND_ARG,"commit");
    	   try
    	   {
    		   ESBService atTransaction = new ESBService("Transactions","AtomicTransaction");
    		   atTransaction.setMessage(message);
    		   atTransaction.sendMessage();
    		   result = "Atomic transaction started.";
    		  
    	   }
    	   catch (Exception e)
    	   {
    		   e.printStackTrace();
    	   }
    	   
       }
       
       else if("BusinessActivity".equals(request.getParameter(ClientConstants.TX_TYPE_ARG)))
       {
    	   /*
    	    * add to message the tx type and the closing command (complete, close, cancel) 
    	    */
    	   message.getBody().add(ClientConstants.TX_TYPE_ARG,request.getParameter(ClientConstants.TX_TYPE_ARG));
    	   message.getBody().add(ClientConstants.TX_COMMAND_ARG,"complete");
    	   try
    	   {
    		   ESBService baTransaction = new ESBService("Transactions","BusinessActivity");
    		   baTransaction.setMessage(message);
    		   baTransaction.sendMessage();
    		   result = "Business activity started.";
    	   }
    	   catch (Exception e)
    	   {
    		   e.printStackTrace();
    	   }
    	   
       }
       else
       {
           result = "Unknown transaction type " + request.getParameter(ClientConstants.TX_TYPE_ARG);
       }

      
      
		request.setAttribute("result", result);
        context.getRequestDispatcher("/index.jsp").forward(request, response);
    }
	
	
	
	
	
	/*
	 * The user data from frontend will be transformed
	 * and packed into esb message
	 */
	private Message createESBMessage(HttpServletRequest request)
	{
		/**
		 * convert the strings arguments to it's right datatypes
		 * because we will need it for the services
		 */
	 	int restaurantSeats = Integer.parseInt(request.getParameter(ClientConstants.RESTAURANT_ARG));
        int theatreCircleSeats = Integer.parseInt(request.getParameter(ClientConstants.THEATRE_CIRCLE_ARG));
        int theatreStallsSeats = Integer.parseInt(request.getParameter(ClientConstants.THEATRE_STALLS_ARG));
        int theatreBalconySeats = Integer.parseInt(request.getParameter(ClientConstants.THEATRE_BALCONY_ARG));
        int taxiCount = Integer.parseInt(request.getParameter(ClientConstants.TAXI_ARG));
        boolean bookTaxi = (taxiCount >= 1 ? true : false);
        String txType = request.getParameter("TX_TYPE_ARG");
        
        Message message = MessageFactory.getInstance().getMessage(MessageType.JBOSS_XML);      
        
        
        /*
         * each service has it's own hash map for arguments
         */
        HashMap<String,Integer> restaurantMap = new HashMap(); 
        HashMap<String,Integer> theatrecircleMap = new HashMap<String,Integer>(); 
        HashMap<String,Integer> theatrestallsMap = new HashMap<String,Integer>(); 
        HashMap<String,Integer> theatrebalconyMap = new HashMap<String,Integer>(); 
        HashMap<String,String> taxiMap = new HashMap<String,String>(); 
        HashMap<String,String> txTypeMap = new HashMap<String,String>(); 
        HashMap<String,String> delistMap = new HashMap<String,String>();
        
        
        /*
         * hash map with data for the right service will be add
         * to message body with unique key. Data bridge will pick up
         * the hashmap and send it into service in default location
         * 
         * for case the user insert zero or don't want to use the service
         * there is delist map. The delist map will be used in the soap client.
         * Delist map informs soap client to not invoke the service and return
         * message and so continue in action pipeline.
         */
        if(restaurantSeats > 0)
        {
        	restaurantMap.put("how_many", restaurantSeats);
        	message.getBody().add(ClientConstants.RESTAURANT_ARG,restaurantMap);
        	
        }
        else
        {
        	delistMap.put(ClientConstants.DELIST, ClientConstants.DELIST);
        	message.getBody().add(ClientConstants.RESTAURANT_ARG, delistMap);
        }
        
        if(theatreCircleSeats > 0)
        {
        	theatrecircleMap.put("how_many", theatreCircleSeats);
        	theatrecircleMap.put("which_area", ClientConstants.CIRCLESEATS_AREA);
        	message.getBody().add(ClientConstants.THEATRE_CIRCLE_ARG,theatrecircleMap);
        }
        else
        {   delistMap.put(ClientConstants.DELIST, ClientConstants.DELIST);
        	message.getBody().add(ClientConstants.THEATRE_CIRCLE_ARG, delistMap);
        }
        
        
        
        if(theatreStallsSeats > 0)
        {
        	theatrestallsMap.put("how_many", theatreStallsSeats);
        	theatrestallsMap.put("which_area", ClientConstants.STALLEATS_AREA);
        	message.getBody().add(ClientConstants.THEATRE_STALLS_ARG,theatrestallsMap);
        	
        }
        else
        {
        	delistMap.put(ClientConstants.DELIST, ClientConstants.DELIST);
        	message.getBody().add(ClientConstants.THEATRE_STALLS_ARG, delistMap);
        }
        
        
        if(theatreBalconySeats > 0)
        {
        	theatrebalconyMap.put("how_many", theatreBalconySeats);
        	theatrebalconyMap.put("which_area", ClientConstants.BALCONYSEATS_AREA);
        	message.getBody().add(ClientConstants.THEATRE_BALCONY_ARG,theatrebalconyMap);
        	
        }
        else
        {
        	delistMap.put(ClientConstants.DELIST, ClientConstants.DELIST);
        	message.getBody().add(ClientConstants.THEATRE_BALCONY_ARG, delistMap);
        }
        
        
        
        if(bookTaxi)
        {
        	taxiMap.put("bookTaxi", "");
        	message.getBody().add(ClientConstants.TAXI_ARG,taxiMap);
        }
        else
        {
        	delistMap.put(ClientConstants.DELIST, ClientConstants.DELIST);
        	message.getBody().add(ClientConstants.TAXI_ARG,delistMap);
        }
        
         
       
        
        	
        
        
        return message;
	}
	
	
	
	
	
}
