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

import javax.inject.Inject;

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
    public static final String ARG_COMPETITION_IMAGES_LIST = "competitionImagesList";

    @Inject
    public ImageCarouselPresenter presenter;

    private ArrayList<CompetitionImage> competitionImages;
    private int currentItem = 0;
    private ImageView previous;
    private ImageView next;
    private ImageView image;
    private ImageView plusOne;
    private TextView ownerLogin;
    private TextView numberOfVotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_carousel);

        previous = (ImageView) findViewById(R.id.previous_image);
        previous.setOnClickListener(v -> onPreviousButtonPressed());

        next = (ImageView) findViewById(R.id.next_image);
        next.setOnClickListener(v -> onNextButtonPressed());

        image = (ImageView) findViewById(R.id.competition_image_view);

        ownerLogin = (TextView) findViewById(R.id.owner_login);
        numberOfVotes = (TextView) findViewById(R.id.number_of_votes);

        plusOne = (ImageView) findViewById(R.id.competition_plus_one);
        plusOne.setOnClickListener(v -> onVoteButtonPressed());

        competitionImages = getIntent().getParcelableArrayListExtra(ARG_COMPETITION_IMAGES_LIST);
        Long selectedItem = getIntent().getLongExtra(ARG_STARTING_COMPETION_IMAGE, 0);
        currentItem = getIndexOfSelectedCompetitionImage(selectedItem);

        if(competitionImages.size() == 1) {
            previous.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        }

        populateView();

        presenter.attachView(this);
    }

    private int getIndexOfSelectedCompetitionImage(Long selectedItem) {
        for(int i = 0; i < competitionImages.size(); i++) {

            CompetitionImage competitionImage = competitionImages.get(i);

            if (competitionImage.getId() == selectedItem) {
                return i;
            }
        }

        return 0;
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

    @Override
    public void voteWasSuccessful() {
        Log.i(TAG, "vote was successful!");
    }

    @Override
    public void voteWasUnsuccessful() {
        Log.i(TAG, "vote was unsuccessful!");
    }

}





