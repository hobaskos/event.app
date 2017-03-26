package io.hobaskos.event.eventapp.util;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
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

    public static void setCategoryColorCardView(Context context, View view, EventCategoryTheme theme) {
        if (view == null) return;
        switch (theme) {
            case RED:
                setGradientBackground(context, view, ContextCompat.getColor(context, R.color.colorRed), ContextCompat.getColor(context, R.color.colorRedDark));
                //subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRedDark));
                break;
            case ORANGE:
                setGradientBackground(context, view, ContextCompat.getColor(context, R.color.colorOrange), ContextCompat.getColor(context, R.color.colorOrangeDark));
                //view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange));
                //subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrangeDark));
                break;
            case YELLOW:
                setGradientBackground(context, view, ContextCompat.getColor(context, R.color.colorYellow), ContextCompat.getColor(context, R.color.colorYellowDark));
                //view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorYellow));
                //subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorYellowDark));
                break;
            case GREEN:
                setGradientBackground(context, view, ContextCompat.getColor(context, R.color.colorGreen), ContextCompat.getColor(context, R.color.colorGreenDark));
                //view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
                //subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreenDark));
                break;
            case BLUE:
                setGradientBackground(context, view, ContextCompat.getColor(context, R.color.colorBlue), ContextCompat.getColor(context, R.color.colorBlueDark));
                //view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlue));
                //subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlueDark));
                break;
            case INDIGO:
                setGradientBackground(context, view, ContextCompat.getColor(context, R.color.colorIndigo), ContextCompat.getColor(context, R.color.colorIndigoDark));
                //view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorIndigo));
                //subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorIndigoDark));
                break;
            case VIOLET:
                setGradientBackground(context, view, ContextCompat.getColor(context, R.color.colorViolet), ContextCompat.getColor(context, R.color.colorVioletDark));
                //view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorViolet));
                //subView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorVioletDark));
                break;
        }
    }

    private static void setGradientBackground(Context context, View view, int bottomColor, int topColor) {
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {topColor, bottomColor});
        gd.setCornerRadius(0f);
        view.setBackground(gd);
    }
}
