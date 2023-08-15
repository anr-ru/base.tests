/*
 * Copyright 2014-2023 the original author or authors.
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

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import ru.anr.base.BaseParent;

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

    static UniformRandomProvider rng;

    static {
        rng = RandomSource.XO_RO_SHI_RO_128_PP.create();
    }

    /**
     * @return random email
     */
    public static String randomEmail() {
        return String.format("%s@mail.com", BaseParent.guid().replace("-", ""));
    }

    /**
     * Generates a valid phone number in Russia.
     *
     * @return random phone (in RU).
     */
    public static String randomPhone() {
        return String.valueOf(rng.nextLong(79120000000L, 79129999999L));
    }

    /**
     * Generates a valid phone number in the USA.
     *
     * @return random phone (in US).
     */
    public static String randomAmericanPhone() {
        return String.valueOf(rng.nextLong(15417500000L, 15417599999L));
    }
}
