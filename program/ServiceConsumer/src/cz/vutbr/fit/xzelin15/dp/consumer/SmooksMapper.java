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

import it.javalinux.wise.core.mapper.WiseMapper;

import java.io.IOException;
import java.util.Map;

import org.jboss.soa.esb.lifecycle.LifecycleResourceException;
import org.jboss.soa.esb.smooks.resource.SmooksResource;
import org.milyn.Smooks;
import org.milyn.container.ExecutionContext;
import org.milyn.container.plugin.PayloadProcessor;
import org.milyn.event.report.HtmlReportGenerator;
import org.milyn.resource.URIResourceLocator;
import org.xml.sax.SAXException;

/**
 * A WiseMapper based on smooks, copied from the wise codebase
 * 
 * @author stefano.maestri@javalinux.it
 * 
 */
public class SmooksMapper implements WiseMapper {

    private String smooksResource;

    private String smooksReport ;

    private Smooks smooks ;
    
    /**
     * Create this mapper using passed resource
     * 
     * @param smooksResource
     *                URI of smooks resource to use
     * @throws SAXException 
     * @throws IOException 
     * @throws IllegalArgumentException 
     * @throws LifecycleResourceException
     */
    public SmooksMapper(String smooksResource)
        throws IllegalArgumentException, IOException, SAXException, LifecycleResourceException {
	this(smooksResource, null);
    }

    /**
     * Create this mapper using passed resource and passed smooks html report to
     * generate. A SmooksMapper created with this constructor will create an
     * html smooks report useful for debug.
     * 
     * @param smooksResource
     *                URI of smooks resource to use
     * @param smooksReport
     *                the URI of smooks html report to generate.
     * @throws SAXException 
     * @throws IllegalArgumentException 
     * @throws IOException
     * @throws LifecycleResourceException
     */
    public SmooksMapper(String smooksResource, String smooksReport)
        throws IOException, IllegalArgumentException, SAXException, LifecycleResourceException {
	this.smooksResource = smooksResource;
	this.smooksReport = smooksReport;
    }

    /**
     * apply this mapping to original object
     * 
     * @param originalObjects
     * @return Map returned is typically used to invoke webservice operations.
     *         To do this, beanids defined in smooks config (and used here as
     *         Map's keys) have to be the parameters names as defined in
     *         wsdl/wsconsume generated classes
     * @throws Exception
     */
    public Map<String, Object> applyMapping(Object originalObjects) throws Exception {

    if(smooks == null) {
    	synchronized (this) {
    	    if(smooks == null) {
	    		smooks = SmooksResource.createSmooksResource();
	    		smooks.addConfigurations("smooks-resource", new URIResourceLocator().getResource(smooksResource));
    	    }
		}
    }
    	
	ExecutionContext executionContext = smooks.createExecutionContext();
	// Configure the execution context to generate a report...
	if (this.getSmooksReport() != null) {
	    executionContext.setEventListener(new HtmlReportGenerator(this.getSmooksReport()));
	}
	org.milyn.container.plugin.PayloadProcessor payloadProcessor = new PayloadProcessor(smooks, org.milyn.container.plugin.ResultType.JAVA);
	// smooks should return a map
	// TODO: verify with some unit tests
	return (Map<String, Object>) payloadProcessor.process(originalObjects, executionContext);
	// return result.getResultMap();

    }

    public String getSmooksResource() {
	return smooksResource;
    }

    public void setSmooksResource(String smooksResource) {
	this.smooksResource = smooksResource;
    }

    public String getSmooksReport() {
	return smooksReport;
    }

    public void setSmooksReport(String smooksReport) {
	this.smooksReport = smooksReport;
    }

}
