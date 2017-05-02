package io.hobaskos.event.eventapp.util;


import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by hansp on 11.03.2017.
 */

public final class ImageUtil {

    public static final int CAPTURE_IMAGE_REQUEST = 0;
    public static final int PICK_IMAGE_REQUEST = 2;

    public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }



}
