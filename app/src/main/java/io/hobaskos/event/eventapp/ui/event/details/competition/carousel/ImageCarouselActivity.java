package io.hobaskos.event.eventapp.ui.event.details.competition.carousel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
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
    public static final String ARG_STARTING_COMPETION_IMAGE = "startingImage";
    public static final String ARG_EVENT_ID = "eventId";
    public static final String ARG_COMPETITION_IMAGES_LIST = "competitionImagesList";

    @Inject
    public ImageCarouselPresenter presenter;

    private ArrayList<CompetitionImage> competitionImages;
    private int currentItem = 0;
    private Long selectedItemId;
    private Long eventId;
    private boolean initialSettingOfData = true;

    @BindView(R.id.previous_image)
    protected ImageView previous;
    @BindView(R.id.next_image)
    protected ImageView next;
    @BindView(R.id.competition_image_view)
    protected ImageView image;
    @BindView(R.id.competition_plus_one)
    protected ImageView plusOne;
    @BindView(R.id.owner_login)
    protected TextView ownerLogin;
    @BindView(R.id.number_of_votes)
    protected TextView numberOfVotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_carousel);
        ButterKnife.bind(this);

        previous.setOnClickListener(v -> onPreviousButtonPressed());
        next.setOnClickListener(v -> onNextButtonPressed());
        plusOne.setOnClickListener(v -> onVoteButtonPressed());

        eventId = getIntent().getLongExtra(ARG_EVENT_ID, 0);
        selectedItemId = getIntent().getLongExtra(ARG_STARTING_COMPETION_IMAGE, 0);
        currentItem = -1;
        competitionImages = new ArrayList<>();

        presenter.attachView(this);
        presenter.get(eventId);
    }

    private int getIndexOfSelectedCompetitionImage(Long selectedItem) {
        for(int i = 0; i < competitionImages.size(); i++) {

            CompetitionImage competitionImage = competitionImages.get(i);

            if (competitionImage.getId() == selectedItem) {
                return i;
            }
        }

        return -1;
    }


    @NonNull
    @Override
    public ImageCarouselPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return presenter;
    }

    private void onPreviousButtonPressed() {
        if(currentItem == 0) {
            currentItem = competitionImages.size() - 1;
        } else {
            currentItem--;
        }

        Log.i(TAG, "onPreviousButtonPressed, moving to image number: " + currentItem);

        populateView();
    }

    private void onNextButtonPressed() {
        if(currentItem == competitionImages.size() - 1) {
            currentItem = 0;
        } else {
            currentItem++;
        }

        Log.i(TAG, "onNextButtonPressed, moving to image number: " + currentItem);

        populateView();
    }

    private void onVoteButtonPressed() {
        CompetitionImage currentImage = competitionImages.get(currentItem);
        if(currentImage.getHasMyVote()) {
            currentImage.setHasMyVote(false);
            showLikeLink();
            refreshVotesView();
            return;
        }

        presenter.vote(new Long(currentItem));
        currentImage.setHasMyVote(true);
        showDislikeLink();
        refreshVotesView();
    }

    private void refreshVotesView() {
        numberOfVotes.setText("Number of votes: " + competitionImages.get(currentItem).getNumberOfVotes());
    }

    private void showLikeLink(){
        plusOne.setImageResource(R.mipmap.ic_like);
    }

    private void showDislikeLink(){
        plusOne.setImageResource(R.mipmap.ic_dislike);
    }

    private void populateView() {
        if(currentItem >= 0) {
            Log.i(TAG, "Populating view for image number: " + currentItem);

            Log.i(TAG, competitionImages.get(currentItem).toString());

            CompetitionImage currentImage = competitionImages.get(currentItem);

            Picasso.with(this)
                    .load(currentImage.getImageUrl() != null ? currentImage.getAbsoluteImageUrl() : COMPETITION_IMAGE_URL_PLACEHOLDER)
                    .fit()
                    .centerCrop()
                    .into(image);

            if(currentImage.getHasMyVote()) {
                showDislikeLink();
            } else {
                showLikeLink();
            }

            ownerLogin.setText(competitionImages.get(currentItem).getOwnerLogin());
            numberOfVotes.setText("Number of votes: " + competitionImages.get(currentItem).getNumberOfVotes());
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
            currentItem = getIndexOfSelectedCompetitionImage(selectedItemId);

            if(competitionImages.size() == 1) {
                previous.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
            }

            initialSettingOfData = false;
        }

        populateView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}





