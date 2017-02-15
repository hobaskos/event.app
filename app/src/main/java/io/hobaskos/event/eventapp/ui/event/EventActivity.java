package io.hobaskos.event.eventapp.ui.event;

import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.view.activity.BaseActivity;

/**
 * Created by andre on 1/26/2017.
 */
public class EventActivity extends BaseActivity implements EventView {

    public final static String EVENT_ID = "eventId";
    public final static String TAG = EventActivity.class.getName();

    private Long eventId;

    //@BindView(R.id.event_title1) TextView eventTitle;

    private TextView eventTitle;

    @Inject public EventPresenter eventPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);
        //ButterKnife.bind(this);
        eventTitle = (TextView) findViewById(R.id.event_title1);
        eventTitle.setText("TEST");

        eventId = getIntent().getExtras().getLong(EVENT_ID);
    }





    @Override
    public void showLoading(boolean loading) {

    }

    /*
    public void showError(Throwable e) {
        Log.i("event-activity", e.getMessage());
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    */

    @Override
    public void showContent() {

    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    public void setData(Event event) {
        eventTitle.setText(String.format("ID:%d, TITLE:%s", event.getId(), event.getTitle()));
    }

    @Override
    public void loadData(boolean pullToRefresh) {

    }
}
