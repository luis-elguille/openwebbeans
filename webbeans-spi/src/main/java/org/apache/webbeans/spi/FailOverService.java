/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.webbeans.spi;

import javax.enterprise.inject.spi.Bean;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Container provided failover and passivation service.
 * 
 */
public interface FailOverService 
{
    /**
     * Used for tracking the origin of serialized bean instances. 
     * 
     * @return
     */
    public String getJVMId();
    
    /**
     * The session attribute name used to store the bean instances bag
     *    
     * @return
     */
    public String getFailOverAttributeName();
    
    /**
     * Whether or not to support failover.
     * @return
     */
    public boolean isSupportFailOver();

    /**
     * Whether or not to support passivation.
     * @return
     */
    public boolean isSupportPassivation();
    
    /**
     * Enable failover support.
     * 
     * @param flag
     */
    public void enableFailOverSupport(boolean flag);

    /**
     * Enable passivation support.
     * 
     * @param flag
     */
    public void enablePassivationSupport(boolean flag);

    /**
     * Inform the service that a session is idle. Invoked when we finish
     * a request.
     * 
     * @param session
     */
    public void sessionIsIdle(HttpSession session);

    /**
     * Inform the service that a session will be active. Invoked when 
     * a request is received. 
     * 
     * @param session
     */
    public void sessionIsInUse(HttpSession session);
    
    /**
     * Invoked when we try to restore cdi bean instances. Invoked when
     * a request is finished.
     * 
     * @param session
     */
    public void restoreBeans(HttpSession session);
    
    /**
     * Container is going to actively passivate a session.
     * 
     * @param session
     */
    public void sessionWillPassivate(HttpSession session);
    
    /**
     * Container provided object input stream.
     *  
     * Note, the stream should support deserializing javassist objects.
     * 
     * @return custom object input stream.
     */
    public ObjectInputStream getObjectInputStream(InputStream in) throws IOException;
    
    /**
     * Container provided object output stream. 
     * 
     * Note, the stream should support serializing javassist objects.
     * 
     * @return custom object output stream.
     */
    public ObjectOutputStream getObjectOutputStream(OutputStream out) throws IOException;


    /**
     * Container provided custom handler for serialize / deserialize a resource bean. 
     * Add clean up code in this method will allow OWB to override default resource 
     * bean passivation behavior. 
     * 
     * Note, in the method, a container may first invoke the application provided 
     * handler(@See SerializationHandler) if it is configured. 
     * 
     * @param bean                The resource bean.
     * @param resourceObject    The resource bean instance
     * @param in                The input object stream
     * @param out                The output object stream
     * 
     * @return NOT_HANDLED if not handled by handler.
     */
    public Object handleResource(
            Bean<?> bean,
            Object resourceObject,
            ObjectInput in,
            ObjectOutput out
    );

    /**
     * Returned, if container or application does not handle the resource object
     * in the handleResource() method.
     */
    public final static Object NOT_HANDLED = new Object();

}
