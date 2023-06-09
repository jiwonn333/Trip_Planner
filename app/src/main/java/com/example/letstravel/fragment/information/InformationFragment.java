package com.example.letstravel.fragment.information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.letstravel.R;
import com.example.letstravel.databinding.FragmentInformationBinding;
import com.example.letstravel.fragment.search.SearchFragment;

public class InformationFragment extends Fragment {

    private FragmentInformationBinding binding;
    private InformationViewModel informationViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInformationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initObserver();


    }

    private void initView() {
        binding.toolbar.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            SearchFragment searchFragment = new SearchFragment();
            fragmentTransaction.replace(R.id.frameLayout, searchFragment).commit();
        });
    }

    private void initObserver() {
        informationViewModel = new ViewModelProvider(requireActivity()).get(InformationViewModel.class);
        informationViewModel.getSearchWord().observe(getViewLifecycleOwner(), search -> {
//            informationViewModel.getSearchWord();
            binding.tvSearch.setText(search);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}