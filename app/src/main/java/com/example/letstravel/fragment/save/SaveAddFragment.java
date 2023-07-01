package com.example.letstravel.fragment.save;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letstravel.R;
import com.example.letstravel.databinding.FragmentSaveAddBinding;

import java.util.ArrayList;

public class SaveAddFragment extends Fragment {

    private FragmentSaveAddBinding binding;
    SaveViewModel saveViewModel;

    public SaveAddFragment() {
    }

    public static SaveAddFragment newInstance(int res) {

        return new SaveAddFragment();
    }

    private final ArrayList<RecyclerViewItem> itemLists = new ArrayList<RecyclerViewItem>() {{
        add(new RecyclerViewItem(R.drawable.ic_icon_star));
        add(new RecyclerViewItem(R.drawable.ic_icon_heart));
        add(new RecyclerViewItem(R.drawable.ic_icon_flash));
        add(new RecyclerViewItem(R.drawable.ic_icon_check));
        add(new RecyclerViewItem(R.drawable.ic_icon_eye));
        add(new RecyclerViewItem(R.drawable.ic_icon_smile));
        add(new RecyclerViewItem(R.drawable.ic_icon_flower));
        add(new RecyclerViewItem(R.drawable.ic_icon_square));
        add(new RecyclerViewItem(R.drawable.ic_icon_thumb_up));
        add(new RecyclerViewItem(R.drawable.ic_icon_diamond));
    }};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSaveAddBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.saveAddRecyclerview;
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(), itemLists);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 5);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.addItem(itemLists);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}