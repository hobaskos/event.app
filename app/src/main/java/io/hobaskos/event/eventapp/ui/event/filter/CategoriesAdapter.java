package io.hobaskos.event.eventapp.ui.event.filter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import io.hobaskos.event.eventapp.data.model.EventCategory;

/**
 * Created by andre on 3/8/2017.
 */

public class CategoriesAdapter extends ArrayAdapter<EventCategory> {
    public CategoriesAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }
}
