package io.hobaskos.event.eventapp.ui.login;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.repository.JwtRepository;
import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by osvold.hans.petter on 15.02.2017.
 */

public class LoginPresenterTest {

    @Mock
    private JwtRepository repository;

    private LoginPresenter loginPresenter;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        repository = mock(JwtRepository.class);
        loginPresenter = new LoginPresenter(repository);

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
        LoginVM loginVM = new LoginVM("user", "admin");

        repository.login(loginVM);

        when(repository.login(loginVM)).thenReturn(Observable.create((subscriber) -> {
            subscriber.onError(new Exception());
            subscriber.onCompleted();
        }));


    }

    @Test
    public void loginWithCorrectCredentialsShouldCallSuccessMethod()
    {
        LoginVM loginVM = new LoginVM("admin", "admin");

        repository.login(loginVM);

    }



}
