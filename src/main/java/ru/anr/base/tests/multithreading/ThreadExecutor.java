/*
 * Copyright 2014-2024 the original author or authors.
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

package ru.anr.base.tests.multithreading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.anr.base.BaseParent;

import java.lang.Thread.State;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * The main executor with stores all threads participating in the execution and
 * their original jobs. It has functions to start all threads and check the
 * results of their executions.
 *
 * @author Alexey Romanchuk
 * @created Jul 3, 2015
 */

public class ThreadExecutor extends BaseParent {

    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ThreadExecutor.class);

    /**
     * A map with stored references to thread and its {@link ThreadJob}
     */
    private final Map<Thread, ThreadJob> threads = toMap();

    /**
     * The default constructor
     */
    public ThreadExecutor() {

    }

    /**
     * Adds a job to the execution pool
     *
     * @param job The job to add
     */
    public void add(ThreadJob job) {
        threads.put(new Thread(job), job);
    }

    /**
     * A constructor with a full list of jobs
     *
     * @param jobs The list
     */
    public ThreadExecutor(ThreadJob... jobs) {
        list(jobs).forEach(this::add);
    }

    /**
     * A constructor with the same job which must be repeated multiple times
     *
     * @param job    The job
     * @param number The number of threads
     */
    public ThreadExecutor(ThreadJob job, int number) {
        for (int i = 0; i < number; i++) {
            add(job);
        }
    }

    // ///////////////////// methods

    /**
     * Starts all jobs
     */
    public void start() {
        result.error = false;
        threads.keySet().forEach(Thread::start);
    }

    /**
     * The random generator
     */
    private static final Random random = new Random();

    /**
     * Allows sleeping for a random number of milliseconds
     *
     * @param from   The lower border of the sleeping interval
     * @param length The maximum length for the sleeping interval (from + length =
     *               upper border)
     */
    public static void sleep(long from, int length) {
        sleep(from + random.nextInt(length));
    }

    /**
     * The stored result of the execution
     */
    private final TestResult result = new TestResult();

    /**
     * Waits until the jobs complete their execution
     *
     * @return true, all threads completed without errors
     */
    public boolean waitNoErrors() {

        Set<Thread> s = new HashSet<Thread>(threads.keySet());

        while (!s.isEmpty()) {
            Set<Thread> rm = set();
            s.forEach(a -> {
                if (a.getState() == State.TERMINATED) {
                    rm.add(a);
                }
                sleep(10, 10);
            });
            s.removeAll(rm);
        }

        threads.forEach((k, v) -> {

            logger.debug("Thread({}): {} = {} ms, result: {}", k.getState(),
                    k.getName(), (v.getTime()), nullSafe(v.getError()));

            if (v.getError() != null) {
                result.error = true;
            }
        });

        if (result.error) {
            dumpStatistic();
        }
        return !result.error;
    }

    /**
     * Writes statistic in the log
     */
    public void dumpStatistic() {

        threads.forEach((k, v) -> {
            logger.info("Thread({}): {} = {} ms, result: {}", k.getState(), k.getName(), (v.getTime()),
                    nullSafe(v.getError()));
        });
    }

    /**
     * Returns the jobs
     *
     * @return A set of jobs
     */
    public Set<ThreadJob> getJobs() {
        return new HashSet<>(threads.values());
    }

    /**
     * A class to store the result
     */
    private static class TestResult {

        /**
         * A string with an error
         */
        private boolean error;
    }

    /**
     * Returns true if the last execution completed with an error
     *
     * @return true, if there was an error
     */
    public boolean isError() {
        return result.error;
    }
}
