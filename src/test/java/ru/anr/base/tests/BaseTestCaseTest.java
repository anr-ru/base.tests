/**
 *
 */
package ru.anr.base.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ContextConfiguration;
import ru.anr.base.ApplicationException;

import java.time.ZonedDateTime;
import java.util.Locale;

/**
 * Tests for {@link BaseTestCase}
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 3, 2014
 *
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
}
