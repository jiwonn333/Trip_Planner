package com.example.letstravel.fragment.save;

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

public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder> {
    Context context;
    private ArrayList<RecyclerViewDetailItem> itemLists;

    public DetailRecyclerViewAdapter(Context context, ArrayList<RecyclerViewDetailItem> itemLists) {
        this.context = context;
        this.itemLists = itemLists;
    }

    public void addItem(int iconDrawable, String title) {
        itemLists.add(new RecyclerViewDetailItem(iconDrawable, title));
        notifyDataSetChanged();
    }

    public void setData(ArrayList<RecyclerViewDetailItem> itemLists) {
        this.itemLists = itemLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetailRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.save_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.tvPlace.setText(itemLists.get(position).getTitle());
        holder.imageView.setImageResource(itemLists.get(position).getIconDrawable());
    }

    @Override
    public int getItemCount() {
        if (itemLists != null && !itemLists.isEmpty()) return itemLists.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvPlace;
        TextView tvAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_group_icon);
            tvPlace = itemView.findViewById(R.id.tv_place);
            tvAddress = itemView.findViewById(R.id.tv_address);
        }
    }
}
