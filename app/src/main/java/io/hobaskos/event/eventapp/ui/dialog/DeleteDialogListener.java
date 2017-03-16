package io.hobaskos.event.eventapp.ui.dialog;

/**
 * Created by osvold.hans.petter on 15.03.2017.
 */

public interface DeleteDialogListener<T> {
    void onDeleteButtonClicked(T item);
    void onCancelButtonClicked();
}
