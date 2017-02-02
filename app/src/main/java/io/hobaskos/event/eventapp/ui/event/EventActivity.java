package io.hobaskos.event.eventapp.ui.event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.BasePresenterActivity;
import io.hobaskos.event.eventapp.ui.base.PresenterFactory;

/**
 * Created by andre on 1/26/2017.
 */

public class EventActivity extends BasePresenterActivity<EventContract.Presenter, EventContract.View> implements EventContract.View {

    public final static String EVENT_ID = "eventId";

    @Inject public EventPresenter eventPresenter;


    @BindView(R.id.event_title) TextView eventTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getInst().getComponent().inject(this);
        renderView();
        init();

        Long eventId = getIntent().getExtras().getLong(EVENT_ID);

        eventPresenter.onViewAttached(this);
        eventPresenter.getEvent(eventId);
    }

    @NonNull
    @Override
    protected String tag() {
        return activity;
    }

    @NonNull
    @Override
    protected PresenterFactory<EventContract.Presenter> getPresenterFactory() {
        return new PresenterFactory<EventContract.Presenter>() {
            @NonNull @Override
            public EventPresenter create() {
                return new EventPresenter(this);
            }
        };
    }

    @Override
    protected void onPresenterPrepared(@NonNull EventContract.Presenter presenter) {

    }

    public void renderView() {
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
    }

    public void init(){
        //list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setData(Event data) {
        eventTitle.setText(data.getTitle());
    }
}
