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
     * Assertion of specific Exception
     * 
     * @param ex
     *            Exception to be asserted
     * @param msgPart
     *            A string to find in the exception message
     * @param expectedClass
     *            Exception class to be
     */
    public static void assertException(Throwable ex, String msgPart, Class<?> expectedClass) {

        Throwable rootException = new ApplicationException(ex).getRootCause();

        if (expectedClass != null) {
            Assert.assertThat(rootException, new IsInstanceOf(expectedClass));
        }
        Assert.assertThat(rootException.getMessage(), new StringContains(msgPart));
    }

    /**
     * Assertion of specific Exception
     * 
     * @param ex
     *            Exception to be asserted
     * @param msgPart
     *            A string to find in the exception message
     */
    public static void assertException(Throwable ex, String msgPart) {

        assertException(ex, msgPart, null);
    }
}
