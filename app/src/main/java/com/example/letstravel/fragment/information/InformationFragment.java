package com.example.letstravel.fragment.information;

import android.graphics.Bitmap;
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
import com.example.letstravel.fragment.common.BaseFragment;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InformationFragment extends BaseFragment {

    private FragmentInformationBinding binding;
    private InformationViewModel informationViewModel;
    private MainActivity mainActivity;
    private PlacesClient placesClient;
    final String placeId = BuildConfig.PLACE_ID;


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

        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setTypesFilter(Arrays.asList("restaurant", "subway_station", "store", "cafe", "bar"));
        autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)));
        autocompleteSupportFragment.setCountries("KR");
        autocompleteSupportFragment.setHint(getString(R.string.search_text));
        autocompleteSupportFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.d("test", "place id : " + place.getId());
                informationViewModel = new ViewModelProvider(requireActivity()).get(InformationViewModel.class);
                informationViewModel.setTitle(place.getName());
                mainActivity = (MainActivity) getActivity();
                informationViewModel.getTitle().observe(getViewLifecycleOwner(), title -> mainActivity.initObserve(place.getName(), place.getLatLng().latitude, place.getLatLng().longitude));
                showPhoto(place.getName(), place.getAddress());
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e("test", "error : " + status);
            }
        });
    }

    private void showPhoto(String name, String address) {
        Log.d("test", "success");
        placesClient = Places.createClient(requireContext());

        // 필드를 지정, 사진 요청에는 항상 PHOTO_METADATAS 필드가 있어야 한다.
        final List<Place.Field> fields = Collections.singletonList(Place.Field.PHOTO_METADATAS);

        final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);
        placesClient.fetchPlace(placeRequest).addOnSuccessListener(response -> {
            final Place place = response.getPlace();
            final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
            if (metadata == null || metadata.isEmpty()) {
                Log.w("test", "No photo metadata.");
                return;
            }

            final PhotoMetadata photoMetadata = metadata.get(0);
            final String attr = photoMetadata.getAttributions();
            final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500)
                    .setMaxHeight(300)
                    .build();

            placesClient.fetchPhoto(photoRequest).addOnSuccessListener(fetchPhotoResponse -> {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                binding.detailsLayout.setVisibility(View.VISIBLE);
                binding.ivImage.setImageBitmap(bitmap);
                binding.tvTitle.setText(name);
                binding.tvDetail.setText(address);
                Log.e("test", "address : " + address);

            }).addOnFailureListener(exception -> {
                if (exception instanceof ApiException) {
                    final ApiException apiException = (ApiException) exception;
                    Log.e("test", "Place not found : " + exception.getMessage());

                }
            });
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