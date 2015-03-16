/**
 * 
 */
package ru.anr.base.tests;

import org.apache.commons.lang3.RandomUtils;

/**
 * Utils for tests.
 *
 *
 * @author Dmitry Philippov
 * @created Mar 12, 2015
 *
 */
public final class TestUtils {

    /**
     * Default constructor
     */
    private TestUtils() {

    }

    /**
     * @return random email
     */
    public static String randomEmail() {

        return String.format("%s@gmail.com", RandomUtils.nextInt(100000, 999999));
    }

    /**
     * @return random phone
     */
    public static String randomPhone() {

        return String.valueOf(RandomUtils.nextLong(10000000000L, 99999999999L));
    }
}
