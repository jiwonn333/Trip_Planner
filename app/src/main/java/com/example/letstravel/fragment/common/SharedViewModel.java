package com.example.letstravel.fragment.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Group>> groupList = new MutableLiveData<>(new ArrayList<>());
    public void addGroup(Group group) {
        this.groupList.getValue().add(group);
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList.setValue(groupList);
    }

    public LiveData<List<Group>> getGroup() {
        return groupList;
    }

    public boolean checkAlreadyExistGroup(Group group) {
        for (Group g : groupList.getValue()) {
            if (g.getTitle().equals(group.getTitle())) {
                return true;
            }
        }

        return false;
    }
}
