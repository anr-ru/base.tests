package ru.anr.base.tests;

/**
 * @author Alexey Romanchuk
 * @created Jun 22, 2022
 */
public class SampleStrategy {

    private final SampleService service;

    public SampleStrategy(SampleService service) {
        this.service = service;
    }

    public String doWork(String arg) {
        return service.someMethod(arg);
    }
}
