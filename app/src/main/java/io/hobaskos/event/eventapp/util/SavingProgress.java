package io.hobaskos.event.eventapp.util;

import android.app.ProgressDialog;
import android.content.Context;

import io.hobaskos.event.eventapp.R;

/**
 * Created by alex on 4/24/17.
 */

public class SavingProgress extends ProgressDialog {

    private SavingProgress(Context context) {
        super(context, R.style.AppTheme_Dialog);
    }

    public static SavingProgress createAndShow(Context context) {
        SavingProgress sp = new SavingProgress(context);
        sp.setIndeterminate(true);
        sp.setMessage(context.getText(R.string.saving));
        sp.show();
        return sp;
    }
}
