/**
 *
 */
package ru.anr.base.tests;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
     * Empty for now
     */
}
