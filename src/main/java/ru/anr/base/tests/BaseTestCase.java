/*
 * Copyright 2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ru.anr.base.tests;

import java.util.Locale;

import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.anr.base.ApplicationException;
import ru.anr.base.BaseSpringParent;

/**
 * Base testcase - preconfigured parent for all JUnit tests. It's Spring-based
 *
 *
 * @author Alexey Romanchuk
 * @created Oct 29, 2014
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
public class BaseTestCase extends BaseSpringParent {

    /**
     * Logger used in all tests
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Initialilzation for all tests
     */
    @Before
    public void setUp() {

        LocaleContextHolder.setLocale(Locale.ENGLISH); // default

    }

    /**
     * Builds a new bean by the provided bean class. This function is useful in
     * tests with mocks, when you need to create a bean with some mocked
     * properties without breaking the origin bean from the context.
     * 
     * @param beanClass
     *            The class of the bean
     * @return A bean instance
     * 
     * @param <S>
     *            The type of the bean
     * 
     */
    protected <S> S bean(Class<S> beanClass) {

        return ctx.getAutowireCapableBeanFactory().createBean(beanClass);
    }

    /**
     * Assertion of specific Exception
     * 
     * @param ex
     *            Exception to be asserted
     * @param msgPart
     *            A string to find in the exception message
     * @param expectedClass
     *            Exception class to be
     */
    public void assertException(Throwable ex, String msgPart, Class<?> expectedClass) {

        Throwable rootException = new ApplicationException(ex).getRootCause();

        if (expectedClass != null) {
            Assert.assertThat(rootException, new IsInstanceOf(expectedClass));
        }
        String msg = extractMessage(rootException);
        Assert.assertThat(msg, new StringContains(msgPart));
    }

    /**
     * Extracts the message from the specified exception
     * 
     * @param ex
     *            The exception
     * @return The message
     */
    protected String extractMessage(Throwable ex) {

        return ex.getMessage();
    }

    /**
     * Asserts the 'str' argument contains the specified string (the 'part'
     * argument)
     * 
     * @param str
     *            The string - container
     * @param part
     *            The part to check
     */
    public static void assertContains(String str, String part) {

        Assert.assertThat(str, new StringContains(part));
    }

    /**
     * Assertion of specific Exception
     * 
     * @param ex
     *            Exception to be asserted
     * @param msgPart
     *            A string to find in the exception message
     */
    public void assertException(Throwable ex, String msgPart) {

        assertException(ex, msgPart, null);
    }

    /**
     * Just a call back
     */
    @FunctionalInterface
    public interface AssertedExceptionCallback {

        /**
         * Does something
         * 
         * @param x
         *            The argument
         */
        void doSomething(Object... x);
    }

    /**
     * Asserts an access denied error
     * 
     * @param callback
     *            The callback to use
     * @param msg
     *            The exception message to check
     * @param objects
     *            The objects
     */
    protected void assertException(AssertedExceptionCallback callback, String msg, Object... objects) {

        try {
            callback.doSomething(objects);
            Assert.fail("Failure is expected");
        } catch (Exception ex) {
            assertException(ex, msg);
        }
    }
}
