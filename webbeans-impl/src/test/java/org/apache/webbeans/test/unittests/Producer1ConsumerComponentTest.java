/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.apache.webbeans.test.unittests;

import java.util.List;

import javax.enterprise.inject.spi.BeanManager;

import junit.framework.Assert;

import org.apache.webbeans.component.AbstractBean;
import org.apache.webbeans.container.BeanManagerImpl;
import org.apache.webbeans.context.ContextFactory;
import org.apache.webbeans.test.component.producer.Producer1;
import org.apache.webbeans.test.component.service.IService;
import org.apache.webbeans.test.component.service.Producer1ConsumerComponent;
import org.apache.webbeans.test.component.service.ServiceImpl1;
import org.apache.webbeans.test.servlet.TestContext;
import org.junit.Before;
import org.junit.Test;

public class Producer1ConsumerComponentTest extends TestContext
{
    BeanManager container = null;

    public Producer1ConsumerComponentTest()
    {
        super(Producer1ConsumerComponentTest.class.getSimpleName());
    }

    @Before
    public void init()
    {
        super.init();
        this.container = BeanManagerImpl.getManager();
    }

    @Test
    public void testTypedComponent() throws Throwable
    {
        clear();

        defineManagedBean(ServiceImpl1.class);
        defineManagedBean(Producer1.class);
        defineManagedBean(Producer1ConsumerComponent.class);

        List<AbstractBean<?>> comps = getComponents();

        ContextFactory.initRequestContext(null);
        ContextFactory.initApplicationContext(null);

        Assert.assertEquals(7, getDeployedComponents());

        Object obj = getManager().getInstance(comps.get(0));
        
        Assert.assertNotNull(obj);

        getInstanceByName("service");

        getManager().getInstance(comps.get(1));

        Object object = getManager().getInstance(comps.get(2));

        Assert.assertTrue(object instanceof Producer1ConsumerComponent);

        Producer1ConsumerComponent single = (Producer1ConsumerComponent) object;

        IService service = single.getService();

        Assert.assertNotNull(service);

        ContextFactory.destroyApplicationContext(null);
        ContextFactory.destroyRequestContext(null);
    }

}
