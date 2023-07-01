package com.example.letstravel.fragment.save;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.letstravel.R;
import com.example.letstravel.databinding.FragmentSaveBinding;

public class SaveFragment extends Fragment {
    private FragmentSaveBinding binding;
    private SaveViewModel saveViewModel;


    public static SaveFragment newInstance() {
        return new SaveFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSaveBinding.inflate(inflater, container, false);


        binding.btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("test", "버튼 클릭됨");
                addSaveFragment();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void addSaveFragment() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        SaveAddFragment saveAddFragment = new SaveAddFragment();
        fragmentTransaction.replace(R.id.frameLayout, saveAddFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();

    }

}