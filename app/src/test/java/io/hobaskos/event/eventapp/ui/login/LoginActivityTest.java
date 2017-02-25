package io.hobaskos.event.eventapp.ui.login;

import android.content.Intent;
import android.support.design.widget.TabLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.BuildConfig;
import io.hobaskos.event.eventapp.TestApp;
import io.hobaskos.event.eventapp.data.PersistentStorage;
import io.hobaskos.event.eventapp.data.api.UserJWTService;
import io.hobaskos.event.eventapp.data.model.JwtToken;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.repository.JwtRepository;
import io.hobaskos.event.eventapp.data.service.JwtStorageProxy;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by osvold.hans.petter on 15.02.2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23, application = TestApp.class)
@Ignore
public class LoginActivityTest {

    @Mock
    private UserJWTService service;

    private JwtStorageProxy storageProxy;

    @Mock
    private JwtRepository repository;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);

        //service = mock(UserJWTService.class);

        PersistentStorage persistentStorage = mock(PersistentStorage.class);

        storageProxy = new JwtStorageProxy(persistentStorage);
        repository = new JwtRepository(service, storageProxy);

        // Override RxJava schedulers
        RxJavaHooks.setOnIOScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.immediate();
            }
        });

        RxJavaHooks.setOnComputationScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.immediate();
            }
        });

        RxJavaHooks.setOnNewThreadScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.immediate();
            }
        });

        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @After
    public void tearDown()
    {
        RxAndroidPlugins.getInstance().reset();
    }

    @Test
    public void loginWithWrongCredentialsShouldCallOnErrorMethod()
    {

    }

    @Test
    public void loginWithCorrectCredentialsShouldCallSuccessMethod()
    {


    }

    @Test
    public void loginWithCorrectCredentialsShouldSaveTokenInSharedPreferences()
    {
        if(storageProxy.isSet())
        {
            storageProxy.remove();
        }

        assertFalse(storageProxy.isSet());

        LoginVM loginVM = new LoginVM("right", "password");

        String json = "{\n" +
                "  \"id_token\": \"eJhbGciOiJIUz4VvF\"\n" +
                "}";

        JwtToken jwtToken = new JwtToken();
        jwtToken.setIdToken("eJhbGciOiJIUz4VvF");

        when(service.login(loginVM)).thenReturn(Observable.create((subscriber) -> {
            subscriber.onNext(jwtToken);
            subscriber.onCompleted();
        }));

        repository.login(loginVM).toBlocking().first();

        assertFalse(storageProxy.isSet());

    }

    @Test
    public void loginWithWrongCredentialsShouldNotSaveToken()
    {
        if(storageProxy.isSet())
        {
            storageProxy.remove();
        }

        assertFalse(storageProxy.isSet());

        LoginVM loginVM = new LoginVM("wrong", "password");

        MediaType mediaType = MediaType.parse("application");
        String json = "{\n" +
                "  \"AuthenticationException\": \"Bad credentials\"\n" +
                "}";

        ResponseBody responseBody = ResponseBody.create(mediaType, json);

        HttpException httpException = new HttpException(Response.error(401, responseBody ));

        when(service.login(loginVM)).thenReturn(Observable.create((subscriber) -> {
            subscriber.onError(httpException);
        }));

        repository.login(loginVM);

        assertFalse(storageProxy.isSet());
    }



}
