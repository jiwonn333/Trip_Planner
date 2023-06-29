package com.example.letstravel.fragment.save;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letstravel.R;

import java.util.ArrayList;

public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.ViewHolder> {
    private ArrayList<SaveItem> itemArrayList;

    @SuppressLint("NotifyDataSetChanged")
    public SaveAdapter(ArrayList<SaveItem> itemArrayList) {
        this.itemArrayList = itemArrayList;
        notifyDataSetChanged();
    }

    // 새 보기 만들기
    @NonNull
    @Override
    public SaveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.save_item, parent, false);

        return new ViewHolder(view);
    }


    // 보기의 내용 바꾸기
    @Override
    public void onBindViewHolder(@NonNull SaveAdapter.ViewHolder viewHolder, int position) {
        // 이 위치에 있는 localDataSet 에서 요소를 가져오고 뷰의 콘텐츠를 해당 요소로 바꿈

    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView saveTitle;
        private final TextView saveCountName;
        private final TextView saveCountSize;
        private final TextView saveCountMax;


        public ViewHolder(@NonNull View view) {
            super(view);

            imageView = view.findViewById(R.id.iv_icon);
            saveTitle = view.findViewById(R.id.tv_save_title);
            saveCountName = view.findViewById(R.id.tv_save_count_name);
            saveCountSize = view.findViewById(R.id.tv_save_count_size);
            saveCountMax = view.findViewById(R.id.tv_save_count_max);

        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getSaveTitle() {
            return saveTitle;
        }

        public TextView getSaveCountName() {
            return saveCountName;
        }

        public TextView getSaveCountSize() {
            return saveCountSize;
        }

        public TextView getSaveCountMax() {
            return saveCountMax;
        }
    }
}
