package io.hobaskos.event.eventapp.module;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by alex on 1/26/17.
 */

@Singleton
@Component(modules = {AppModule.class,
                      NetModule.class})
public interface DiComponent {
}
