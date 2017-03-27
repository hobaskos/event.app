package io.hobaskos.event.eventapp.ui.event.details.competition.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.CompetitionImage;

/**
 * Created by hans on 23/03/2017.
 */

public class CompetitionRecyclerViewAdapter extends
        RecyclerView.Adapter<CompetitionRecyclerViewAdapter.CompetitionViewHolder> {

    private static final String COMPETITION_IMAGE_URL_PLACEHOLDER = "https://mave.me/img/projects/full_placeholder.png";
    private final List<CompetitionImage> images;
    private final OnCompetitionListInteractionListener listener;
    private final Context context;
    private final boolean isLoggedIn;

    public CompetitionRecyclerViewAdapter(List<CompetitionImage> images, OnCompetitionListInteractionListener listener, Context context, boolean isLoggedIn) {
        this.images = images;
        this.listener = listener;
        this.context = context;
        this.isLoggedIn = isLoggedIn;
    }

    @Override
    public CompetitionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_competition, parent, false);
        return new CompetitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompetitionViewHolder holder, int position) {
        CompetitionImage currentImage = images.get(position);
        holder.id = currentImage.getId();
        Picasso.with(context)
                .load(currentImage.getImageUrl() != null ? currentImage.getAbsoluteImageUrl() : COMPETITION_IMAGE_URL_PLACEHOLDER)
                .into(holder.image);
        holder.numberOfVotes.setText(currentImage.getNumberOfVotes() + "");
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class CompetitionViewHolder extends RecyclerView.ViewHolder {

        public final ImageView image;
        public final TextView numberOfVotes;
        public final ImageView upVoteButton;
        public final ImageView downVoteButton;
        public Long id;

        public CompetitionViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            numberOfVotes = (TextView) view.findViewById(R.id.number_of_votes);
            upVoteButton = (ImageView) view.findViewById(R.id.up_vote);
            downVoteButton = (ImageView) view.findViewById(R.id.down_vote);

            // TODO: check if user is logged in
            image.setOnClickListener(v -> {
                if(null != listener) {
                    listener.onListFragmentInteraction(id);
                }
            });

            if(isLoggedIn) {

                upVoteButton.setOnClickListener(v -> {
                    if(null != listener) {
                        listener.submitCompetitionImageVote(id, +1);
                    }
                });

                downVoteButton.setOnClickListener(v -> {
                    if(null != listener) {
                        listener.submitCompetitionImageVote(id, -1);
                    }
                });

            } else {

                // TODO: user is not logged in. Make Vote-action redirect to Login/Register-splash.

            }


        }
    }
}
