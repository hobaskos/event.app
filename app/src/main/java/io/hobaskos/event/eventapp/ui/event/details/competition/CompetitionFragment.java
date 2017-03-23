package io.hobaskos.event.eventapp.ui.event.details.competition;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.CompetitionImage;

/**
 * Created by hans on 23/03/2017.
 */

public class CompetitionFragment extends Fragment {

    private static final String COMPETITION_IMAGE_URL_PLACEHOLDER = "https://mave.me/img/projects/full_placeholder.png";
    private static final String ARG_COMPETION_IMAGES = "competition-images";
    private static final String ARG_IS_LOGGED_IN = "isLoggedIn";
    private final String TAG = "CompetitionFragment";

    private ArrayList<CompetitionImage> competitionItems;
    private OnListFragmentInteractionListener listener;
    private DividerItemDecoration dividerItemDecoration;
    private FloatingActionButton addCompetitionImage;
    private CompetitionRecyclerViewAdapter competitionRecyclerViewAdapter;
    private long eventId;
    private boolean isLoggedIn;

    public CompetitionFragment() {}

    public static CompetitionFragment newInstance(ArrayList<CompetitionImage> competitionImageList, boolean isLoggedIn) {
        CompetitionFragment fragment = new CompetitionFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_COMPETION_IMAGES, competitionImageList);
        args.putBoolean(ARG_IS_LOGGED_IN, isLoggedIn);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            competitionItems = getArguments().getParcelableArrayList(ARG_COMPETION_IMAGES);
            isLoggedIn = getArguments().getBoolean(ARG_IS_LOGGED_IN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competition_list, container, false);

        // set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        competitionRecyclerViewAdapter = new CompetitionRecyclerViewAdapter(competitionItems, listener, context, isLoggedIn);
        recyclerView.setAdapter(competitionRecyclerViewAdapter);

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        addCompetitionImage = (FloatingActionButton) view.findViewById(R.id.fragment_competition_list_fab);

        if(isLoggedIn) {
            addCompetitionImage.setOnClickListener(v -> showAddCompetitionImageDialog());
        } else {
            addCompetitionImage.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void showAddCompetitionImageDialog() {
        Log.i(TAG, "Add Competition FAB clicked!");
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Long id);
    }

}

