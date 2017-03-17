package io.hobaskos.event.eventapp.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.EventCategoryTheme;

/**
 * Created by test on 3/17/2017.
 */

public final class ColorUtil {

    public static void setCategoryColorView(Context context, View mainView, View subView, EventCategoryTheme theme) {
        if (mainView == null || subView == null) return;
        switch (theme) {
            case RED:
                mainView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRed));
                subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRedDark));
                break;
            case ORANGE:
                mainView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange));
                subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrangeDark));
                break;
            case YELLOW:
                mainView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorYellow));
                subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorYellowDark));
                break;
            case GREEN:
                mainView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
                subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreenDark));
                break;
            case BLUE:
                mainView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlue));
                subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlueDark));
                break;
            case INDIGO:
                mainView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorIndigo));
                subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorIndigoDark));
                break;
            case VIOLET:
                mainView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorViolet));
                subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorVioletDark));
                break;
        }
    }
}
