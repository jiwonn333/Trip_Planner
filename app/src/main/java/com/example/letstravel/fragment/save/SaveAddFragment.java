package com.example.letstravel.fragment.save;

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

import java.util.ArrayList;

public class SaveAddFragment extends BaseFragment {

    private FragmentSaveAddBinding binding;
    SaveViewModel saveViewModel;
    private RecyclerView recyclerView;
    private SaveAddRecyclerViewAdapter recyclerViewAdapter;
    private SelectedIcon selectedIcon;
    private int selectedIconPosition;

    public SaveAddFragment() {
    }


    public static SaveAddFragment newInstance(int res) {

        return new SaveAddFragment();
    }

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

        binding.btnComplete.setOnClickListener(v -> {
            String title = binding.etAddGroupTitle.getText().toString();
            if (title.isEmpty()) {
                Toast.makeText(getContext(), "그룹명을 입력해 주세요", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    saveViewModel = new ViewModelProvider(requireActivity()).get(SaveViewModel.class);
                    saveViewModel.setTitle(title);
                    selectedIcon = new SelectedIcon(getContext());
                    saveViewModel.setDrawable(selectedIcon.getIconDrawable(selectedIconPosition));
                    removeFragment(SaveAddFragment.this);

                } catch (Exception e) {
                    Log.e("test", "e : " + e.getMessage());
                }
            }
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerView() {
        recyclerView = binding.saveAddRecyclerview;
        recyclerViewAdapter = new SaveAddRecyclerViewAdapter(getContext(), itemLists);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
        recyclerView.setLayoutManager(gridLayoutManager);

        // 선택한 인증서 정보 받기
        recyclerViewAdapter.setOnItemClickListener((view, position) -> selectedIconPosition = position);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.addItem(itemLists);
        recyclerViewAdapter.notifyDataSetChanged();
    }

}