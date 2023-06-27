package com.example.letstravel.fragment.information;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InformationViewModel extends ViewModel {
    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<Double> latitude = new MutableLiveData<>();
    private final MutableLiveData<Double> longitude = new MutableLiveData<>();

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title.postValue(title);
    }

    public MutableLiveData<Double> getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude.postValue(latitude);
    }

    public MutableLiveData<Double> getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude.postValue(longitude);
    }


}