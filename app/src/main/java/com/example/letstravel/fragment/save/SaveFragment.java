package com.example.letstravel.fragment.save;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letstravel.R;
import com.example.letstravel.databinding.FragmentSaveBinding;
import com.example.letstravel.fragment.common.BaseFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class SaveFragment extends BaseFragment {
    private @NonNull FragmentSaveBinding binding;
    private SaveViewModel saveViewModel;
    private final ArrayList<RecyclerViewItem> itemLists = new ArrayList<>();
    private RecyclerView recyclerView;
    private SaveRecyclerViewAdapter recyclerViewAdapter;
    private int position;



    public static SaveFragment newInstance() {
        return new SaveFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSaveBinding.inflate(inflater, container, false);

        View bottomSheet = binding.coordinator.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior persistentBottomSheet = BottomSheetBehavior.from(bottomSheet);
        persistentBottomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == STATE_HIDDEN) {
                    removeFragment(SaveFragment.this);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        binding.btnAddGroup.setOnClickListener(v -> replaceFragment(SaveFragment.this, SaveFragmentDirections.actionNavigationSaveToNavigationSaveAdd()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        initObserve();

    }

    private void initObserve() {
        saveViewModel = new ViewModelProvider(requireActivity()).get(SaveViewModel.class);
        saveViewModel.getTitle().observe(getViewLifecycleOwner(), s -> {
            Log.e("test", "iconDrawable : " + saveViewModel.getDrawable().getValue());
            int iconDrawable = saveViewModel.getDrawable().getValue();
            recyclerViewAdapter.test(iconDrawable, s);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerView() {
        recyclerView = binding.saveRecyclerview;
        recyclerViewAdapter = new SaveRecyclerViewAdapter(getContext(), itemLists);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.addItem(itemLists);
        recyclerViewAdapter.notifyDataSetChanged();
    }

}