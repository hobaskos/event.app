package io.hobaskos.event.eventapp.util;

import io.hobaskos.event.eventapp.config.Constants;

/**
 * Created by alex on 3/24/17.
 */

public final class UrlUtil {

    public static String getImageUrl(String s) {
        if (s.contains("http")) return s;
        return String.format("%s://%s:%d/api%s",
                Constants.API_SCHEME, Constants.API_HOST, Constants.API_PORT, s);
    }
}
