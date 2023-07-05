package com.example.letstravel.fragment.save;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.letstravel.R;
import com.example.letstravel.databinding.FragmentSaveBinding;
import com.example.letstravel.fragment.common.BaseFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class SaveFragment extends BaseFragment {
    private @NonNull FragmentSaveBinding binding;
    private SaveViewModel saveViewModel;


    public static SaveFragment newInstance() {
        return new SaveFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSaveBinding.inflate(inflater, container, false);


        //persistentBottomSheet로 사용할 view 획득
        View bottomSheet = binding.coordinator.findViewById(R.id.bottom_sheet);
        //획득한 view를 bottomsheet로 지정
        BottomSheetBehavior persistentBottomSheet = BottomSheetBehavior.from(bottomSheet);
        persistentBottomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.e("test", "newState : " + newState);
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


    }

    private void addSaveFragment() {
        NavController navHostController = NavHostFragment.findNavController(SaveFragment.this);
        navHostController.navigate(SaveFragmentDirections.actionNavigationSaveToNavigationSaveAdd());

    }

}