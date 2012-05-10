/*
 * JBoss, Home of Professional Open Source Copyright 2009, Red Hat Middleware
 * LLC, and individual contributors by the @authors tag. See the copyright.txt
 * in the distribution for a full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package cz.vutbr.fit.xzelin15.dp.consumer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.lang.StringUtils;
import org.jboss.internal.soa.esb.assertion.AssertArgument;
import sun.misc.BASE64Encoder;
import it.javalinux.wise.core.client.WSDynamicClient;
import it.javalinux.wise.core.exceptions.WiseConnectionException;
import it.javalinux.wise.core.exceptions.WiseException;
import it.javalinux.wise.core.utils.IDGenerator;
import it.javalinux.wise.core.utils.IOUtils;
import it.javalinux.wise.core.utils.WiseProperties;

/**
 * Factory for {@link WSDynamicClient}. 
 * Lifted from {@link it.javalinux.wise.core.client.WSDynamicClientFactory}.
 * <p/>
 */
public class WSDynamicClientFactory
{
    private static final String WISE_PROPERTIES_FILE = "wise-core.properties";
    
    private static final WSDynamicClientFactory SINGLETON = new WSDynamicClientFactory() ;
    
    public static WSDynamicClientFactory getFactory()
    {
        return SINGLETON ;
    }
    
    public synchronized WSDynamicClient create(final String wsdl, final String name, final String username, final String password) throws WiseException
    {
        AssertArgument.isNotNull(name, "name");
        AssertArgument.isNotNull(wsdl, "wsdl");
        
        final WiseProperties wiseProperties = new WiseProperties(WISE_PROPERTIES_FILE);
        
        String usableWsdl = wsdl;
        if (wsdl.startsWith("http://"))
        {
            usableWsdl = downloadWsdl(wsdl, username, password, wiseProperties);
        }
        
        final WSDynamicClient client = new WSDynamicClient(wiseProperties);
        client.init(usableWsdl, name, username, password);
        return client;
    }
    
    private String downloadWsdl(String wsdlURL, String userName, String password, WiseProperties wiseProperties) throws WiseConnectionException
    {
        if (StringUtils.trimToNull(userName) == null || StringUtils.trimToNull(password) == null)
        {
            return this.transferWSDL(wsdlURL, null, wiseProperties);
        } 
        else
        {
            return this.transferWSDL(wsdlURL, new StringBuffer(userName).append(":").append(password).toString(), wiseProperties);
        }
    }

    private String transferWSDL(String wsdlURL, String userPassword, WiseProperties wiseProperties) throws WiseConnectionException
    {
        String filePath = null;
        try
        {
            URL endpoint = new URL(wsdlURL);
            // Create the connection
            HttpURLConnection conn = (HttpURLConnection) endpoint.openConnection();
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            // set Connection close, otherwise we get a keep-alive
            // connection
            // that gives us fragmented answers.
            conn.setRequestProperty("Connection", "close");
            // BASIC AUTH
            if (userPassword != null)
            {
                conn.setRequestProperty("Authorization", "Basic " + (new BASE64Encoder()).encode(userPassword.getBytes()));
            }
            // Read response
            InputStream is = null;
            if (conn.getResponseCode() == 200)
            {
                is = conn.getInputStream();
            } 
            else
            {
                is = conn.getErrorStream();
                InputStreamReader isr = new InputStreamReader(is);
                StringWriter sw = new StringWriter();
                char[] buf = new char[200];
                int read = 0;
                while (read != -1)
                {
                    read = isr.read(buf);
                    sw.write(buf);
                }
                throw new WiseConnectionException("Remote server's response is an error: " + sw.toString());
            }
            // saving file
            File file = new File(wiseProperties.getProperty("wise.tmpDir"), new StringBuffer("Wise").append(IDGenerator.nextVal()).append(".xml").toString());
            OutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
            IOUtils.copyStream(fos, is);
            fos.close();
            is.close();
            filePath = file.getPath();
        } 
        catch (WiseConnectionException wce)
        {
            throw wce;
        } 
        catch (Exception e)
        {
            throw new WiseConnectionException("Wsdl download failed!", e);
        }
        return filePath;
    }
}