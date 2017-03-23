package io.hobaskos.event.eventapp.ui.event.details.competition;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
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

public class ImageCarouselActivity extends MvpActivity<CompetitionView, CompetitionPresenter>
        implements CompetitionView {

    private static final String COMPETITION_IMAGE_URL_PLACEHOLDER = "https://mave.me/img/projects/full_placeholder.png";
    private final String TAG = "CompetitionFragment";
    public static final String ARG_STARTING_COMPETION_IMAGE = "startingImage";
    public static final String ARG_COMPETITION_IMAGES_LIST = "competitionImagesList";

    @Inject
    public CompetitionPresenter presenter;

    private ArrayList<CompetitionImage> competitionImages;
    private int currentItem = 0;
    private Button previous;
    private Button next;
    private ImageView image;
    private ImageView plusOne;
    private TextView ownerLogin;
    private TextView numberOfVotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_carousel);

        previous = (Button) findViewById(R.id.previous_image);
        previous.setOnClickListener(v -> onPreviousButtonPressed());

        next = (Button) findViewById(R.id.next_image);
        next.setOnClickListener(v -> onNextButtonPressed());

        image = (ImageView) findViewById(R.id.competition_image_view);

        ownerLogin = (TextView) findViewById(R.id.owner_login);
        numberOfVotes = (TextView) findViewById(R.id.number_of_votes);

        plusOne = (ImageView) findViewById(R.id.competition_plus_one);
        plusOne.setOnClickListener(v -> onVoteButtonPressed());

        competitionImages = getIntent().getParcelableArrayListExtra(ARG_COMPETITION_IMAGES_LIST);
        Long selectedItem = getIntent().getLongExtra(ARG_STARTING_COMPETION_IMAGE, 0);
        currentItem = getIndexOfSelectedCompetitionImage(selectedItem);

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
    public CompetitionPresenter createPresenter() {
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
            showLikePicture();
            refreshVotesView();
            return;
        }

        currentImage.setHasMyVote(true);
        showDislikePicture();
        refreshVotesView();
    }

    private void refreshVotesView() {
        numberOfVotes.setText("Number of votes: " + competitionImages.get(currentItem).getHearts());
    }

    private void showLikePicture(){
        plusOne.setImageResource(R.mipmap.ic_up);
    }

    private void showDislikePicture(){
        plusOne.setImageResource(R.mipmap.ic_down);
    }

    private void populateView() {
        Log.i(TAG, "Populating view for image number: " + currentItem);
        Log.i(TAG, competitionImages.get(currentItem).toString());

        CompetitionImage currentImage = competitionImages.get(currentItem);

        Picasso.with(this)
                .load(currentImage.getImageUrl() != null ? currentImage.getAbsoluteImageUrl() : COMPETITION_IMAGE_URL_PLACEHOLDER)
                .into(image);

        if(currentImage.getHasMyVote()) {
            showDislikePicture();
        } else {
            showLikePicture();
        }

        ownerLogin.setText(competitionImages.get(currentItem).getOwnerLogin());
        numberOfVotes.setText("Number of votes: " + competitionImages.get(currentItem).getHearts());

    }
}





