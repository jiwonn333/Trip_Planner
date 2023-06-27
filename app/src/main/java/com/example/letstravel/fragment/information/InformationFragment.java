package com.example.letstravel.fragment.information;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.letstravel.BuildConfig;
import com.example.letstravel.MainActivity;
import com.example.letstravel.R;
import com.example.letstravel.databinding.FragmentInformationBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;

public class InformationFragment extends Fragment {

    private FragmentInformationBinding binding;
    private InformationViewModel informationViewModel;
    private MainActivity mainActivity;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInformationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialized();

    }

    private void initialized() {
        Places.initialize(getContext(), BuildConfig.MAPS_API_KEY);
        PlacesClient placesClient = Places.createClient(getContext());

        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setTypesFilter(Arrays.asList("restaurant", "subway_station", "store", "cafe", "bar"));
        autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)));
        autocompleteSupportFragment.setCountries("KR");
        autocompleteSupportFragment.setHint(getString(R.string.search_text));
        autocompleteSupportFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                informationViewModel = new ViewModelProvider(requireActivity()).get(InformationViewModel.class);
                informationViewModel.setTitle(place.getName());

                mainActivity = (MainActivity) getActivity();
                informationViewModel.getTitle().observe(getViewLifecycleOwner(), title -> mainActivity.observing(place.getName(), place.getLatLng().latitude, place.getLatLng().longitude));
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e("test", "error : " + status);
            }
        });
    }


//    private void initView() {
//        binding.toolbar.setOnClickListener(v -> {
//            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//            SearchFragment searchFragment = new SearchFragment();
//            fragmentTransaction.replace(R.id.frameLayout, searchFragment).commit();
//        });
//    }

//    private void initObserver() {
//        informationViewModel = new ViewModelProvider(requireActivity()).get(InformationViewModel.class);
//        informationViewModel.getSearchWord().observe(getViewLifecycleOwner(), search -> {
////            informationViewModel.getSearchWord();
//            binding.tvSearch.setText(search);
//        });
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}