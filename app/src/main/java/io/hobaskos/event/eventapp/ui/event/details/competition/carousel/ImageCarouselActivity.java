package io.hobaskos.event.eventapp.ui.event.details.competition.carousel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.SupportActivity;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.CompetitionImage;
import io.hobaskos.event.eventapp.ui.event.details.competition.list.CompetitionFragment;
import io.hobaskos.event.eventapp.ui.event.details.competition.list.OnCompetitionListInteractionListener;

/**
 * Created by hans on 23/03/2017.
 */

public class ImageCarouselActivity extends AppCompatActivity
        implements OnCompetitionListInteractionListener {

    public static final String ARG_COMPETITION_ID = "competitionId";
    public static final String ARG_EVENT_TITLE = "eventTitle";
    public static final String ARG_EVENT_GOING = "eventGoing";

    private String eventTitle;
    private boolean eventGoing = false;

    protected ImageView image;
    protected ImageView upVoteButton;
    protected ImageView downVoteButton;
    protected TextView title;
    protected TextView author;
    protected TextView voteScore;

    @Inject public ImageCarouselPresenter presenter;

    private CompetitionFragment competitionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_carousel);
        //ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            eventTitle = getIntent().getExtras().getString(ARG_EVENT_TITLE);
            eventGoing = getIntent().getExtras().getBoolean(ARG_EVENT_GOING);
            setTitle(eventTitle);
        }

        Long competitionId = getIntent().getLongExtra(ARG_COMPETITION_ID, 0);
        competitionFragment = CompetitionFragment.newInstance(competitionId, eventGoing, true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_pane, competitionFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_carousel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.add:
                if (eventGoing) { competitionFragment.pickNewCompetitionImage(); }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCompetitionImageClick(CompetitionImage competitionImage) {
        // do nothing
    }

    @Override
    public void onCompetitionVoteButtonClicked(CompetitionImage competitionImage, int vote) {
        competitionFragment.onCompetitionImageVoteSubmitted(competitionImage, vote);
    }
}