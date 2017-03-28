package io.hobaskos.event.eventapp.ui.event.details.competition.carousel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * Created by hans on 23/03/2017.
 */

public class ImageCarouselActivity extends MvpActivity<ImageCarouselView, ImageCarouselPresenter>
        implements ImageCarouselView {

    private static final String COMPETITION_IMAGE_URL_PLACEHOLDER = "https://mave.me/img/projects/full_placeholder.png";
    private final String TAG = "CompetitionFragment";
    public static final String ARG_STARTING_COMPETITION_IMAGE = "startingImage";
    public static final String ARG_EVENT_ID = "eventId";
    public static final String ARG_COMPETITION_ID = "competitionId";

    @Inject
    public ImageCarouselPresenter presenter;

    private ArrayList<CompetitionImage> competitionImages;
    private int currentItem = 0;
    private Long selectedItemId;
    private Long eventId;
    private Long competitionId;
    private int numberOfImages = 0;
    private boolean initialSettingOfData = true;

    @BindView(R.id.competition_image_view)
    protected ImageView image;
    @BindView(R.id.competition_up_vote)
    protected ImageView upVoteButton;
    @BindView(R.id.competition_down_vote)
    protected ImageView downVoteButton;
    @BindView(R.id.owner_login)
    protected TextView ownerLogin;
    @BindView(R.id.number_of_votes)
    protected TextView numberOfVotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_carousel);
        ButterKnife.bind(this);

        upVoteButton.setOnClickListener(v -> onUpVoteButtonPressed());
        downVoteButton.setOnClickListener(v -> onDownVoteButtonPressed());

        eventId = getIntent().getLongExtra(ARG_EVENT_ID, 0);
        competitionId = getIntent().getLongExtra(ARG_COMPETITION_ID, 0);
        selectedItemId = getIntent().getLongExtra(ARG_STARTING_COMPETITION_IMAGE, 0);
        currentItem = -1;
        competitionImages = new ArrayList<>();

        presenter.attachView(this);
        presenter.setCompetitionId(competitionId);
        presenter.get();

        image.setOnClickListener(v -> Log.i(TAG, "image clicked!"));
        image.setOnTouchListener(new OnSwipeTouchListener(ImageCarouselActivity.this) {
            @Override
            public boolean onSwipeLeft() {
                onPreviousButtonPressed();
                return true;
            }
            @Override
            public boolean onSwipeRight() {
                onNextButtonPressed();
                return true;
            }
        });
    }

    private int getIndexOfSelectedCompetitionImage(Long selectedItem) {
        if(numberOfImages == 1) {
            return 0;
        }

        for(int i = 0; i < numberOfImages; i++) {

            CompetitionImage competitionImage = competitionImages.get(i);

            if (competitionImage.getId().equals(selectedItem)) {
                return i;
            }
        }

        return -1;
    }

    @NonNull
    @Override
    public ImageCarouselPresenter createPresenter() {
        Log.i(TAG, "Creating presenter...");
        App.getInst().getComponent().inject(this);
        return presenter;
    }

    private void onPreviousButtonPressed() {
        if(numberOfImages < 2) {
            return;
        }

        if(currentItem == 0) {
            currentItem = numberOfImages - 1;
        } else {
            currentItem--;
        }

        Log.i(TAG, "onPreviousButtonPressed, moving to image number: " + currentItem);

        populateView();
    }

    private void onNextButtonPressed() {
        if(numberOfImages < 2) {
            return;
        }

        if(currentItem == numberOfImages - 1) {
            currentItem = 0;
        } else {
            currentItem++;
        }

        Log.i(TAG, "onNextButtonPressed, moving to image number: " + currentItem);

        populateView();
    }

    private void onUpVoteButtonPressed() {
        CompetitionImage currentImage = competitionImages.get(currentItem);
        currentImage.setHasMyVote(true);
        presenter.vote(currentImage.getId(), +1);
    }

    private void onDownVoteButtonPressed() {
        CompetitionImage currentImage = competitionImages.get(currentItem);
        currentImage.setHasMyVote(true);
        presenter.vote(currentImage.getId(), -1);
    }

    private void refreshVotesView() {
        numberOfVotes.setText("Number of votes: " + competitionImages.get(currentItem).getNumberOfVotes());
    }

    private void populateView() {
        if(numberOfImages > 0) {
            Log.i(TAG, "Populating view for image number: " + currentItem);

            Log.i(TAG, competitionImages.get(currentItem).toString());

            CompetitionImage currentImage = competitionImages.get(currentItem);

            Picasso.with(this)
                    .load(currentImage.getImageUrl() != null ? currentImage.getAbsoluteImageUrl() : COMPETITION_IMAGE_URL_PLACEHOLDER)
                    .fit()
                    .centerCrop()
                    .into(image);
        }
    }

    @Override
    public void voteWasSuccessful() {
        Log.i(TAG, "vote was successful!");
    }

    @Override
    public void voteWasUnsuccessful() {
        Log.i(TAG, "vote was unsuccessful!");
    }

    @Override
    public void setData(List<CompetitionImage> data) {
        this.competitionImages.clear();
        this.competitionImages.addAll(data);

        if(initialSettingOfData) {

            numberOfImages = data.size();

            currentItem = getIndexOfSelectedCompetitionImage(selectedItemId);

            initialSettingOfData = false;
        }

        populateView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {

        }

        return super.onTouchEvent(event);
    }
}





