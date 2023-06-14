package com.example.letstravel.fragment.information;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InformationViewModel extends ViewModel {

    private final MutableLiveData<String> searchWord = new MutableLiveData<>();

    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<Double> latitude = new MutableLiveData<>();
    private final MutableLiveData<Double> longitude = new MutableLiveData<>();

    public final MediatorLiveData<Double> latLngMerger = new MediatorLiveData<>();

    public void setLatLng(Double lat, Double lng) {
        latLngMerger.addSource(latitude, lat1 -> latLngMerger.setValue(lat));
        latLngMerger.addSource(longitude, lng1 -> latLngMerger.setValue(lng));
    }

    public MediatorLiveData<Double> getLatLng() {
        return latLngMerger;
    }


    public void setTitle(String title) {
        this.title.setValue(title);
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public void setLatitude(Double lat) {
        latitude.setValue(lat);
    }

    public MutableLiveData<Double> getLatitude() {
        return latitude;
    }

    public void setLongitude(Double lng) {
        longitude.setValue(lng);
    }

    public MutableLiveData<Double> getLongitude() {
        return longitude;
    }

    public void setSearchWord(String str) {
        searchWord.setValue(str);
    }

    public LiveData<String> getSearchWord() {
        return searchWord;
    }
}