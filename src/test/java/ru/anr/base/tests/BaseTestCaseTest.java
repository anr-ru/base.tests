/**
 *
 */
package ru.anr.base.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ContextConfiguration;
import ru.anr.base.ApplicationException;

import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Locale;

/**
 * Tests for {@link BaseTestCase}
 *
 * @author Alexey Romanchuk
 * @created Nov 3, 2014
 */
@ContextConfiguration(classes = BaseTestCaseTest.class)
public class BaseTestCaseTest extends BaseTestCase {

    /**
     * Here is nothing to test
     */
    @Test
    public void testInit() {

        Assertions.assertNotNull(getEnv());
        Assertions.assertEquals(Locale.ENGLISH, LocaleContextHolder.getLocale());
    }

    /**
     * Testing exception
     */
    @Test
    public void testBean() {
        try {
            throw new ApplicationException("Throw me");
        } catch (Exception ex) {
            assertException(ex, "Throw me");
        }
    }

    @Test
    public void testAssertContains() {
        assertContains("X + Y = Z", "X +");
        assertContains("X + Y = Z", "");
    }

    @Test
    public void testFixedTime() {

        ZonedDateTime z = now();
        fixTime(z);

        sleep(1000);
        Assertions.assertEquals(z, now());
    }

    @Test
    public void testLocale() {
        Assertions.assertEquals(Locale.ENGLISH, LocaleContextHolder.getLocale());
    }

    @Test
    public void testExceptions() {

        assertException(args -> {
            throw new ApplicationException("Exception raised");
        }, "Exception raised");

        assertException(args -> {
            throw new ApplicationException("Exception raised: " + args[0]);
        }, "Exception raised: 10", 10);


    }

    /**
     * Use case: a negative case when no exception is thrown
     */
    @Test
    public void testNoExceptions() {
        AssertionError fail = Assertions.assertThrows(AssertionError.class, () -> {
            assertException(args -> log("Do my job"), "Exception raised");
        });
        Assertions.assertEquals("\nExpected: a string containing \"Exception raised\" ignoring case\n" +
                "     but: was \"Failure is expected\"", fail.getMessage());
    }

    /**
     * Use case: assert wait functions
     */
    @Test
    public void testAssertWait() {

        // Negative case
        AssertionError fail = Assertions.assertThrows(AssertionError.class, () -> assertWaitCondition(5, args -> false));
        Assertions.assertEquals("Exceeded the limit of attempts: 5 s", fail.getMessage());

        // Positive case
        assertWaitCondition(7, args -> true);

        // Doing iteration more often
        fail = Assertions.assertThrows(AssertionError.class, () -> assertWaitCondition(7, 10, args -> false));
        Assertions.assertEquals("Exceeded the limit of attempts: 7 s", fail.getMessage());

        // Positive case
        assertWaitCondition(10, 100, args -> true);
    }

    @Test
    public void testMock() {

        SampleService mock = mock(SampleService.class);
        Mockito.when(mock.someMethod(Mockito.eq("X"))).thenReturn("Y");

        SampleStrategy s = new SampleStrategy(mock);
        Assertions.assertEquals("Y", s.doWork("X"));

        Mockito.verify(mock).someMethod(Mockito.eq("X"));
    }

    @Test
    public void testMockCapture() {

        SampleService mock = mock(SampleService.class);

        ArgumentCaptor<String> args = ArgumentCaptor.forClass(String.class);

        Mockito.when(mock.someMethod(Mockito.eq("X"))).thenReturn("Y");

        SampleStrategy s = new SampleStrategy(mock);
        Assertions.assertEquals("Y", s.doWork("X"));

        Mockito.verify(mock).someMethod(args.capture());
        Assertions.assertEquals("X", args.getValue());
    }


    /**
     * Testing exception
     */
    @Test
    public void testAssetException() {

        UnsupportedEncodingException e = new UnsupportedEncodingException("Wrong Something");
        assertException(args -> {
            throw new ApplicationException("Throw me", (Throwable) args[0]);
        }, "Wrong Something", e);
    }
}
