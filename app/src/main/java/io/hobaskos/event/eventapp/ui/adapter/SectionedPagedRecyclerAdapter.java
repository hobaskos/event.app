package io.hobaskos.event.eventapp.ui.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;

import java.util.ArrayList;
import java.util.List;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import rx.functions.Action1;

/**
 * Created by test on 3/25/2017.
 */

public abstract class SectionedPagedRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static String TAG = PagedRecyclerAdapter.class.getName();


    protected final int VIEW_TYPE_ITEM = 0;
    protected final int VIEW_TYPE_LOADING = 1;
    protected final int VIEW_TYPE_HEADER = -2;

    private final ArrayMap<Integer, Integer> headerLocationMap;
    private ArrayList<Section<T>> sectionList;

    protected List<T> items;
    protected Context context;
    protected final Action1<T> onItemClick;
    //private final Action1<Integer> onListBottom;

    private boolean showLoadMore = false;

    public SectionedPagedRecyclerAdapter(Context context, Action1<T> onItemClick) {
        headerLocationMap = new ArrayMap<>();
        this.context = context;
        this.onItemClick = onItemClick;
    }

    public final boolean isHeader(int position) {
        return headerLocationMap.get(position) != null;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public void addItems(List<T> items) {
        int startPosition = items.size();
        this.items.addAll(items);
        notifyItemRangeInserted(startPosition, items.size());
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

    protected String getSectionTitleToPosition(int position) 

    @Override public int getItemViewType(int position) {

        if (showLoadMore && position == items.size()) { // At last position add load more item
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + (showLoadMore ? 1 : 0);
    }


    public T getItemAtPosition(int position) {

        int count = 0;
        int relativePosition = position;

        for (Section<T> section : sectionList) {
            count += section.getItemCount();
            relativePosition -= 1; // header offset
            if (count >= position) {
                // Item resides in current section
                return section.getItemAtPosition(relativePosition);
            } else {
                // Item does not reside in current section
                //Update relative position / subtract values in current section:
                relativePosition -= section.getItemCount();
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