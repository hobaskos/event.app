package io.hobaskos.event.eventapp.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hobaskos.event.eventapp.R;

public class JoinPrivateEventFragment extends DialogFragment {

    @BindView(R.id.invitation_code)
    protected EditText inviteCode;

    private OnInviteCodeSubmitInteractionListener listener;

    public static JoinPrivateEventFragment newInstance() {
        return new JoinPrivateEventFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.JoinEventDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_join_event, container, false);

        getDialog().setTitle(getContext().getString(R.string.join_private_event));
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.close_btn)
    protected void close() {
        dismiss();
    }

    @OnClick(R.id.send_btn)
    protected void send() {
        if (inviteCode.getText().toString().length() < 4) {
            inviteCode.setError(getContext().getString(R.string.invite_code_length_error));
            return;
        }
        listener.onInviteCodeSubmitInteractionListener(inviteCode.getText().toString());
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInviteCodeSubmitInteractionListener) {
            listener = (OnInviteCodeSubmitInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    public interface OnInviteCodeSubmitInteractionListener {
        void onInviteCodeSubmitInteractionListener(String inviteCode);
    }
}
