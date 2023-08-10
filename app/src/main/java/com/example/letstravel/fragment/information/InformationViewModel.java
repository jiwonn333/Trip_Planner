package com.example.letstravel.fragment.information;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.letstravel.fragment.common.Info;

import java.util.List;

public class InformationViewModel extends ViewModel {
    private MutableLiveData<List<Info>> info;

    public LiveData<List<Info>> getInfo() {
        if (info == null) {
            info = new MutableLiveData<List<Info>>();
            loadInfo();
        }
        return info;
    }

    private void loadInfo() {
        // Do an asynchronous operation to fetch users.
    }

//    private final MutableLiveData<String> title = new MutableLiveData<>();
//
//    public MutableLiveData<String> getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title.postValue(title);
//    }

}