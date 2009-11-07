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
package org.apache.webbeans.test.unittests.intercept;

import java.util.List;

import javax.servlet.ServletContext;

import junit.framework.Assert;

import org.apache.webbeans.component.AbstractBean;
import org.apache.webbeans.context.ContextFactory;
import org.apache.webbeans.test.component.intercept.InterceptedComponent;
import org.apache.webbeans.test.component.intercept.InterceptorWithSuperClassInterceptedComponent;
import org.apache.webbeans.test.component.intercept.MultipleInterceptedComponent;
import org.apache.webbeans.test.component.intercept.MultipleListOfInterceptedComponent;
import org.apache.webbeans.test.component.intercept.MultipleListOfInterceptedWithExcludeClassComponent;
import org.apache.webbeans.test.servlet.TestContext;
import org.junit.Before;
import org.junit.Test;

public class EJBInterceptComponentTest extends TestContext
{

    public EJBInterceptComponentTest()
    {
        super(EJBInterceptComponentTest.class.getName());
    }

    public void endTests(ServletContext ctx)
    {

    }

    @Before
    public void init()
    {
        super.init();
    }

    @Test
    public void testInterceptedComponent()
    {
        defineManagedBean(InterceptedComponent.class);
    }

    @Test
    public void testInterceptorCalls()
    {
        clear();
        defineManagedBean(InterceptedComponent.class);

        ContextFactory.initRequestContext(null);
        List<AbstractBean<?>> comps = getComponents();

        Object object = getManager().getInstance(comps.get(0));

        Assert.assertTrue(object instanceof InterceptedComponent);

        InterceptedComponent comp = (InterceptedComponent) object;
        Object s = comp.hello(null);

        Assert.assertEquals(new Integer(5), s);

        ContextFactory.destroyRequestContext(null);
    }

    @Test
    public void testMultipleInterceptedComponent()
    {
        clear();
        defineManagedBean(MultipleInterceptedComponent.class);

        ContextFactory.initRequestContext(null);
        List<AbstractBean<?>> comps = getComponents();

        Object object = getManager().getInstance(comps.get(0));

        Assert.assertTrue(object instanceof MultipleInterceptedComponent);

        MultipleInterceptedComponent comp = (MultipleInterceptedComponent) object;
        Object obj = comp.intercepted();

        Assert.assertTrue(obj instanceof String[]);

        String[] arr = (String[]) obj;

        Assert.assertEquals(2, arr.length);
        Assert.assertTrue("key".equals(arr[0]) && "key2".equals(arr[1]) || "key".equals(arr[1]) && "key2".equals(arr[0]));
        ContextFactory.destroyRequestContext(null);
    }

    @Test
    public void testInterceptorWithSuperClassComponent()
    {
        clear();
        defineManagedBean(InterceptorWithSuperClassInterceptedComponent.class);

        ContextFactory.initRequestContext(null);
        List<AbstractBean<?>> comps = getComponents();

        Object object = getManager().getInstance(comps.get(0));

        Assert.assertTrue(object instanceof InterceptorWithSuperClassInterceptedComponent);

        InterceptorWithSuperClassInterceptedComponent comp = (InterceptorWithSuperClassInterceptedComponent) object;
        Object obj = comp.intercepted();

        Assert.assertTrue(obj instanceof String[]);

        String[] arr = (String[]) obj;

        Assert.assertEquals(2, arr.length);
        Assert.assertTrue("key".equals(arr[0]) && "key0".equals(arr[1]) || "key".equals(arr[1]) && "key0".equals(arr[0]));
        ContextFactory.destroyRequestContext(null);
    }

    @Test
    public void testMultipleListOfInterceptedComponent()
    {
        clear();
        defineManagedBean(MultipleListOfInterceptedComponent.class);

        ContextFactory.initRequestContext(null);
        List<AbstractBean<?>> comps = getComponents();

        Object object = getManager().getInstance(comps.get(0));

        Assert.assertTrue(object instanceof MultipleListOfInterceptedComponent);

        MultipleListOfInterceptedComponent comp = (MultipleListOfInterceptedComponent) object;
        Object obj = comp.intercepted();

        Assert.assertTrue(obj instanceof String);

        Assert.assertEquals("ok", (String) obj);

        ContextFactory.destroyRequestContext(null);
    }

    @Test
    public void testMultipleListOfInterceptedWithExcludeClassComponent()
    {
        clear();
        defineManagedBean(MultipleListOfInterceptedWithExcludeClassComponent.class);

        ContextFactory.initRequestContext(null);
        List<AbstractBean<?>> comps = getComponents();

        Object object = getManager().getInstance(comps.get(0));

        Assert.assertTrue(object instanceof MultipleListOfInterceptedWithExcludeClassComponent);

        MultipleListOfInterceptedWithExcludeClassComponent comp = (MultipleListOfInterceptedWithExcludeClassComponent) object;
        Object obj = comp.intercepted();

        Assert.assertTrue(obj instanceof String);

        Assert.assertEquals("value2", (String) obj);

        ContextFactory.destroyRequestContext(null);
    }

}
