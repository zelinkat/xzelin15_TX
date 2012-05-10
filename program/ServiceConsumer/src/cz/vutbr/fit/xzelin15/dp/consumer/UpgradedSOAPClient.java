

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cz.vutbr.fit.xzelin15.dp.consumer;


import it.javalinux.wise.core.client.InvocationResult;
import it.javalinux.wise.core.client.WSDynamicClient;
import it.javalinux.wise.core.client.WSEndpoint;
import it.javalinux.wise.core.client.WSMethod;
import it.javalinux.wise.core.client.handler.LoggingHandler;
import it.javalinux.wise.core.exceptions.WiseException;
import it.javalinux.wise.core.mapper.WiseMapper;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.ws.handler.Handler;

import org.apache.log4j.Logger;
import org.jboss.internal.soa.esb.publish.Publish;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Body;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.MessagePayloadProxy;

import cz.vutbr.fit.xzelin15.dp.consumer.ClientConstants;

/**
 * SOAP Client action processor. <p/> Uses the Wise Client Service to generate JAXWS client class and call the target service.
 * This action then routes that message to that service.
 * <h2>Endpoint Operation Specification</h2>
 * Specifying the endpoint operation is a straightforward task. Simply specify the "wsdl" and "SOAPAction" properties on the
 * SOAPClient action as follows:
 * 
 * <pre>
 * 	&lt;action name=&quot;soap-wise-client-action&quot; class=&quot;org.jboss.soa.esb.actions.soap.wise.SOAPClient&quot;&gt;
 * 	    &lt;property name=&quot;wsdl&quot; value=&quot;http://localhost:8080/acme/services/OrderManagement?wsdl&quot;/&gt;
 * 	    &lt;property name=&quot;SOAPAction&quot; value=&quot;http://www.acme.com/OrderManagement/SendSalesOrderNotification&quot;/&gt;
 * 	&lt;/action&gt;
 * </pre>
 * 
 * The SOAP operation is derived from the SOAPAction. There are some optional attribute:
 * 
 * <pre>
 * 	&lt;property name=&quot;EndPointName&quot; value=&quot;PingWSPort&quot;/&gt;
 * </pre>
 * 
 * The EndPoint invoked. Webservices can have multiple endpoint. If it's not specified the first specified in wsdl will be used
 * 
 * <pre>
 * 	&lt;property name=&quot;SmooksRequestMapper&quot; value=&quot;smooks-request-config.xml&quot;/&gt;
 * </pre>
 * 
 * It's a smooks config file to define the mapping java-to-java defined for the request
 * 
 * <pre>
 * 	&lt;property name=&quot;SmooksResponseMapper&quot; value=&quot;smooks-response-config.xml&quot;/&gt;
 * </pre>
 * 
 * It's a smooks config file to define the mapping java-to-java defined for the response
 * 
 * <pre>
 * 	&lt;property name=&quot;serviceName&quot; value=&quot;PingWS&quot;/&gt;
 * </pre>
 * 
 * It's a symbolic service name used by wise to cache object generation and/or use already generated object. It it isn't provided
 * wise use the servlet name of wsdl.
 * 
 * <pre>
 * 	&lt;property name=&quot;userName&quot; value=&quot;&quot;/&gt;
 * 	&lt;property name=&quot;password&quot; value=&quot;&quot;/&gt;	
 * </pre>
 * 
 * User and password used if webservice is protected by BAsic Authentication HTTP user and password
 * <h2 id="request-construction">SOAP Request Message Construction</h2>
 * The SOAP operation parameters are supplied in one of 2 ways:
 * <ol>
 * <li>As a {@link Map} instance set on the <i>default body location</i> (Message.getBody().add(Map))</li>
 * <li>As a {@link Map} instance set on in a <i>named body location</i> (Message.getBody().add(String, Map)), where the name of
 * that body location is specified as the value of the "paramsLocation" action property. </li>
 * </ol>
 * The parameter {@link Map} itself can also be populated in one of 2 ways:
 * <ol>
 * <li><b>Option 1</b>: With a set of Objects of any type. In this case a smooks config have to be specified in action attribute
 * SmooksRequestMapper and smooks is used to make the java-to-java conversion
 * <li><b>Option 2</b>: With a set of String based key-value pairs(&lt;String, Object&gt;), where the key is the name of the
 * SOAP parameter as specified in wsdls (or in generated class) to be populated with the key's value. </li>
 * </ol>
 * <h2>SOAP Response Message Consumption</h2>
 * The SOAP response object instance can be is attached to the ESB {@link Message} instance in one of the following ways:
 * <ol>
 * <li>On the <i>default body location</i> (Message.getBody().add(Map))</li>
 * <li>On in a <i>named body location</i> (Message.getBody().add(String, Map)), where the name of that body location is
 * specified as the value of the "responseLocation" action property. </li>
 * </ol>
 * The response object instance can also be populated (from the SOAP response) in one of 3 ways:
 * <ol>
 * <li><b>Option 1</b>: With a set of Objects of any type. In this case a smooks config have to be specified in action attribute
 * SmooksResponseMapper and smooks is used to make the java-to-java conversion
 * <li><b>Option 2</b>: With a set of String based key-value pairs(&lt;String, Object&gt;), where the key is the name of the
 * SOAP answer as specified in wsdls (or in generated class) to be populated with the key's value. </li>
 * </ol>
 * <h2>JAX-WS Handler for the SOAP Request/Response Message</h2>
 * It's often necessary to be able to transform the SOAP request or response, especially in header. This may be to simply add some
 * standard SOAP handlers. Wise support JAXWS Soap Handler, both custom or a predefined one based on smooks. <p/> Transformation
 * of the SOAP request (before sending) is supported by configuring the SOAPClient action with a Smooks transformation
 * configuration property as follows:
 * 
 * <pre>
 *     &lt;property name=&quot;smooksTransform&quot; value=&quot;/transforms/order-transform.xml&quot; /&gt;
 * </pre>
 * 
 * <p>
 * The value of the "smooksTransform" property is resolved by first checking it as a filesystem based resource. Failing that, it's
 * checked as a classpath resource and failing that, as a URI based resource.
 * </p>
 * <p>
 * It's also possible to provide a set of custom standard JAXWS Soap Handler. The parameter accept a list of classes implementing
 * SoapHandler interface. Classes have to provide full qualified name and be separated by semi-columns.
 * </p>
 * 
 * <pre>
 *     &lt;property name=&quot;custom-handlers&quot; value=&quot;package.Class1;package.Class2&quot; /&gt;
 * </pre>
 * 
 * <h2>Logging the SOAP Request/Response Message</h2>
 * It's useful for debug purpose to view soap Message sent and response received. Wise achieve this goal using a JAX-WS handler
 * printing all messages exchanged on System.out You can enable as follow:
 * 
 * <pre>
 *     &lt;property name=&quot;LoggingMessages&quot; value=&quot;true&quot; /&gt;
 * </pre>
 * 
 * @author <a href="mailto:stefano.maestri@javalinux.it">stefano.maestri@javalinux.it</a>
 */
@Publish(WiseWsdlContractPublisher.class)
public class UpgradedSOAPClient extends AbstractActionPipelineProcessor {

    private final Logger logger = Logger.getLogger(UpgradedSOAPClient.class);
    private final String wsdl;
    private final String soapAction;
    private final String endPointName;
    private final String smooksRequestMapperURL;
    private final String smooksResponseMapperURL;
    private final WiseMapper smooksRequestMapper;
    private final WiseMapper smooksResponseMapper;
    private String operationName;
    private String serviceName;
    private final String username;
    private final String password;
    private final List<String> smooksHandler = new ArrayList<String>();
    private final List<String> customHandlers = new ArrayList<String>();
    private final MessagePayloadProxy payloadProxy;
    private boolean loggingEnabled = false;
    private boolean tsParticipantEnrollment = false;
    
    private WSDynamicClient client;
    
    public UpgradedSOAPClient(final ConfigTree config ) throws ConfigurationException 
    {
        wsdl = config.getRequiredAttribute("wsdl");
        soapAction = config.getAttribute("SOAPAction");
        endPointName = config.getRequiredAttribute("EndPointName");
        smooksRequestMapperURL = config.getAttribute("SmooksRequestMapper");
        smooksRequestMapper = createSmooksMapper(smooksRequestMapperURL) ;
        smooksResponseMapperURL = config.getAttribute("SmooksResponseMapper");
        smooksResponseMapper = createSmooksMapper(smooksResponseMapperURL) ;
        serviceName = config.getAttribute("serviceName");
        serviceName = serviceName != null ? serviceName : wsdl.substring(wsdl.lastIndexOf("/"), wsdl.lastIndexOf("?"));
        username = config.getAttribute("username");
        password = config.getAttribute("password");
        tsParticipantEnrollment = Boolean.parseBoolean(config.getAttribute("tsParticipantEnrollment"));
        loggingEnabled = Boolean.parseBoolean(config.getAttribute("LoggingMessages"));
        logger.debug("loggingEnabled:" + loggingEnabled);
        
        if (config.getAttribute("smooks-handler-config") != null) 
        {
            smooksHandler.add(config.getAttribute("smooks-handler-config"));
        }
        
        if (config.getAttribute("custom-handlers") != null) 
        {
            for (String className : config.getAttribute("custom-handlers").split(";")) 
            {
                customHandlers.add(className);
            }
        }
        operationName = config.getAttribute("operationName");
        
        if (operationName == null)
        {
            if (soapAction == null)
            {
                throw new ConfigurationException("Missing operationName or soapAction") ;
            }
            final int pathIndex = soapAction.lastIndexOf('/') ;
            
            if (pathIndex >= 0)
            {
                if (pathIndex == soapAction.length() -1)
                {
                    throw new ConfigurationException("Invalid soapAction, cannot end with '/'") ;
                }
                operationName = soapAction.substring(pathIndex+1) ;
            }
            else
            {
                operationName = soapAction;
            }
        }
        
        payloadProxy = new MessagePayloadProxy(config);
    }
    
    public Message process(final Message message) throws ActionProcessingException 
    {
    	/********************** Tomas Zelinka *************************************/
    	// for the services which shouldn't be enrolled in transactions
    	if(tsParticipantEnrollment == true )
    	{
    		Map delist = (Map) message.getBody().get(Body.DEFAULT_LOCATION);
    		 
    		if (delist.get(ClientConstants.DELIST) != null)
    		{
    			
    			//logger.debug("[UpgradedSOAPClient]: action exited without invoke service.") ;
    			System.out.println("[UpgradedSOAPClient]: action exited without invoke service.");
    			return message;
    		}
    	}
    	
    	/*********************** Tomas Zelinka ************************************/
        WSDynamicClient client = createClient(wsdl, serviceName, username, password);
        WSEndpoint endpoint = getEndpoint(client);
        
        Object payload = getMessagePayload(message);
        final List<SmooksHandler> handlers = addSmooksHandlers(endpoint, payload);
        try
        {
            addCustomHandlers(endpoint);
            addLoggingHandler(endpoint);
            
            WSMethod wsMethod = getWSMethodFromEndpoint(operationName, endpoint);
    
            InvocationResult result;
            try 
            {
                result = wsMethod.invoke(payload, smooksRequestMapper);
            } 
            catch (final WiseException e) 
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("Exception thrown from wsMethod invocation", e) ;
                }
                throw new ActionProcessingException("Could not call method" + operationName, e);
            }
            return mapResponseToMessage(message, result, smooksResponseMapper);
        }
        finally
        {
            cleanupHandlers(handlers) ;
        }
    }
    
    synchronized WSDynamicClient createClient(final String wsdl, final String serviceName, final String username, final String password) throws ActionProcessingException
    {
        if (client == null)
        {
            try
            {
                client = new WSDynamicClientFactory().create(wsdl, serviceName, username, password);
            } 
            catch (final WiseException e)
            {
                throw new ActionProcessingException(e.getMessage(), e);
            }
            // Force endpoints to prevent JBossWS concurrency issues
            getEndpoints(client) ;
        }
        return client;
    }

    private WSEndpoint getEndpoint(final WSDynamicClient client)
    {
        Map<String, WSEndpoint> endpointsMap = getEndpoints(client);
        
        WSEndpoint endpoint;
        if (endPointName != null) {
            endpoint = endpointsMap.get(endPointName);
        } else {
            endpoint = endpointsMap.values().iterator().next();
        }
        return endpoint;
    }

    private Object getMessagePayload(final Message message) throws ActionProcessingException
    {
        try 
        {
            return payloadProxy.getPayload(message);
        } 
        catch (final MessageDeliverException e) 
        {
            throw new ActionProcessingException("Could not locate SOAP message parameters from payload", e);
        }
    }

    private List<SmooksHandler> addSmooksHandlers(final WSEndpoint endpoint, final Object params)
        throws ActionProcessingException
    {
        final List<SmooksHandler> handlers = new ArrayList<SmooksHandler>() ;
        boolean cleanup = true ;
        try
        {
            for (String config : smooksHandler) 
            {
                logger.debug("adding smooks handler:" + config);
                final SmooksHandler handler ;
                if (params instanceof Map) 
                {
                    handler = new SmooksHandler(config, (Map)params);
                } 
                else 
                {
                    handler = new SmooksHandler(config, null);
                }
                handlers.add(handler) ;
                endpoint.addHandler(handler) ;
            }
            cleanup = false ;
        }
        catch (final RuntimeException re)
        {
            throw re ;
        }
        catch (final Exception ex)
        {
            throw new ActionProcessingException("Unexpected exception while adding smooks handlers", ex) ;
        }
        finally
        {
            if (cleanup)
            {
                cleanupHandlers(handlers) ;
            }
        }
        return handlers ;
    }

    private void cleanupHandlers(final List<SmooksHandler> handlers)
    {
        for(SmooksHandler handler: handlers)
        {
            try
            {
                handler.cleanup() ;
            }
            catch (final Exception ex)
            {
                logger.warn("Unexpected exception cleaning up smooks handler", ex) ;
            }
        }
    }
    private void addCustomHandlers(final WSEndpoint endpoint)
    {
        for (String config : customHandlers) 
        {
            logger.debug("adding custom handler:" + config);
            try 
            {
                Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(config);
                endpoint.addHandler((Handler)clazz.newInstance());
            } 
            catch (final Exception e) 
            {
                logger.debug("Failed during custom handler addition:" + e.getLocalizedMessage());
            }
        }
    }

    private void addLoggingHandler(final WSEndpoint endpoint)
    {
        if (loggingEnabled) 
        {
            logger.debug("adding logging handler");
            endpoint.addHandler(new LoggingHandler());
        }
    }

    private WSMethod getWSMethodFromEndpoint(final String name, final WSEndpoint endpoint) throws ActionProcessingException
    {
        Map<String, WSMethod> wsmethodsMap = endpoint.getWSMethods();
        WSMethod wsMethod = wsmethodsMap.get(name);
        if (wsMethod != null)
        {
            return wsMethod;
        }
        throw new ActionProcessingException("No WSMethod found for " + name);
    }
    
    Map<String, WSEndpoint> getEndpoints(final WSDynamicClient client)
    {
        return client.processEndpoints();
        
    }

    private Message mapResponseToMessage(final Message message, final InvocationResult result, final WiseMapper mapper) throws ActionProcessingException
    {
        try 
        {
            payloadProxy.setPayload(message, result.getMappedResult(mapper));
        } 
        catch (final Exception e) 
        {
            throw new ActionProcessingException("Could not set payload to SOAP message", e);
        }
        return message;
    }

    private WiseMapper createSmooksMapper(final String url)
        throws ConfigurationException
    {
        if (url != null) {
            try {
                return new SmooksMapper(url);
            } catch (final Exception ex) {
                throw new ConfigurationException("Unexpected exception while creating smooks mapper", ex) ;
            }
        }
        return null;
    }
    
    @Override
    public String toString()
    {
        return "Wise SOAPClient [wsdl=" + wsdl + ", soapAction=" + soapAction + ", endPointName=" + endPointName + ", serviceName=" + serviceName + ", smooksRequestMapperURL=" + smooksRequestMapperURL + ", smooksResponseMapperURL=" + smooksResponseMapperURL + "]";
    }
    
    String getOperationName()
    {
        return operationName;
    }
}

