/**
 *
 */
package ru.anr.base.tests;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ContextConfiguration;
import ru.anr.base.ApplicationException;

import java.io.UnsupportedEncodingException;
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

        Assert.assertNotNull(getEnv());
        Assert.assertEquals(Locale.ENGLISH, LocaleContextHolder.getLocale());
    }

    /**
     * Testing exception
     */
    @Test
    public void testBean() {
        try {
            throw new ApplicationException("Throw me");
        } catch (Exception ex) {
            assertException(ex, "Throw me", ApplicationException.class);
        }
    }

    /**
     * Testing exception
     */
    @Test
    public void testAssetException() {

        UnsupportedEncodingException e = new UnsupportedEncodingException("Wrong Something");
        assertException(args -> {
            throw new ApplicationException("Throw me", args[0]);
        }, "Wrong Something", e);
    }
}
