package com.example.letstravel.fragment.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Group> group = new MutableLiveData<>();

    public void setGroup(Group group) {
        this.group.setValue(group);
    }

    public LiveData<Group> getGroup() {
        return group;
    }
}
