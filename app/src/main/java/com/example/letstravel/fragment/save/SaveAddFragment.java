package com.example.letstravel.fragment.save;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letstravel.R;
import com.example.letstravel.databinding.FragmentSaveAddBinding;
import com.example.letstravel.fragment.common.BaseFragment;

import java.util.ArrayList;

public class SaveAddFragment extends BaseFragment {

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
                    binding.btnComplete.setBackgroundResource(R.drawable.background_add_btn_active);
                } else {
                    binding.btnComplete.setBackgroundResource(R.drawable.background_add_btn_default);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.ibCancel.setOnClickListener(v -> removeFragment(SaveAddFragment.this));
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