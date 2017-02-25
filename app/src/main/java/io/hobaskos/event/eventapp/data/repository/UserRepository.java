package io.hobaskos.event.eventapp.data.repository;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.AccessToken;
import io.hobaskos.event.eventapp.data.model.SocialUserVM;
import io.hobaskos.event.eventapp.data.storage.FBAccessTokenStorageProxy;
import io.hobaskos.event.eventapp.data.storage.JwtStorageProxy;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hansp on 25.02.2017.
 */

public class UserRepository {

    private FBAccessTokenStorageProxy storageProxy;

    @Inject
    public UserRepository(FBAccessTokenStorageProxy storageProxy)
    {
        this.storageProxy = storageProxy;
    }

    public boolean login(SocialUserVM socialUserVM)
    {
        AccessToken accessToken = new AccessToken(socialUserVM.getUserId(), socialUserVM.getAccessToken());

        return storageProxy.put(accessToken);
    }

}
