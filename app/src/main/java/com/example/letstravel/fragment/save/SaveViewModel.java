package com.example.letstravel.fragment.save;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SaveViewModel extends ViewModel {
    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<String> description = new MutableLiveData<>();
    private final MutableLiveData<Integer> drawable = new MutableLiveData<>();
    MediatorLiveData<Object> mediatorLiveData = new MediatorLiveData<>();

    public SaveViewModel() {
        mediatorLiveData.addSource(title, o -> mediatorLiveData.setValue(o));
        mediatorLiveData.addSource(drawable, o -> mediatorLiveData.setValue(o));
    }

    public MediatorLiveData<Object> getMediatorLiveData() {
        return mediatorLiveData;
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public void setTitle(String str) {
        title.setValue(str);
    }

    public LiveData<String> getDescription() {
        return description;
    }

    public void setDescription(String str) {
        description.setValue(str);
    }

    public LiveData<Integer> getDrawable() {
        return drawable;
    }

    public void setDrawable(int res) {
        drawable.setValue(res);
    }


}