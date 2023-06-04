package com.example.letstravel.fragment.information;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.letstravel.R;

import java.util.EventListener;

public class InformationViewModel extends ViewModel {

    private final MutableLiveData<String> textHint;

    public InformationViewModel() {
        textHint = new MutableLiveData<>();
        textHint.setValue("송파구");
    }

    public LiveData<String> getText() {
        return textHint;
    }

}