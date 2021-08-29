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

import org.apache.commons.lang3.RandomUtils;

/**
 * Useful things in testing.
 *
 * @author Dmitry Philippov
 * @created Mar 12, 2015
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
        return String.format("%s@mail.com", RandomUtils.nextInt(100000, 999999));
    }

    /**
     * Generates a valid phone number in Russia.
     *
     * @return random phone (in RU).
     */
    public static String randomPhone() {
        return String.valueOf(RandomUtils.nextLong(79120000000L, 79129999999L));
    }

    /**
     * Generates a valid phone number in the USA.
     *
     * @return random phone (in US).
     */
    public static String randomAmericanPhone() {
        return String.valueOf(RandomUtils.nextLong(15417500000L, 15417599999L));
    }
}
