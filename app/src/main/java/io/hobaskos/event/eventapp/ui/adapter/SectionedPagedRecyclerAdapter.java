package io.hobaskos.event.eventapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.hobaskos.event.eventapp.R;
import rx.functions.Action1;

/**
 * Created by test on 3/25/2017.
 */

public abstract class SectionedPagedRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static String TAG = SectionedPagedRecyclerAdapter.class.getName();


    protected final int VIEW_TYPE_ITEM = 0;
    protected final int VIEW_TYPE_LOADING = 1;
    protected final int VIEW_TYPE_HEADER = -2;

    protected ArrayList<Section<T>> activeSectionsList;

    protected List<T> items;
    protected Context context;
    protected final Action1<T> onItemClick;
    //private final Action1<Integer> onListBottom;

    protected boolean showLoadMore = false;

    protected int totalPositions;

    public SectionedPagedRecyclerAdapter(Context context, Action1<T> onItemClick) {
        this.context = context;
        this.onItemClick = onItemClick;
        totalPositions = 0;
        activeSectionsList = new ArrayList<>();
    }

    public boolean isEmpty() {
        for (Section section : activeSectionsList) {
            Log.d(TAG, "isEmpty() for loop");
            if (!section.isEmpty()) {
                Log.d(TAG, "isEmpty() false");
                return false; // adapter is not empty
            }
        }
        Log.d(TAG, "isEmpty() true");
        return true; // adapter is empty
    }

    protected boolean isHeader(int position) {
        for (Section section : activeSectionsList) {
            if (section.getRelativePosition() == position) {
                return true;
            }
        }
        return false;
    }

    protected String getHeaderTitle(int position) {
        for (Section section : activeSectionsList) {
            if (section.getRelativePosition() == position) {
                return section.getHeaderTitle();
            }
        }
        return null;
    }

    public void setLoadMore(boolean enabled) {

        if (showLoadMore != enabled) {
            if (showLoadMore) {
                showLoadMore = false;
                notifyDataSetChanged();
            } else {
                showLoadMore = true;
                notifyDataSetChanged();
            }
        }
    }

    @Override public int getItemViewType(int position) {

        if (showLoadMore && position == totalPositions) { // At last position add load more item
            return VIEW_TYPE_LOADING;
        } else if (isHeader(position)){
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        //return items.size() + (showLoadMore ? 1 : 0);
        return totalPositions + (showLoadMore ? 1 : 0);
    }


    public T getItemAtPosition(int position) {
        Log.d(TAG, "getItemAtPosition() totalPositions: " + totalPositions);
        Log.d(TAG, "getItemAtPosition() position: " + position);
        int count = 0;
        int relativePosition = position;

        for (Section<T> section : activeSectionsList) {
            count += section.getItemCount();
            if (count >= position) {
                // Item resides in current section
                return section.getItemAtPosition(position);
            } else {
                // Item does not reside in current section
                //Update relative position / subtract values in current section:
                relativePosition -= (section.getItemCount() +1);
            }
        }

        return items.get(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, null);
                view.setLayoutParams(new RecyclerView.
                        LayoutParams(RecyclerView.LayoutParams.
                        MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                return newItemViewHolder(view);
            case VIEW_TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_section, null);
                view.setLayoutParams(new RecyclerView.
                        LayoutParams(RecyclerView.LayoutParams.
                        MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                return new SectionViewHolder(view);

            case VIEW_TYPE_LOADING: // viewType = 1 (show load more item)
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loadmore, null);
                view.setLayoutParams(new RecyclerView.
                        LayoutParams(RecyclerView.LayoutParams.
                        MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                return new LoadMoreViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract class ItemViewHolder extends RecyclerView.ViewHolder {


        public ItemViewHolder(View itemView) {
            super(itemView);
        }

        public void click(final T item, Action1<T> listener) {
            itemView.setOnClickListener((i) -> listener.call(item));
        }

    }

    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        public LoadMoreViewHolder(View itemView) {
            super(itemView);
        }
    }

    // SectionViewHolder Class for Sections
    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        final TextView sectionTitle;

        public SectionViewHolder(View itemView) {
            super(itemView);

            sectionTitle = (TextView) itemView.findViewById(R.id.sectionTitle);
        }
    }

    protected abstract SectionedPagedRecyclerAdapter.ItemViewHolder newItemViewHolder(View view);
}