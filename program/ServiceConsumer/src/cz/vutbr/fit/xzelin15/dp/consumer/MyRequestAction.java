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


import java.io.BufferedWriter;
import java.io.FileWriter;

import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

import com.arjuna.mw.wst11.UserBusinessActivity;
import com.arjuna.mw.wst11.UserBusinessActivityFactory;
import com.arjuna.mw.wst11.UserTransaction;
import com.arjuna.mw.wst11.UserTransactionFactory;


public class MyRequestAction extends AbstractActionLifecycle
{
   protected ConfigTree _config;

   public MyRequestAction(ConfigTree config)
   {
      _config = config;
   }

   public Message noOperation(Message message)
   {
      return message;
   }

   /*
    * Convert the message into a webservice request map.
    */
   public Message process(Message message) throws Exception
   {
	   
      logHeader();
      String txType = (String) message.getBody().get(ClientConstants.TX_TYPE_ARG);
      
      
      
      
      if("AtomicTransaction".equals(txType))
      {
    	  UserTransaction ut = UserTransactionFactory.userTransaction();
    	  ut.begin();
    	  System.out.println(" [CONSUMER] Atomic transaction begun: " + ut.toString());
      }
      
      else if("BusinessActivity".equals(txType))
      {
    	  UserBusinessActivity ba = UserBusinessActivityFactory.userBusinessActivity();
    	  ba.begin();
    	  System.out.println(" [CONSUMER] Business activity begun: " + ba.toString());
      }
      else
      {
    	  throw new Exception("Unknown transaction type");
      }
            
      //System.out.println(" [CONSUMER] Request map is: " + requestMap.toString());

      logFooter();
      return message;
   }

   public void exceptionHandler(Message message, Throwable exception)
   {
      logHeader();
      System.out.println("!ERROR!");
      System.out.println(exception.getClass());
      System.out.println(exception.getCause());
      System.out.println(exception.getMessage());
     exception.printStackTrace();
      System.out.println("For Message: ");
      System.out.println(message.getBody().get());
      logFooter();
      ResultWriter writer = new ResultWriter();
       writer.writeResult("Transaction failed"); 
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