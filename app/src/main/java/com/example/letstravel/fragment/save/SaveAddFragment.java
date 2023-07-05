package com.example.letstravel.fragment.save;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
        binding = FragmentSaveAddBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();

        binding.etAddGroupTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.etAddGroupTitle.length() > 0) {
                    binding.btnComplete.setClickable(true);
                    binding.btnComplete.setBackgroundResource(R.drawable.add_button_active_background);
                } else {
                    binding.btnComplete.setClickable(false);
                    binding.btnComplete.setBackgroundResource(R.drawable.add_button_background);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.saveAddRecyclerview;
        SaveAddRecyclerViewAdapter recyclerViewAdapter = new SaveAddRecyclerViewAdapter(getContext(), itemLists);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 5);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.addItem(itemLists);
        recyclerViewAdapter.notifyDataSetChanged();
    }


}