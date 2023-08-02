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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letstravel.R;
import com.example.letstravel.databinding.FragmentSaveBinding;
import com.example.letstravel.fragment.common.BaseFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class SaveFragment extends BaseFragment {
    private FragmentSaveBinding binding;
    private SaveViewModel saveViewModel;
    private RecyclerView recyclerView;
    private SaveRecyclerViewAdapter recyclerViewAdapter;
    private ItemTouchHelper helper;


    private final ArrayList<RecyclerViewSaveItem> itemLists = new ArrayList<RecyclerViewSaveItem>() {{
        add(new RecyclerViewSaveItem(R.drawable.ic_icon_heart, "기본 그룹"));
    }};


    public static SaveFragment newInstance() {
        return new SaveFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.e("test", "onViewCreated 실행");
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        setHelper();
        initObserve();

        binding.btnAddGroup.setOnClickListener(v -> replaceFragment(SaveFragment.this, SaveFragmentDirections.actionNavigationSaveToNavigationSaveAdd()));
        recyclerViewAdapter.setOnItemClickListener((v, title) -> {
            setDetailTitle("title", title);
        });
    }

    private void initObserve() {
        saveViewModel = new ViewModelProvider(requireActivity()).get(SaveViewModel.class);
        saveViewModel.getTitle().observe(getViewLifecycleOwner(), title -> {
            int iconDrawable = saveViewModel.getDrawable().getValue();
            recyclerViewAdapter.addItem(iconDrawable, title);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerView() {
        recyclerView = binding.saveRecyclerview;
        recyclerViewAdapter = new SaveRecyclerViewAdapter(getContext(), itemLists);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setData(itemLists);
    }

    private void setHelper() {
        // ItemTouchHelper 생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(recyclerViewAdapter));
        // RecyclerView에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(recyclerView);
    }

    public void setDetailTitle(String key, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(key, title);
        dataSendFragment(SaveFragment.this, R.id.action_navigation_save_to_navigation_save_detail, bundle);
    }
}