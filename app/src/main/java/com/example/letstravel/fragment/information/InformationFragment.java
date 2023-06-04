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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InformationViewModel informationViewModel =
                new ViewModelProvider(this).get(InformationViewModel.class);

        binding = FragmentInformationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


//        final Toolbar toolbar = binding.infoToolbar.toolbar;
//        informationViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 검색창 클릭하면 SearchFragment로 이동
        binding.toolbar.setOnClickListener(v -> {
//            SearchFragment searchFragment = new SearchFragment();
//            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
//
//            fragmentTransaction.replace(R.id.frameLayout, searchFragment);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();

            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            SearchFragment searchFragment = new SearchFragment();
            // HomeFragment 로 transaction
            fragmentTransaction.replace(R.id.frameLayout, searchFragment).commit();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}