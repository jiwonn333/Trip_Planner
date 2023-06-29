package com.example.letstravel.fragment.save;

import android.widget.ImageView;

public class SaveItem {
    String saveTitle;
    ImageView saveIcon;
    int count;

    public SaveItem(String saveTitle, ImageView saveIcon, int count) {
        this.saveTitle = saveTitle;
        this.saveIcon = saveIcon;
        this.count = count;
    }

    public String getSaveTitle() {
        return saveTitle;
    }

    public void setSaveTitle(String saveTitle) {
        this.saveTitle = saveTitle;
    }

    public ImageView getSaveIcon() {
        return saveIcon;
    }

    public void setSaveIcon(ImageView saveIcon) {
        this.saveIcon = saveIcon;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
