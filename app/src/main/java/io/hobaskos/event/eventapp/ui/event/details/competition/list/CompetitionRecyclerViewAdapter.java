package io.hobaskos.event.eventapp.ui.event.details.competition.list;

import android.content.Context;
import android.graphics.PorterDuff;
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
    private final boolean horizontal;

    public CompetitionRecyclerViewAdapter(List<CompetitionImage> images,
                                          OnCompetitionListInteractionListener listener,
                                          Context context,
                                          boolean isAttending,
                                          boolean horizontal) {
        this.images = images;
        this.listener = listener;
        this.context = context;
        this.isAttending = isAttending;
        this.horizontal = horizontal;
    }

    @Override
    public CompetitionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CompetitionViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(horizontal ? R.layout.list_item_competition_image_horizontal : R.layout.list_item_competition_image, parent, false));
    }

    @Override
    public void onBindViewHolder(CompetitionViewHolder holder, int position) {
        CompetitionImage currentImage = images.get(position);
        holder.competitionImage = currentImage;
        holder.title.setText(currentImage.getTitle());
        holder.author.setText(currentImage.getAuthor());
        Picasso.with(context)
                .load(currentImage.getImageUrl() != null ? currentImage.getAbsoluteImageUrl() : COMPETITION_IMAGE_URL_PLACEHOLDER)
                .into(holder.image);
        holder.voteScore.setText(currentImage.getVoteScore() + "");

        if (currentImage.hasMyVote()) {
            holder.upVoteButton.setColorFilter(R.color.light_gray, PorterDuff.Mode.SRC_IN);
            holder.downVoteButton.setColorFilter(R.color.light_gray, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class CompetitionViewHolder extends RecyclerView.ViewHolder {

        public final ImageView image;
        public final TextView voteScore;
        public final TextView title;
        public final TextView author;
        public final ImageView upVoteButton;
        public final ImageView downVoteButton;
        public CompetitionImage competitionImage;

        public CompetitionViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            author = (TextView) view.findViewById(R.id.author);
            image = (ImageView) view.findViewById(R.id.image);
            voteScore = (TextView) view.findViewById(R.id.number_of_votes);
            upVoteButton = (ImageView) view.findViewById(R.id.up_vote);
            downVoteButton = (ImageView) view.findViewById(R.id.down_vote);

            if (listener == null) return;

            image.setOnClickListener(v -> listener.onCompetitionImageClick(competitionImage));

            if (isAttending) {
                upVoteButton.setOnClickListener(v -> listener.onCompetitionVoteButtonClicked(competitionImage, +1));
                downVoteButton.setOnClickListener(v -> listener.onCompetitionVoteButtonClicked(competitionImage, -1));
            } else {
                upVoteButton.setVisibility(View.GONE);
                downVoteButton.setVisibility(View.GONE);
            }
        }
    }
}
