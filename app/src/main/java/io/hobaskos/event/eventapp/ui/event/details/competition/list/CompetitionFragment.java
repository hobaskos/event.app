package io.hobaskos.event.eventapp.ui.event.details.competition.list;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.State;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.CompetitionImage;
import io.hobaskos.event.eventapp.util.ImageUtil;
import io.hobaskos.event.eventapp.util.SavingProgress;

import static android.app.Activity.RESULT_OK;
import static io.hobaskos.event.eventapp.util.ImageUtil.CAPTURE_IMAGE_REQUEST;
import static io.hobaskos.event.eventapp.util.ImageUtil.PICK_IMAGE_REQUEST;

/**
 * Created by hans on 23/03/2017.
 */

public class CompetitionFragment extends MvpFragment<CompetitionView, CompetitionPresenter>
        implements CompetitionView {

    public static final String TAG = CompetitionFragment.class.getName();
    public static final String ARG_COMPETITION_ID = "competitionId";
    public static final String ARG_IS_ATTENDING_EVENT = "myAttendance";
    public static final String ARG_HORIZONTAL = "horizontal";

    private static final String[] CAMERA_PERMS = { Manifest.permission.CAMERA };
    private static final int CAMERA_REQUEST = 4337;

    private ArrayList<CompetitionImage> competitionImages;
    private OnCompetitionListInteractionListener listener;
    private CompetitionRecyclerViewAdapter competitionRecyclerViewAdapter;

    private Long competitionId;
    private boolean isAttending;
    private boolean horizontal = false;
    private SavingProgress savingProgress;

    @BindView(R.id.contentView)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_competition_list_fab)
    protected FloatingActionButton addCompetitionImage;
    @BindView(R.id.list)
    protected RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    protected TextView emptyResultView;

    @State
    protected boolean canLoadMore = true;
    @State
    protected boolean isLoadingMore = false;

    @Inject
    public CompetitionPresenter competitionPresenter;

    @NonNull
    @Override
    public CompetitionPresenter createPresenter() {
        Log.i(TAG, "createPresenter");
        App.getInst().getComponent().inject(this);
        return competitionPresenter;
    }

    public static CompetitionFragment newInstance(Long eventCompetitionId, boolean isAttending, boolean horizontal) {
        CompetitionFragment fragment = new CompetitionFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_ATTENDING_EVENT, isAttending);
        args.putLong(ARG_COMPETITION_ID, eventCompetitionId);
        args.putBoolean(ARG_HORIZONTAL, horizontal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        competitionImages = new ArrayList<>();

        if (getArguments() != null) {
            competitionId = getArguments().getLong(ARG_COMPETITION_ID);
            isAttending = getArguments().getBoolean(ARG_IS_ATTENDING_EVENT);
            horizontal = getArguments().getBoolean(ARG_HORIZONTAL);
        }

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competition_list, container, false);

        ButterKnife.bind(this, view);

        // set the adapter
        Context context = view.getContext();
        LinearLayoutManager linearLayoutManager;
        if (horizontal) {
            linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        } else {
            linearLayoutManager = new LinearLayoutManager(context);
        }
        recyclerView.setLayoutManager(linearLayoutManager);
        competitionRecyclerViewAdapter = new CompetitionRecyclerViewAdapter(competitionImages, listener, context, isAttending, horizontal);
        recyclerView.setAdapter(competitionRecyclerViewAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> loadData(true));

        if (!horizontal) {
            addCompetitionImage.setOnClickListener(v -> pickNewCompetitionImage());
        } else {
            addCompetitionImage.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(competitionId != null) {
            competitionPresenter.setCompetitionId(competitionId);
            competitionPresenter.get();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnCompetitionListInteractionListener) {
            listener = (OnCompetitionListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCompetitionListInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void showLoading(boolean pullToRefresh) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    public void setData(List<CompetitionImage> data) {

        competitionImages.clear();
        competitionImages.addAll(data);

        Collections.sort(competitionImages, (o1, o2) -> o2.getVoteScore().compareTo(o1.getVoteScore()));

        competitionRecyclerViewAdapter =
                new CompetitionRecyclerViewAdapter(competitionImages, listener, getContext(), isAttending, horizontal);

        recyclerView.setAdapter(competitionRecyclerViewAdapter);

        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        competitionPresenter.get();
        canLoadMore = false;
    }

    @Override
    public void showLoadMore(boolean showLoadMore) {

    }

    @Override
    public void showLoadMoreError(Throwable e) {

    }

    @Override
    public void addMoreData(List<CompetitionImage> model) {
        this.competitionImages.addAll(model);
        competitionRecyclerViewAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        competitionPresenter.get();
    }

    public void onCompetitionImageVoteSubmitted(CompetitionImage competitionImage, int vote) {
        if (!competitionImage.hasMyVote()) {
            competitionPresenter.vote(competitionImage.getId(), vote);
            updateCompetitionImageVoteScore(competitionImage, vote);
        } else {
            Toast.makeText(getContext(), R.string.already_voted, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCompetitionImageVoteScore(CompetitionImage competitionImage, int vote) {
        Log.i(TAG, "Inside updateCompetitionImageVoteScore()");
        int index = competitionImages.indexOf(competitionImage);
        Log.i(TAG, "Image has index = " + index);
        competitionImage.setHasMyVote(vote);
        int numberOfVotes = competitionImage.getNumberOfVotes();
        Long voteScore = competitionImage.getVoteScore();
        Log.i(TAG, "Image has now " + numberOfVotes + " votes");
        Log.i(TAG, "Image has now " + voteScore + " voteScore");
        competitionImage.setNumberOfVotes(numberOfVotes + 1);
        competitionImage.setVoteScore(voteScore + vote);
        competitionImages.set(index, competitionImage);
        Collections.sort(competitionImages, (o1, o2) -> o2.getVoteScore().compareTo(o1.getVoteScore()));
        competitionRecyclerViewAdapter.notifyDataSetChanged();
        competitionRecyclerViewAdapter.notifyItemChanged(index);
        index = competitionImages.indexOf(competitionImage);
        recyclerView.scrollToPosition(index);
    }

    @Override
    public void imageWasUnsuccessfullyNominated(Throwable throwable) {
        savingProgress.dismiss();
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void imageWasSuccessfullyNominated(CompetitionImage competitionImage) {
        savingProgress.dismiss();
        competitionImages.add(competitionImage);
        competitionRecyclerViewAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(competitionImages.size() - 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = null;

        if (resultCode != RESULT_OK) return;

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            } catch (IOException e) { e.printStackTrace(); }
        } else if (requestCode == CAPTURE_IMAGE_REQUEST) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
        }

        final Bitmap bMap = bitmap;

        UploadImageFragment.newInstance()
                .setImage(bitmap)
                .onConfirmAction((string) -> uploadImage(string, bMap))
                .show(getFragmentManager(), "imageUpload");
    }

    private void uploadImage(String title, Bitmap bitmap) {
        if (bitmap == null) return;

        String image = ImageUtil.getEncoded64ImageStringFromBitmap(bitmap);
        savingProgress = SavingProgress.createAndShow(getContext());
        presenter.nominateImage(title, image);
    }

    public void setAttendingEvent(boolean isAttending) {
        this.isAttending = isAttending;

        if (isAttending) {
            addCompetitionImage.setVisibility(View.VISIBLE);
        } else {
            addCompetitionImage.setVisibility(View.GONE);
        }

        competitionPresenter.get();
    }

    public void pickNewCompetitionImage() {
        if (!isAttending) {
            listener.onRequestForAttendingEvent((bool) -> {
                if (bool) {
                    isAttending = true;
                    pickNewCompetitionImage();
                }
            });
        } else {
            CharSequence options[] = new CharSequence[]{
                    getString(R.string.capture_image),
                    getString(R.string.select_image_from_lib)};

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.select_image_option))
                    .setItems(options, (dialog, which) -> {
                        switch (which) {
                            case 0: launchCamera(); break;
                            case 1: launchLibrary(); break;
                        }
                    })
                    .setNegativeButton(R.string.close, (dialog, which) -> dialog.dismiss())
                    .create().show();
        }
    }

    private void launchCamera() {
        if (!getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(getContext(), R.string.could_not_find_camera, Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), CAMERA_PERMS, CAMERA_REQUEST);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
        }
    }

    private void launchLibrary(){
        Intent intent = new Intent();
        // Only show images
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser
        startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}

