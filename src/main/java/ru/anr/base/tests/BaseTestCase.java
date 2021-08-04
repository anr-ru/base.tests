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

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Locale;

import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.anr.base.ApplicationException;
import ru.anr.base.BaseParent;
import ru.anr.base.BaseSpringParent;

/**
 * The base testcase - a pre-configured parent for all spring-based JUnit tests.
 *
 * @author Alexey Romanchuk
 * @created Oct 29, 2014
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
public class BaseTestCase extends BaseSpringParent {

    /**
     * Logger used in all tests
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Initialization for all tests
     */
    @Before
    public void setUp() {

        BaseParent.setClock(null); // reset the clock
        LocaleContextHolder.setLocale(Locale.ENGLISH); // default
    }

    /**
     * Builds a new bean by the provided bean class. This function is useful in
     * tests with mocks, when you need to create a bean with some mocked
     * properties without breaking the origin bean from the context.
     *
     * @param beanClass The class of the bean
     * @param <S>       The type of the bean
     * @return A bean instance
     */
    @Override
    protected <S> S bean(Class<S> beanClass) {

        return ctx.getAutowireCapableBeanFactory().createBean(beanClass);
    }

    /**
     * Assertion of specific Exception
     *
     * @param ex            Exception to be asserted
     * @param msgPart       A string to find in the exception message
     * @param expectedClass Exception class to be
     */
    public void assertException(Throwable ex, String msgPart, Class<?> expectedClass) {

        Throwable rootException = new ApplicationException(ex).getRootCause();

        if (expectedClass != null) {
            Assert.assertThat(rootException, new IsInstanceOf(expectedClass));
        }
        String msg = extractMessage(rootException);

        error("Exception: '{}' with the message: '{}'", rootException, msg);
        if (logger.isDebugEnabled()) {
            logger.debug("Exception details", ex);
        }
        if (msg == null) {
            Assert.assertEquals(msg, msgPart);
        } else {
            Assert.assertThat(msg, new StringContains(msgPart));
        }
    }

    /**
     * Extracts the message from the specified exception
     *
     * @param ex The exception
     * @return The message
     */
    protected String extractMessage(Throwable ex) {

        return ex.getMessage();
    }

    /**
     * Asserts the 'str' argument contains the specified string (the 'part'
     * argument)
     *
     * @param str  The string - container
     * @param part The part to check
     */
    public static void assertContains(String str, String part) {

        Assert.assertThat(str, new StringContains(part));
    }

    /**
     * Assertion of specific Exception
     *
     * @param ex      Exception to be asserted
     * @param msgPart A string to find in the exception message
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
         * @param x The argument
         * @throws Exception If a checked exception occurs
         */
        void doSomething(Object... x) throws Exception;
    }

    /**
     * Asserts an access denied error
     *
     * @param callback The callback to use
     * @param msg      The exception message to check
     * @param objects  The objects
     */
    protected void assertException(AssertedExceptionCallback callback, String msg, Object... objects) {

        try {
            callback.doSomething(objects);
            Assert.fail("Failure is expected");
        } catch (Throwable ex) {
            assertException(ex, msg);
        }
    }

    /**
     * Performs an expectation cycle during the specified number of seconds and
     * checks the condition on each iteration. Throws an {@link AssertionError}
     * if the expectation limit is exceeded.
     *
     * @param secs     The number of seconds
     * @param callback The callback
     * @param args     The arguments
     */
    protected static void assertWaitCondition(int secs, SleepCallback callback, Object... args) {

        if (waitCondition("Test Assertion", secs, true, callback, args)) {
            Assert.fail("Exceeded the limit of attempts: " + secs + " s");
        }
    }

    /**
     * Performs an expectation cycle during the specified number of seconds and
     * checks the condition on each iteration every msecSleep milliseconds.
     * Throws an {@link AssertionError} if the expectation limit is exceeded.
     *
     * @param secs      The number of seconds
     * @param msecSleep The number of
     * @param callback  The callback
     * @param args      The arguments
     */
    protected static void assertWaitCondition(int secs, int msecSleep, SleepCallback callback, Object... args) {

        if (waitCondition("Test Assertion", secs, msecSleep, true, callback, args)) {
            Assert.fail("Exceeded the limit of attempts: " + secs + " s");
        }
    }

    /**
     * A short-cut method to explain how to use mocks in tests. The 'Mockito'
     * objects provides plenty of static functions.
     *
     * @param clazz A class to create a mock object
     * @param <S>   The type
     * @return The mocked object
     */
    protected <S> S mock(Class<S> clazz) {

        return Mockito.mock(clazz);
    }

    /**
     * Fixes the time at the given point
     *
     * @param time The time to set
     */
    public static void fixTime(ZonedDateTime time) {

        setClock(Clock.fixed(time.toInstant(), DEFAULT_TIMEZONE));
    }
}
