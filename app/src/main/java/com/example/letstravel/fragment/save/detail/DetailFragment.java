package com.example.letstravel.fragment.save.detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letstravel.databinding.FragmentSaveDetailBinding;
import com.example.letstravel.fragment.common.BaseFragment;

import java.util.ArrayList;

public class DetailFragment extends BaseFragment {

    private DetailViewModel detailViewModel;
    private FragmentSaveDetailBinding binding;
    private final ArrayList<RecyclerViewDetailItem> itemLists = new ArrayList<>();
    private RecyclerView recyclerView;
    private DetailRecyclerViewAdapter recyclerViewAdapter;

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSaveDetailBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        getDetailTitle("title");

        binding.ibBack.setOnClickListener(v -> removeFragment(DetailFragment.this));
        binding.btnAddPlace.setOnClickListener(v -> replaceFragment(DetailFragment.this, DetailFragmentDirections.actionNavigationSaveDetailToNavigationAddPlace()));

    }

    private void initObserve() {
        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerView() {
        recyclerView = binding.saveDetailRecyclerview;
        recyclerViewAdapter = new DetailRecyclerViewAdapter(getContext(), itemLists);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setData(itemLists);
    }

    private void getDetailTitle(String key) {
        binding.tvText.setText(getArguments().getString(key));
    }
}