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

        return String.valueOf(RandomUtils.nextLong(79120000000L, 79129999999L));
    }

    /**
     * @return random phone
     */
    public static String randomAmericanPhone() {

        return String.valueOf(RandomUtils.nextLong(15417500000L, 15417599999L));
    }
}
