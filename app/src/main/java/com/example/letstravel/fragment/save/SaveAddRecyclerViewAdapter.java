package com.example.letstravel.fragment.save;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letstravel.R;

import java.util.ArrayList;


public class SaveAddRecyclerViewAdapter extends RecyclerView.Adapter<SaveAddRecyclerViewAdapter.ViewHolder> {
    Context context;
    private ArrayList<RecyclerViewItem> itemLists; // 어댑터에 들어갈 리스트

    private int selectedItemPosition = 0;

    @SuppressLint("NotifyDataSetChanged")
    public void addItem(ArrayList<RecyclerViewItem> itemLists) {
        this.itemLists = itemLists;
        notifyDataSetChanged();
    }

    //메인 액티비티와 연결
    public SaveAddRecyclerViewAdapter(Context context, ArrayList<RecyclerViewItem> itemLists) {
        super();
        this.context = context;
        this.itemLists = itemLists;
    }

    public SaveAddRecyclerViewAdapter(Context context) {
        this.context = context;
    }


    // 아이템 뷰를 위한 뷰 홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.icon_list, parent, false);
        return new ViewHolder(view);
    }

    // 생성된 뷰 홀더에 데이터 넣는 함수 (position에 해당하는 데이터를 뷰홀더 아이템뷰에 표시)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setData(itemLists.get(holder.getBindingAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        if (itemLists != null && !itemLists.isEmpty()) return itemLists.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);

        }

        public void setData(RecyclerViewItem item) {
            imageView.setImageResource(item.getIconDrawable());
            boolean isSelected = selectedItemPosition == getBindingAdapterPosition();
            int itemColor = isSelected ? R.color.icon_selected_color : R.color.icon_color;

            imageView.setColorFilter(context.getColor(itemColor), PorterDuff.Mode.SRC_IN);

            imageView.setOnClickListener(v -> {
                selectedItemPosition = getBindingAdapterPosition();
                notifyDataSetChanged();
            });
        }
    }
}