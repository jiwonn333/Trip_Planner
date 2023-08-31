package com.example.letstravel.fragment.common;

import java.util.Objects;

public class Group {
    private String title;
    private String description;
    private int drawable;
    public Group(int drawable) {
        this.drawable = drawable;
    }

    public Group(String title, int drawable) {
        this.title = title;
        this.drawable = drawable;
    }

    public Group(String title, String description, int drawable) {
        this.title = title;
        this.description = description;
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
