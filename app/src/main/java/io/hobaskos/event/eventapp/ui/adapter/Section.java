package io.hobaskos.event.eventapp.ui.adapter;

import java.util.ArrayList;

/**
 * Created by test on 3/26/2017.
 */

public  class Section<M> {

    private String headerTitle;
    private ArrayList<M> items;


    public Section() {
        items = new ArrayList<M>();
    }

    public Section(String headerTitle) {
        this.headerTitle = headerTitle;
        items = new ArrayList<M>();
    }
    public Section(String headerTitle, ArrayList<M> items) {
        this.headerTitle = headerTitle;
        this.items = items;
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

}
