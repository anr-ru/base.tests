/**
 * 
 */
package ru.anr.base.tests.multithreading;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.anr.base.BaseParent;

/**
 * A ThreadJob is a slight wrapper around the {@link Runnable} object which
 * allows to measure the time spent for execution and store an error message in
 * a case of an error. It also uses a special interface {@link DoJob} for
 * Java8-styled callback through a {@link FunctionalInterface}.
 *
 * ThreadJob job = new ThreadJon(x -&gt; {
 * 
 * // Write a test body
 * 
 * });
 * 
 * @author Alexey Romanchuk
 * @created Jul 3, 2015
 *
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
    private DoJob job;

    /**
     * A list of stored arguments to send as the job arguments
     */
    private List<Object> args = list();

    /**
     * The constructor
     * 
     * @param job
     *            The job to run
     */
    public ThreadJob(DoJob job) {

        this.job = job;
    }

    /**
     * Adding an argument for the job executor
     * 
     * @param objects
     *            Objects to add as the arguments
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

            // The main job is here
            job.execute(args.toArray(new Object[args.size()]));

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
     * @param ex
     *            An exception
     * @return A string with a message
     */
    protected String processException(Throwable ex) {

        return (ex.getMessage() == null) ? "error" : ex.getMessage();
    }

    /**
     * A simple interface to specify a job directly as a part of the main code
     */
    @FunctionalInterface
    public interface DoJob {

        /**
         * The execution method
         * 
         * @param args
         *            The arguments which have been added above in
         *            {@link ThreadJob#add(Object...)}
         */
        void execute(Object... args);
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
