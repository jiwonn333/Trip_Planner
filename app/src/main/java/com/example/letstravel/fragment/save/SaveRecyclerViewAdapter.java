package com.example.letstravel.fragment.save;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letstravel.R;

import java.util.ArrayList;

public class SaveRecyclerViewAdapter extends RecyclerView.Adapter<SaveRecyclerViewAdapter.ViewHolder> {
    Context context;
    private ArrayList<RecyclerViewItem> itemLists;

    public SaveRecyclerViewAdapter(Context context, ArrayList<RecyclerViewItem> itemLists) {
        this.context = context;
        this.itemLists = itemLists;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItem(ArrayList<RecyclerViewItem> itemLists) {
        this.itemLists = itemLists;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void test(int iconDrawable, String title) {
        itemLists.add(new RecyclerViewItem(iconDrawable, title));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SaveRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.save_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaveRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.title.setText(itemLists.get(position).getTitle());
        holder.imageView.setImageResource(itemLists.get(position).getIconDrawable());
    }

    @Override
    public int getItemCount() {
        if (itemLists != null && !itemLists.isEmpty()) return itemLists.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_icon);
            title = itemView.findViewById(R.id.tv_save_title);
            count = itemView.findViewById(R.id.tv_save_count_size);

        }
    }
}
