package io.hobaskos.event.eventapp.data.api;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by hansp on 14.03.2017.
 */

public class DateTimeSerializer implements JsonSerializer {
    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        Log.i("DateTimeSerializer", src.toString());
        return new JsonPrimitive(src.toString());

    }
}
