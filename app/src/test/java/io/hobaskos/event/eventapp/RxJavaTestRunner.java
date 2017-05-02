package io.hobaskos.event.eventapp;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;

import rx.Scheduler;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * Created by andre on 2/10/2017.
 */

public class RxJavaTestRunner extends RobolectricTestRunner {
    public RxJavaTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);

        RxJavaPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getIOScheduler() {
                return Schedulers.immediate();
            }
        });
    }
}
