package io.hobaskos.event.eventapp.ui.event.details.competition.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hobaskos.event.eventapp.R;
import rx.functions.Action1;

/**
 * Created by alex on 4/20/17.
 */
public class UploadImageFragment extends DialogFragment {

    @BindView(R.id.image)
    protected ImageView image;

    @BindView(R.id.title)
    protected EditText title;

    private Bitmap bitmap;
    private Action1<String> action;

    public static UploadImageFragment newInstance() {
        return new UploadImageFragment();
    }

    public UploadImageFragment onConfirmAction(Action1<String> action) {
        this.action = action;
        return this;
    }

    public UploadImageFragment setImage(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.UploadImageDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_upload_image, container, false);

        getDialog().setTitle(getContext().getString(R.string.upload_new_image));
        ButterKnife.bind(this, view);

        image.setImageBitmap(bitmap);
        return view;
    }

    @OnClick(R.id.close_btn)
    protected void close() {
        dismiss();
    }

    @OnClick(R.id.send_btn)
    protected void send() {
        action.call(title.getText().toString());
        dismiss();
    }
}
