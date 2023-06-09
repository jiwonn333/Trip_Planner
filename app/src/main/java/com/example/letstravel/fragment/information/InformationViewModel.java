package com.example.letstravel.fragment.information;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.letstravel.R;

import java.util.EventListener;

public class InformationViewModel extends ViewModel {

    private final MutableLiveData<String> searchWord = new MutableLiveData<String>();
    public void setSearchWord(String str) {
        searchWord.setValue(str);
    }
    public LiveData<String> getSearchWord() {
        return searchWord;
    }
}