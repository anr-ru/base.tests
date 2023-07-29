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

package ru.anr.base.tests.multithreading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.anr.base.BaseParent;

import java.util.List;
import java.util.function.Consumer;

/**
 * A ThreadJob is a slight wrapper around the {@link Runnable} object which
 * allows to measure the time spent for execution and store an error message in
 * a case of an error. It also uses a special callback through a {@link Consumer}.
 * <pre>
 * ThreadJob job = new ThreadJob(x -&gt; {
 *
 * // Write a test body
 *
 * });
 * </pre>
 *
 * @author Alexey Romanchuk
 * @created Jul 3, 2015
 */

public class ThreadJob extends BaseParent implements Runnable {

    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ThreadJob.class);

    /**
     * Spent time
     */
    private long time = 0;

    /**
     * An error message if it was
     */
    private String error;

    /**
     * The job to do
     */
    private final Consumer<Object[]> job;

    /**
     * A list of stored arguments to send as the job arguments
     */
    private final List<Object> args = list();

    /**
     * The constructor
     *
     * @param job The job to run
     */
    public ThreadJob(Consumer<Object[]> job) {

        this.job = job;
    }

    /**
     * Adding an argument for the job executor
     *
     * @param objects Objects to add as the arguments
     */
    public void add(Object... objects) {
        args.addAll(list(objects));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

        this.error = null;
        this.time = 0;

        try {

            long started = System.currentTimeMillis();

            logger.info("Job started");
            // The main job is here
            job.accept(args.toArray(new Object[0]));

            time = System.currentTimeMillis() - started;
            logger.info("The job completed in {} ms", time);

        } catch (Throwable ex) {

            this.error = processException(ex);
            logger.error("Thread exception: " + this.error, ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {

        return "[" + time + " ms, error=" + nullSafe(error) + "]";
    }

    /**
     * The default error handler
     *
     * @param ex An exception
     * @return A string with a message
     */
    protected String processException(Throwable ex) {

        return (ex.getMessage() == null) ? "error" : ex.getMessage();
    }


    // /////////////////////////////////////////////////////////////////////////
    // /// getters/setters
    // /////////////////////////////////////////////////////////////////////////

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }
}
