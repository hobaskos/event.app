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

    public static final String COMPETITION_IMAGE_URL_PLACEHOLDER = "https://mave.me/img/projects/full_placeholder.png";
    private final List<CompetitionImage> images;
    private final OnCompetitionListInteractionListener listener;
    private final Context context;
    private final boolean isAttending;

    public CompetitionRecyclerViewAdapter(List<CompetitionImage> images, OnCompetitionListInteractionListener listener, Context context, boolean isAttending) {
        this.images = images;
        this.listener = listener;
        this.context = context;
        this.isAttending = isAttending;
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
        holder.voteScore.setText(currentImage.getVoteScore() + "");
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class CompetitionViewHolder extends RecyclerView.ViewHolder {

        public final ImageView image;
        public final TextView voteScore;
        public final ImageView upVoteButton;
        public final ImageView downVoteButton;
        public Long id;

        public CompetitionViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            voteScore = (TextView) view.findViewById(R.id.number_of_votes);
            upVoteButton = (ImageView) view.findViewById(R.id.up_vote);
            downVoteButton = (ImageView) view.findViewById(R.id.down_vote);

            image.setOnClickListener(v -> {
                if(null != listener) {
                    listener.onCompetitionImageClick(id);
                }
            });

            if(isAttending) {

                upVoteButton.setOnClickListener(v -> {
                    if(null != listener) {
                        listener.onCompetitionVoteButtonClicked(id, +1);
                    }
                });

                downVoteButton.setOnClickListener(v -> {
                    if(null != listener) {
                        listener.onCompetitionVoteButtonClicked(id, -1);
                    }
                });

            } else {
                upVoteButton.setVisibility(View.GONE);
                downVoteButton.setVisibility(View.GONE);
            }


        }
    }
}
