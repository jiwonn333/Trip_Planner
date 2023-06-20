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

    public void setLatLng(double lat, double lng) {
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

    public void setSearchWord(String str) {
        searchWord.setValue(str);
    }

    public LiveData<String> getSearchWord() {
        return searchWord;
    }
}