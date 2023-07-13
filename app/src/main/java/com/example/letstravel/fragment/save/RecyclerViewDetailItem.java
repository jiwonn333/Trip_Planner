package com.example.letstravel.fragment.save;

public class RecyclerViewDetailItem {
    private int iconDrawable;
    private String title;

    public RecyclerViewDetailItem(int iconDrawable, String title) {
        this.iconDrawable = iconDrawable;
        this.title = title;
    }

    public int getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(int iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
