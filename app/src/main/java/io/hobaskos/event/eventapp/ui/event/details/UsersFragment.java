package io.hobaskos.event.eventapp.ui.event.details;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.User;

import java.util.ArrayList;

/**
 * A fragment representing a list of Users
 */
public class UsersFragment extends Fragment {

    public static final String TAG = UsersFragment.class.getName();

    private static final String ARG_USERS_LIST = "users-list";

    private OnUserListFragmentInteractionListener listener;
    private DividerItemDecoration dividerItemDecoration;

    private ArrayList<User> users;

    public UsersFragment() {}

    public static UsersFragment newInstance(ArrayList<User> users) {
        UsersFragment fragment = new UsersFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_USERS_LIST, users);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            users = getArguments().getParcelableArrayList(ARG_USERS_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        Log.d(TAG, "onCreateView: with users: " + users.size());

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(new UserRecyclerViewAdapter(users, listener));

            dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    linearLayoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserListFragmentInteractionListener) {
            listener = (OnUserListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnUserListFragmentInteractionListener {
        void onListFragmentInteraction(User item);
    }
}
