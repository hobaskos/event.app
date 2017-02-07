package io.hobaskos.event.eventapp.ui.createevent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import butterknife.BindView;
import io.hobaskos.event.eventapp.R;

public class CreateEventActivity extends AppCompatActivity {

    @BindView(R.id.title_input) EditText titleInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }
}
