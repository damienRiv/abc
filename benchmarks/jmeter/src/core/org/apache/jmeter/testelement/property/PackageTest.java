/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.jmeter.testelement.property;

import junit.framework.TestCase;

import org.apache.jmeter.config.LoginConfig;

/**
 *Class for testing the property package.
 */
public class PackageTest extends TestCase
{

    public PackageTest(String name)
    {
        super(name);
    }
    
    public void testStringProperty() throws Exception
    {
        StringProperty prop = new StringProperty("name","value");
        prop.setRunningVersion(true);
        prop.setObjectValue("new Value");
        assertEquals("new Value",prop.getStringValue());
        prop.recoverRunningVersion(null);
        assertEquals("value",prop.getStringValue());
        prop.setObjectValue("new Value");
        prop.setObjectValue("2nd Value");
        assertEquals("2nd Value",prop.getStringValue());
        prop.recoverRunningVersion(null);
        assertEquals("value",prop.getStringValue());        
    }
    
    public void testElementProperty() throws Exception
        {
            LoginConfig config = new LoginConfig();
            config.setUsername("username");
            config.setPassword("password");
            TestElementProperty prop = new TestElementProperty("name",config);
            prop.setRunningVersion(true);
            config = new LoginConfig();
            config.setUsername("user2");
            config.setPassword("pass2");
            prop.setObjectValue(config);
            assertEquals("user2=pass2",prop.getStringValue());
            prop.recoverRunningVersion(null);
            assertEquals("username=password",prop.getStringValue());
            config = new LoginConfig();
            config.setUsername("user2");
            config.setPassword("pass2");
            prop.setObjectValue(config);
            config = new LoginConfig();
            config.setUsername("user3");
            config.setPassword("pass3");
            prop.setObjectValue(config);
            assertEquals("user3=pass3",prop.getStringValue());
            prop.recoverRunningVersion(null);
            assertEquals("username=password",prop.getStringValue());        
        }
}
