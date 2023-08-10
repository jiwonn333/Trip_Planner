package com.example.letstravel.fragment.common;

import android.graphics.Bitmap;

public class Info {
    private Bitmap bitmap;
    private String title;
    private String address;

    public Info(String title, String address) {
        this.title = title;
        this.address = address;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
