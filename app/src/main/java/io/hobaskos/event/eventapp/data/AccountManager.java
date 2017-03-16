package io.hobaskos.event.eventapp.data;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.data.repository.UserRepository;
import io.hobaskos.event.eventapp.data.storage.JwtStorageProxy;
import rx.Observable;

/**
 * Created by hansp on 25.02.2017.
 */

public class AccountManager {

    private final JwtStorageProxy jwtStorageProxy;
    private final UserRepository userRepository;

    /**
     *
     * @param jwtStorageProxy a Proxy to the PersistentStorage (Shared Preferences) class which handles the persistent storage of JWT tokens.
     * @param userRepository holds the reference to User Service and handles all communication with the backend.
     *
     */
    public AccountManager(JwtStorageProxy jwtStorageProxy, UserRepository userRepository)
    {
        this.jwtStorageProxy = jwtStorageProxy;
        this.userRepository = userRepository;
    }

    /**
     *
     * Checks if the user is logged in.
     * User is logged in if there exists a JWT-token in the shared-preferences or
     * a Facebook AccessToken.
     *
     * @return if the user is logged in or not.
     */
    public boolean isLoggedIn()
    {
        return jwtStorageProxy.isSet() || (AccessToken.getCurrentAccessToken() != null);
    }

    /**
     *
     * Logs the Account out by removing the JWT-token in shared-preferences.
     *
     */
    public void logout()
    {
        jwtStorageProxy.remove();
        LoginManager.getInstance().logOut();
    }

    /**
     *
     * Gets the User-object of the logged-in Account.
     *
     * @return the current User-object of the logged-in Account.
     */
    public Observable<User> getAccount()
    {
        return userRepository.getAccount();
    }

    public Observable<Void> saveAccount(User user)
    {
        return userRepository.saveAccount(user);
    }

    public User getLocalAccount() {
        return userRepository.getLocalAccount();
    }
}
