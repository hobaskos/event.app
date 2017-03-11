package io.hobaskos.event.eventapp.ui.event.details;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.ui.event.details.UsersFragment.OnUserListFragmentInteractionListener;

import java.util.List;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.UserViewHolder> {

    private final List<User> users;
    private final OnUserListFragmentInteractionListener listener;

    public UserRecyclerViewAdapter(List<User> users, OnUserListFragmentInteractionListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.item = user;
        holder.name.setText(user.getName());

        holder.view.setOnClickListener((view) -> {
            if (null != listener) {
                listener.onListFragmentInteraction(holder.item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public User item;

        public UserViewHolder(View view) {
            super(view);
            this.view = view;
            name = (TextView) view.findViewById(R.id.name);
        }
    }
}
