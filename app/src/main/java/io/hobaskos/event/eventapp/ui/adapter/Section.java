package io.hobaskos.event.eventapp.ui.adapter;

import java.util.ArrayList;

/**
 * Created by test on 3/26/2017.
 */

public  class Section<M> {

    private String headerTitle;
    private ArrayList<M> items;
    private int relativePosition;


    public Section() {
        items = new ArrayList<M>();
    }

    public Section(String headerTitle) {
        this.headerTitle = headerTitle;
        items = new ArrayList<M>();
    }

    public Section(String headerTitle, int relativePosition) {
        this.headerTitle = headerTitle;
        this.relativePosition = relativePosition;
        items = new ArrayList<M>();
    }
    public Section(String headerTitle, ArrayList<M> items, int relativePosition) {
        this.headerTitle = headerTitle;
        this.items = items;
        this.relativePosition = relativePosition;
    }

    public int getRelativePosition() {
        return relativePosition;
    }

    public void setRelativePosition(int relativePosition) {
        this.relativePosition = relativePosition;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<M> getItems() {
        return items;
    }

    public M getItemAtPosition(int position) {
        M item;
        return items.get(position);
    }

    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<M> items) {
        this.items = items;
    }

    public void addItems(ArrayList<M> items) {
        this.items.addAll(items);
    }

    public void addItem(M item) {
        this.items.add(item);
    }

    public boolean isEmpty() {
        if (items == null || items.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

}
