package com.example.letstravel.fragment.save;

public class RecyclerViewAddItem {
    private int iconDrawable;

    private int position;
    private String title;
    private int count;


    public RecyclerViewAddItem(int iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public void setIconDrawable(int iconDrawable) {
        this.iconDrawable = iconDrawable;
    }
    public int getIconDrawable() {
        return iconDrawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
