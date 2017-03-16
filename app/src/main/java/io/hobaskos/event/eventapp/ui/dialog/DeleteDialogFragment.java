package io.hobaskos.event.eventapp.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;


import io.hobaskos.event.eventapp.R;

/**
 * Created by osvold.hans.petter on 15.03.2017.
 */

public class DeleteDialogFragment<T> extends DialogFragment  {

    public static final String ITEM_TO_BE_DELETED = "item_to_be_deleted";

    private T item;
    private DeleteDialogListener<T> listener;

    public void setItem(T item) {
        this.item = item;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DeleteDialogListener<T>) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement DeleteListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_location)
                .setPositiveButton(R.string.delete, (dialog, id) -> {
                    listener.onDeleteButtonClicked(item);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    // User cancelled the dialog
                    listener.onCancelButtonClicked();
                });
        return builder.create();
    }

}
