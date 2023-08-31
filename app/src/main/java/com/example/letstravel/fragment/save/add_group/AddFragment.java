package com.example.letstravel.fragment.save.add_group;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letstravel.R;
import com.example.letstravel.databinding.FragmentSaveAddBinding;
import com.example.letstravel.fragment.common.BaseFragment;
import com.example.letstravel.fragment.common.Group;
import com.example.letstravel.fragment.common.SharedViewModel;
import com.example.letstravel.fragment.save.SelectedIcon;

import java.util.ArrayList;

public class AddFragment extends BaseFragment {

    private FragmentSaveAddBinding binding;
    private SharedViewModel sharedViewModel;
    private RecyclerView recyclerView;
    private AddRecyclerViewAdapter recyclerViewAdapter;
    private SelectedIcon selectedIcon;
    private int selectedIconPosition;

    private final ArrayList<RecyclerViewAddItem> itemLists = new ArrayList<RecyclerViewAddItem>() {{
        add(new RecyclerViewAddItem(R.drawable.ic_icon_star));
        add(new RecyclerViewAddItem(R.drawable.ic_icon_heart));
        add(new RecyclerViewAddItem(R.drawable.ic_icon_flash));
        add(new RecyclerViewAddItem(R.drawable.ic_icon_check));
        add(new RecyclerViewAddItem(R.drawable.ic_icon_eye));
        add(new RecyclerViewAddItem(R.drawable.ic_icon_smile));
        add(new RecyclerViewAddItem(R.drawable.ic_icon_flower));
        add(new RecyclerViewAddItem(R.drawable.ic_icon_square));
        add(new RecyclerViewAddItem(R.drawable.ic_icon_thumb_up));
        add(new RecyclerViewAddItem(R.drawable.ic_icon_diamond));
    }};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSaveAddBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        initObserve();

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

        binding.ibCancel.setOnClickListener(v -> removeFragment(AddFragment.this));

        binding.btnComplete.setOnClickListener(v -> {
            String title = binding.etAddGroupTitle.getText().toString();
            String description = binding.etAddDescription.getText().toString();
            Group group = new Group(title, description, itemLists.get(selectedIconPosition).getIconDrawable());

            Group group1 = new Group(title, description, itemLists.get(selectedIconPosition).getIconDrawable());
            Group group2 = new Group(title, description, itemLists.get(selectedIconPosition).getIconDrawable());
            Log.e("test", "Result : " + group1.equals(group2));

            if (title.isEmpty()) {
                Toast.makeText(getContext(), R.string.add_title_text_hint, Toast.LENGTH_SHORT).show();
            } else if (description.isEmpty()) {
                Toast.makeText(getContext(), R.string.add_desc_text_hint, Toast.LENGTH_SHORT).show();
            } else if (sharedViewModel.checkAlreadyExistGroup(group)) {
                Toast.makeText(getContext(), "이미 존재 하는 그룹명 입니다.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    sharedViewModel.addGroup(group);
                    removeFragment(AddFragment.this);
                } catch (Exception e) {
                    Log.e("test", "binding.btnComplete.setOnClickListener / e : " + e.getMessage());
                }
            }
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerView() {
        recyclerView = binding.saveAddRecyclerview;
        recyclerViewAdapter = new AddRecyclerViewAdapter(getContext(), itemLists);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
        recyclerView.setLayoutManager(gridLayoutManager);

        // 선택한 인증서 정보 받기
        recyclerViewAdapter.setOnItemClickListener((view, position) -> selectedIconPosition = position);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.addItem(itemLists);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void initObserve() {
        selectedIcon = new SelectedIcon(getContext());
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }


}