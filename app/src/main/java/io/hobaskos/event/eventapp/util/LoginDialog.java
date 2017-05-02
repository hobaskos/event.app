package io.hobaskos.event.eventapp.util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.ui.login.LoginActivity;

/**
 * Created by alex on 4/24/17.
 */
public class LoginDialog extends AlertDialog {

    private LoginDialog(Context context) {
        super(context);

        build(context);
    }

    public static LoginDialog createAndShow(Context context) {
        return new LoginDialog(context);
    }

    private void build(Context context) {
        new Builder(context).setTitle(R.string.login)
            .setMessage(R.string.must_be_logged_in)
            .setPositiveButton(R.string.login, (dialog, which) ->
                context.startActivity(new Intent(context, LoginActivity.class))
            )
            .setNegativeButton(R.string.close, (dialog, which) -> dismiss())
            .create()
            .show();
    }
}
