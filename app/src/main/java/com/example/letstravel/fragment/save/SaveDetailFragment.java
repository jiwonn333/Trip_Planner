package com.example.letstravel.fragment.save;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.letstravel.databinding.FragmentSaveDetailBinding;

public class SaveDetailFragment extends Fragment {

    private SaveDetailViewModel saveDetailViewModel;
    private FragmentSaveDetailBinding binding;

    public static SaveDetailFragment newInstance() {
        return new SaveDetailFragment();
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
        saveDetailViewModel = new ViewModelProvider(this).get(SaveDetailViewModel.class);

    }
}