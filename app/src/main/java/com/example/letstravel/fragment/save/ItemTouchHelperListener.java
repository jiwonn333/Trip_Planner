package com.example.letstravel.fragment.save;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperListener {
    void onDeleteClick(int position, RecyclerView.ViewHolder viewHolder);

}