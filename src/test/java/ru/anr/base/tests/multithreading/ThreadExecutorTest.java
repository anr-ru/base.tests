package ru.anr.base.tests.multithreading;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import ru.anr.base.ApplicationException;
import ru.anr.base.tests.BaseTestCase;

import java.util.Set;

/**
 * Description ...
 *
 * @author Alexey Romanchuk
 * @created Jul 3, 2015
 */
@ContextConfiguration(classes = ThreadExecutorTest.class)
public class ThreadExecutorTest extends BaseTestCase {

    /**
     * Test for 2 threads
     */
    @Test
    public void test() {

        ThreadExecutor exec = new ThreadExecutor();

        exec.add(new ThreadJob(x -> {
            // do nothing
        }));

        exec.add(new ThreadJob(x -> {
            throw new ApplicationException("Message");
        }));

        exec.start();
        Assertions.assertFalse(exec.waitNotError()); // Must be an error

        Set<ThreadJob> jobs = exec.getJobs();
        Assertions.assertEquals("Message", jobs.stream()
                .filter(j -> j.getError() != null)
                .findFirst().orElseThrow().getError());
    }
}
