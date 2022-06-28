package ru.anr.base.tests;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Locale;

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
import java.util.function.Consumer;
import java.util.function.Function;

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
     * properties without breaking down the original bean from the context.
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
     * Assertion of specific Exception with the verification of the root exception in the stack.
     *
     * @param ex      The thrown exception
     * @param msgPart A string to find in the exception message
     */
    public void assertException(Throwable ex, String msgPart) {

        Throwable rootException = new ApplicationException(ex).getRootCause();
        String msg = nullSafe(rootException, Throwable::getMessage).orElse(null);

        if (logger.isDebugEnabled()) {
            logger.debug("Exception details: {}", ex.getMessage(), ex);
        }
        if (msg == null) {
            Assertions.assertNull(msgPart);
        } else {
            assertThat(msg, new StringContains(true, msgPart));
        }
    }

    /**
     * Asserts the 'strToCheck' argument contains the specified string (the 'part'
     * argument)
     *
     * @param strToCheck   The string to examine.
     * @param subStrToFind The substring to find
     */
    public static void assertContains(String strToCheck, String subStrToFind) {
        assertThat(strToCheck, new StringContains(true, subStrToFind));
    }

    /**
     * Executes the code in the given callback and verifies whether the code generates an exception
     * with the given string in the message or not.
     *
     * @param callback The callback to use
     * @param msg      The exception message to check
     * @param objects  The objects
     */
    protected void assertException(Consumer<Object[]> callback, String msg, Object... objects) {
        try {
            callback.accept(objects);
            Assertions.fail("Failure is expected");
        } catch (Throwable ex) {
            assertException(ex, msg);
        }
    }

    /**
     * Waits when the given callback is executed the specified number of seconds and
     * checks the condition on each iteration. Throws an {@link AssertionError}
     * if the expected number of seconds is exceeded.
     *
     * @param secs     The number of seconds
     * @param callback The callback
     * @param args     The arguments
     */
    protected static void assertWaitCondition(int secs, Function<Object[], Boolean> callback, Object... args) {
        if (waitCondition("Test Assertion", secs, true, callback, args)) {
            Assertions.fail("Exceeded the limit of attempts: " + secs + " s");
        }
    }

    /**
     * Waits when the given callback is executed the specified number of seconds and
     * checks the condition on each iteration. Throws an {@link AssertionError}
     * if the expected number of seconds is exceeded.
     *
     * @param secs      The number of seconds
     * @param msecSleep The number of
     * @param callback  The callback
     * @param args      The arguments
     */
    protected static void assertWaitCondition(int secs, int msecSleep, Function<Object[], Boolean> callback, Object... args) {
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
