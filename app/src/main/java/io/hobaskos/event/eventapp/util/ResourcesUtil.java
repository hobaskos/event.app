package io.hobaskos.event.eventapp.util;

import io.hobaskos.event.eventapp.App;

/**
 * Created by test on 3/30/2017.
 */

public final class ResourcesUtil {
    public static String getString(int resourceId) {
        return App.getInst().getResources().getString(resourceId);
    }
}
