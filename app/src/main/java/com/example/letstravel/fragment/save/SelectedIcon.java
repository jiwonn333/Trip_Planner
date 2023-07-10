package com.example.letstravel.fragment.save;

import android.content.Context;

import com.example.letstravel.R;

public class SelectedIcon {
    private Context context;

    public SelectedIcon(Context context) {
        this.context = context;
    }

    public int getIconDrawable(int position) {
        switch (position) {
            case 0:
                return R.drawable.ic_icon_star;
            case 1:
                return R.drawable.ic_icon_heart;
            case 2:
                return R.drawable.ic_icon_flash;
            case 3:
                return R.drawable.ic_icon_check;
            case 4:
                return R.drawable.ic_icon_eye;
            case 5:
                return R.drawable.ic_icon_smile;
            case 6:
                return R.drawable.ic_icon_flower;
            case 7:
                return R.drawable.ic_icon_square;
            case 8:
                return R.drawable.ic_icon_thumb_up;
            case 9:
                return R.drawable.ic_icon_diamond;
        }
        return position;
    }
}
