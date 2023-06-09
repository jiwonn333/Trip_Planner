package com.example.letstravel.fragment.search;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.letstravel.R;
import com.example.letstravel.databinding.FragmentSearchBinding;
import com.example.letstravel.fragment.information.InformationViewModel;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private InformationViewModel informationViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        informationViewModel = new ViewModelProvider(requireActivity()).get(InformationViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        binding.ibBack.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction().addToBackStack(null);
            fragmentTransaction.remove(SearchFragment.this).commit();
        });

        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Toast.makeText(getContext(), binding.etSearch.getText().toString() + " 검색", Toast.LENGTH_SHORT).show();
                informationViewModel.setSearchWord(binding.etSearch.getText().toString());
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction().addToBackStack(null);
                fragmentTransaction.remove(SearchFragment.this).commit();

            }
            return false;
        });
    }
}