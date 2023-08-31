package com.example.letstravel.fragment.save;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letstravel.R;
import com.example.letstravel.fragment.common.Group;

import java.util.ArrayList;
import java.util.List;

public class SaveRecyclerViewAdapter extends RecyclerView.Adapter<SaveRecyclerViewAdapter.ViewHolder> implements ItemTouchHelperListener {
    Context context;
    private List<Group> itemLists = new ArrayList<>();
    private SaveRecyclerViewAdapter.OnItemClickListener itemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View view, String title);
    }

    public void setOnItemClickListener(SaveRecyclerViewAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public SaveRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Group> itemLists) {
        this.itemLists = itemLists;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItem(Group item) {
        itemLists.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<Group> groupList) {
        itemLists.addAll(groupList);
        notifyDataSetChanged();
    }

    @Override
    public void onDeleteClick(int position, RecyclerView.ViewHolder viewHolder) {
        if (position == 0) {
            Toast.makeText(context, "기본 그룹은 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
            itemLists.remove(position);
            notifyItemRemoved(position);
        }
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
        holder.ivIcon.setImageResource(itemLists.get(position).getDrawable());

        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(holder.itemView, holder.title.getText().toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (itemLists != null && !itemLists.isEmpty()) return itemLists.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView title;
        TextView count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_save_icon);
            title = itemView.findViewById(R.id.tv_save_title);
            count = itemView.findViewById(R.id.tv_save_count_size);

        }
    }
}
