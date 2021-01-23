package ru.anr.base.tests;

import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.anr.base.ApplicationException;
import ru.anr.base.BaseParent;
import ru.anr.base.BaseSpringParent;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * The base testcase - a pre-configured parent for all spring-based JUnit tests.
 *
 * @author Alexey Romanchuk
 * @created Oct 29, 2014
 */
@ExtendWith(SpringExtension.class)
@Disabled
public class BaseTestCase extends BaseSpringParent {

    /**
     * Logger used in all tests
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Initialization for all tests
     */
    @BeforeEach
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
            assertThat(rootException, new IsInstanceOf(expectedClass));
        }
        String msg = rootException == null ? null : extractMessage(rootException);

        error("Exception: '{}' with the message: '{}'", rootException, msg);
        if (logger.isDebugEnabled()) {
            logger.debug("Exception details", ex);
        }
        if (msg == null) {
            Assertions.assertNull(msgPart);
        } else {
            assertThat(msg, new StringContains(true, msgPart));
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

        assertThat(str, new StringContains(true, part));
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
            Assertions.fail("Failure is expected");
        } catch (Exception ex) {
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
            Assertions.fail("Exceeded the limit of attempts: " + secs + " s");
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
            Assertions.fail("Exceeded the limit of attempts: " + secs + " s");
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
