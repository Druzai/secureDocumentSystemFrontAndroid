package ru.mirea.documenteditor.ui.activities.userInfoId;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.documenteditor.data.payload.DocumentRight;
import ru.mirea.documenteditor.data.payload.UserIdInfo;
import ru.mirea.documenteditor.data.payload.UserInfo;

public class UserInfoIdActivityViewModel extends ViewModel {

    private MutableLiveData<UserIdInfo> mUserIdInfo;
    private UserInfoIdActivityRepository userInfoIdActivityRepository;


    public void init(){
        mUserIdInfo = new MutableLiveData<>();
        userInfoIdActivityRepository = UserInfoIdActivityRepository.getInstance();
        userInfoIdActivityRepository.init();
//        userInfoIdActivityRepository.checkLogin(isSignedIn);
    }

    public MutableLiveData<UserIdInfo> getUserIdInfo(){
        return mUserIdInfo;
    }

    public void fetchUserInfoById(Integer userId){
        userInfoIdActivityRepository.getUserInfoId(mUserIdInfo, userId);
    }

    public void postChangedRights(MutableLiveData<Boolean> isDone, DocumentRight documentRight){
        userInfoIdActivityRepository.postRights(isDone, documentRight);
    }
}
