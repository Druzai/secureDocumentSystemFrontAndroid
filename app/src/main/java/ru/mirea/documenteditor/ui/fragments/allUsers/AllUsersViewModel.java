package ru.mirea.documenteditor.ui.fragments.allUsers;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ru.mirea.documenteditor.data.model.api.user.UserInfo;

public class AllUsersViewModel extends ViewModel {

    private AllUsersRepository allUsersRepository;
    private final MutableLiveData<ArrayList<UserInfo>> mUserInfo;

    public AllUsersViewModel() {
        mUserInfo = new MutableLiveData<>();
        allUsersRepository = AllUsersRepository.getInstance();
        allUsersRepository.init();
    }

    public MutableLiveData<ArrayList<UserInfo>> getArrayUserInfo() {
        return mUserInfo;
    }

    public void queryUsers() {
        allUsersRepository.getUsers(mUserInfo);
    }
}