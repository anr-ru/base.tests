/**
 * 
 */
package ru.anr.base.tests;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
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
     * Define a test bean
     * 
     * @return Bean instance
     */
    @Bean(name = "bean")
    public String factory() {

        return "Factory";
    }

    /**
     * Test method for
     * {@link ru.anr.base.tests.BaseTestCase#bean(java.lang.String, java.lang.Class)}
     * .
     */
    @Test
    public void testBean() {

        String x = bean("bean", String.class);
        Assert.assertEquals("Factory", x);
    }
}
