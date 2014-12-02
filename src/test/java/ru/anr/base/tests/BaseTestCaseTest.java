/**
 * 
 */
package ru.anr.base.tests;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ContextConfiguration;

import ru.anr.base.ApplicationException;

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
}
