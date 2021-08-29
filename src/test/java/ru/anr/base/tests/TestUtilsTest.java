package ru.anr.base.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import ru.anr.base.ParseUtils;

/**
 * Tests for {@link TestUtils}.
 *
 * @author Alexey Romanchuk
 * @created Aug 29, 2021
 */
@ContextConfiguration(classes = BaseTestCaseTest.class)
public class TestUtilsTest extends BaseTestCase {

    @Test
    void randomEmail() {
        Assertions.assertNotNull(
                ParseUtils.regexp(TestUtils.randomEmail(), "(\\d{1,})@mail.com", 1));
    }

    @Test
    void randomPhone() {
        Assertions.assertNotNull(ParseUtils.regexp(TestUtils.randomPhone(), "7912(\\d{1,7})", 1));
    }

    @Test
    void randomAmericanPhone() {
        Assertions.assertNotNull(ParseUtils.regexp(TestUtils.randomAmericanPhone(), "154175(\\d{1,5})", 1));
    }
}