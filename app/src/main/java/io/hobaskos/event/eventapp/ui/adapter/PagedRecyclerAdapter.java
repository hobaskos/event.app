package io.hobaskos.event.eventapp.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.EventCategoryTheme;
import rx.functions.Action1;

/**
 * Created by test on 3/17/2017.
 */

public abstract class PagedRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static String TAG = PagedRecyclerAdapter.class.getName();

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    protected List<T> items;
    protected Context context;
    protected final Action1<T> onItemClick;
    //private final Action1<Integer> onListBottom;

    private boolean showLoadMore = false;

    public PagedRecyclerAdapter(List<T> items, Context context, Action1<T> onItemClick) {
        this.items = items;
        this.context = context;
        this.onItemClick = onItemClick;
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

    protected abstract ItemViewHolder newItemViewHolder(View view);
}