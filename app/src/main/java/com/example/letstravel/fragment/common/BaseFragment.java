package com.example.letstravel.fragment.common;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

public abstract class BaseFragment extends Fragment {

    protected void replaceFragment(Fragment fragment, NavDirections navDirections) {
        NavController navHostController = NavHostFragment.findNavController(fragment);
        navHostController.navigate(navDirections);
    }

    protected void removeFragment(Fragment fragment) {
        NavController navHostController = NavHostFragment.findNavController(fragment);
        navHostController.popBackStack();
    }

    protected void addFragment() {

    }
}
