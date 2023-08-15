[![Build Status](https://app.travis-ci.com/anr-ru/base.tests.svg?branch=master)](https://app.travis-ci.com/anr-ru/base.tests.svg?branch=master)

## A part of [Base.Platform Project](https://github.com/anr-ru/base.platform.parent)

### Base.Tests

The [BaseTestCase](./src/main/java/ru/anr/base/tests/BaseTestCase.java) class is
a parent class for JUnit tests. It contains several short-cut and utility functions
to make writing JUnit tests easier. For example:

```java

import ru.anr.base.BaseParent;

@ContextConfiguration(classes = BaseTestCaseTest.class)
public class BaseTestCaseTest extends BaseTestCase {

    /**
     * Use case: freeze the time ...
     */

    @Test
    public void testFixedTime() {

        ZonedDateTime z = now();
        fixTime(z);

        sleep(1000);
        Assertions.assertEquals(z, now());

        // Unfreeze
        BaseParent.setClock(null);
    }

}
```

The [ThreadExecutor](./src/main/java/ru/anr/base/tests/multithreading/ThreadExecutor.java) is
used for writing multi-threading tests. For example:

```java

@ContextConfiguration(classes = ThreadExecutorTest.class)
public class ThreadExecutorTest extends BaseTestCase {

    /**
     * Use case: we run a multithreaded test with two threads.
     */
    @Test
    public void test() {

        ThreadExecutor exec = new ThreadExecutor();

        // A normal thread
        exec.add(new ThreadJob(x -> {
            // do nothing
            sleep(20);
        }));

        // A thread with an error
        exec.add(new ThreadJob(x -> {
            throw new ApplicationException("Message");
        }));

        // Let's start ...
        exec.start();

        Assertions.assertTrue(exec.waitNoErrors()); // We expect no errors
    }
}

```
