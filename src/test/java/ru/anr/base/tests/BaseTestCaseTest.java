/**
 * 
 */
package ru.anr.base.tests;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * Tests for {@link BaseTestCase}
 *
 *
 * @author Alexey Romanchuk
 * @created 03 нояб. 2014 г.
 *
 */
@ContextConfiguration(classes = BaseTestCaseTest.class)
public class BaseTestCaseTest extends BaseTestCase {

    /**
     * Here is nothing to test
     */
    @Test
    public void testBean() {

        Assert.assertTrue(true);
    }
}
