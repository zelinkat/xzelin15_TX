/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated 
 * by the @authors tag. All rights reserved. 
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors. 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 * (C) 2005-2006,
 * @author JBoss Inc.
 */
package cz.vutbr.fit.xzelin15.dp.consumer;

import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.actions.ActionUtils;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Body;
import org.jboss.soa.esb.message.Message;

import com.arjuna.mw.wst11.UserBusinessActivity;
import com.arjuna.mw.wst11.UserBusinessActivityFactory;
import com.arjuna.mw.wst11.UserTransaction;
import com.arjuna.mw.wst11.UserTransactionFactory;

import java.util.HashMap;
import java.util.Map;

public class MyResponseAction extends AbstractActionLifecycle
{
   protected ConfigTree _config;

   public MyResponseAction(ConfigTree config)
   {
      _config = config;
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
      String txType = (String)message.getBody().get(ClientConstants.TX_TYPE_ARG);
      String txCommand = (String)message.getBody().get(ClientConstants.TX_COMMAND_ARG);
      ResultWriter writer = new ResultWriter();
      
      /*
       * pick up the tx type and commit or rollback the transaciton
       * if the bad command is put into the message the exception is thrown
       */
      if("AtomicTransaction".equals(txType))
      {
    	  UserTransaction ut = UserTransactionFactory.userTransaction();
    	  
    	  if ("commit".equals(txCommand))
    	  {
    		  ut.commit();
    		  System.out.println(" [CONSUMER] Atomic transaction commited: " + ut.toString());
    		  writer.writeResult("Transaction successful");
    		  
    	  }
    	  else if ("rollback".equals(txCommand))
    	  {
    		  ut.rollback();
    		  System.out.println(" [CONSUMER] Atomic transaction rolled back: " + ut.toString());
    		  writer.writeResult("Transaction rolled back");
    	  }
    	  else
    	  {
    		  throw new Exception("Unknown atomic transaction command");
    	  }
    	  
    	  
      }
      /*
       * pick up the tx type and complete/cancel/close the transaciton
       * if bad command is put into the message the exception is thrown
       */
      if("BusinessActivity".equals(txType))
      {
    	  UserBusinessActivity ba = UserBusinessActivityFactory.userBusinessActivity();
    	  
    	  if ("complete".equals(txCommand))
    	  {
    		  ba.complete();
    		  System.out.println(" [CONSUMER] Business activity completed: " + ba.toString());
    	  }
    	  else if ("cancel".equals(txCommand))
    	  {
    		  ba.cancel();
    		  System.out.println(" [CONSUMER] Business activity cancled: " + ba.toString());
    	  }
    	  else if ("close".equals(txCommand))
    	  {
    		  ba.cancel();
    		  System.out.println(" [CONSUMER] Business activity closed: " + ba.toString());
    	  }
    	  else
    	  {
    		  throw new Exception("Unknown business activity command");
    	  }
      }
      
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

}