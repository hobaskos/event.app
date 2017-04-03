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

    public static final String COMPETITION_IMAGE_URL_PLACEHOLDER = "https://mave.me/img/projects/full_placeholder.png";
    public static final String ARG_STARTING_COMPETITION_IMAGE = "startingImage";
    public static final String ARG_COMPETITION_ID = "competitionId";

    @Inject
    public ImageCarouselPresenter presenter;

    private ArrayList<CompetitionImage> competitionImages;
    private int currentItem = 0;
    private Long selectedItemId;
    private int numberOfImages = 0;
    private boolean initialSettingOfData = true;

    @BindView(R.id.competition_image_view)
    protected ImageView image;
    @BindView(R.id.competition_up_vote)
    protected ImageView upVoteButton;
    @BindView(R.id.competition_down_vote)
    protected ImageView downVoteButton;
    @BindView(R.id.voteScore)
    protected TextView voteScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_carousel);
        ButterKnife.bind(this);

        upVoteButton.setOnClickListener(v -> onUpVoteButtonPressed());
        downVoteButton.setOnClickListener(v -> onDownVoteButtonPressed());

        Long competitionId = getIntent().getLongExtra(ARG_COMPETITION_ID, 0);
        selectedItemId = getIntent().getLongExtra(ARG_STARTING_COMPETITION_IMAGE, 0);
        currentItem = -1;
        competitionImages = new ArrayList<>();

        presenter.attachView(this);
        presenter.setCompetitionId(competitionId);
        presenter.get();

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

    private void populateView() {
        if(numberOfImages > 0) {

            CompetitionImage currentImage = competitionImages.get(currentItem);

            Picasso.with(this)
                    .load(currentImage.getImageUrl() != null
                            ? currentImage.getAbsoluteImageUrl()
                            : COMPETITION_IMAGE_URL_PLACEHOLDER)
                    .fit()
                    .centerCrop()
                    .into(image);

            voteScore.setText(currentImage.getVoteScoreAsReadable());
        }
    }



    @Override
    public void voteWasSuccessful() {

    }

    @Override
    public void voteWasUnsuccessful() {

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

}