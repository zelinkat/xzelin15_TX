/**
 *  WISE Invokes Services Easily - Stefano Maestri / Alessio Soldano
 *  
 *  http://www.javalinuxlabs.org - http://www.javalinux.it 
 *
 *  Wise is free software; you can redistribute it and/or modify it under the 
 *  terms of the GNU Lesser General Public License as published by the Free Software Foundation; 
 *  either version 2.1 of the License, or (at your option) any later version.
 *
 *  Wise is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */
package cz.vutbr.fit.xzelin15.dp.consumer;

import it.javalinux.wise.core.exceptions.WiseRuntimeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.jboss.soa.esb.lifecycle.LifecycleResourceException;
import org.jboss.soa.esb.smooks.resource.SmooksResource;
import org.milyn.Smooks;
import org.milyn.container.ExecutionContext;
import org.milyn.resource.URIResourceLocator;
import org.xml.sax.SAXException;

/**
 * A SOAPHandler extension. It apply smooks transformation on soap message.
 * Transformation can also use freemarker, using provided javaBeans map to get
 * values It can apply transformation only on inbound message, outbound ones or
 * both, depending on
 * 
 * @see #setInBoundHandlingEnabled(boolean)
 * @see #setOutBoundHandlingEnabled(boolean)
 * 
 * @author Stefano Maestri, stefano.maestri@javalinux.it
 * 
 */
public class SmooksHandler implements SOAPHandler<SOAPMessageContext> {

    private String smooksResource;

    private Map beansMap;

    private boolean outBoundHandlingEnabled = true;

    private boolean inBoundHandlingEnabled = true;

    private final Smooks smooks ;
    
    /**
     * 
     * @param resource
     *                URI of smooks config file
     * @param beans
     *                used for smooks BeanAccessor
     * @throws IOException 
     * @throws SAXException 
     * @throws IllegalArgumentException 
     * @throws LifecycleResourceException
     */
    public SmooksHandler(String resource, Map beans) throws IllegalArgumentException, SAXException, IOException, LifecycleResourceException {
	this.smooksResource = resource;
	this.beansMap = beans;
	smooks = SmooksResource.createSmooksResource();
    smooks.addConfigurations("smooks-resource", new URIResourceLocator().getResource(smooksResource));
    }

    public Set getHeaders() {
	// TODO Auto-generated method stub
	return null;
    }

    public void close(MessageContext arg0) {
	// TODO Auto-generated method stub

    }

    public boolean handleFault(SOAPMessageContext arg0) {
	return false;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
	SOAPMessage message = smc.getMessage();
	Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
	if (outboundProperty == true && this.isOutBoundHandlingEnabled() == false) {
	    return false;
	}
	if (outboundProperty == false && this.isInBoundHandlingEnabled() == false) {
	    return false;
	}
	try {
		smc.setMessage(applySmooksTransformation(message));
	} catch (Exception e) {
		WiseRuntimeException.rethrow(e);
	}
	return true;
    }

    SOAPMessage applySmooksTransformation(SOAPMessage message) throws Exception {
	ByteArrayOutputStream outStream = null;
	ByteArrayInputStream inStream = null;

	try {

	    ExecutionContext executionContext = smooks.createExecutionContext();
	    StringWriter transResult = new StringWriter();
	    
	    executionContext.getBeanContext().getBeanMap().putAll(this.beansMap);

	    StringWriter buffer;
	    outStream = new ByteArrayOutputStream();
	    message.writeTo(outStream);
	    outStream.flush();
	    inStream = new ByteArrayInputStream(outStream.toByteArray());
	    smooks.filterSource(executionContext, new StreamSource(inStream), new StreamResult(transResult));
	    inStream.close();
	    inStream = new ByteArrayInputStream(transResult.toString().getBytes());
	    SOAPMessage message2 = MessageFactory.newInstance().createMessage(message.getMimeHeaders(), inStream);
	    return message2;
	} finally {
	    try {
		inStream.close();
	    } catch (Exception e) {
		// nop
	    }
	    try {
		outStream.close();
	    } catch (Exception e) {
		// nop
	    }
	}
    }

    public boolean isOutBoundHandlingEnabled() {
	return outBoundHandlingEnabled;
    }

    /**
     * 
     * @param outBoundHandlingEnabled
     *                if true smooks transformation are applied to outBound
     *                message
     */
    public void setOutBoundHandlingEnabled(boolean outBoundHandlingEnabled) {
	this.outBoundHandlingEnabled = outBoundHandlingEnabled;
    }

    public boolean isInBoundHandlingEnabled() {
	return inBoundHandlingEnabled;
    }

    /**
     * 
     * @param inBoundHandlingEnabled
     *                if true smooks transformation are applied to inBound
     *                message
     */
    public void setInBoundHandlingEnabled(boolean inBoundHandlingEnabled) {
	this.inBoundHandlingEnabled = inBoundHandlingEnabled;
    }
    
    public void cleanup() {
        SmooksResource.closeSmooksResource(smooks);
    }
}
