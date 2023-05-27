package ru.mirea.documenteditor.ui.activities.setPassword;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mirea.documenteditor.data.model.api.document.DocumentPassword;
import ru.mirea.documenteditor.data.model.api.document.DocumentRight;
import ru.mirea.documenteditor.data.model.api.user.RoleInfo;
import ru.mirea.documenteditor.data.model.api.user.UserIdInfo;

public class SetPasswordActivityViewModel extends ViewModel {

    private MutableLiveData<List<RoleInfo>> mListRoleInfo;
    private SetPasswordActivityRepository setPasswordActivityRepository;


    public void init() {
        mListRoleInfo = new MutableLiveData<>();
        setPasswordActivityRepository = SetPasswordActivityRepository.getInstance();
        setPasswordActivityRepository.init();
    }

    public MutableLiveData<List<RoleInfo>> getListRoleInfo() {
        return mListRoleInfo;
    }

    public void fetchListRoleInfo() {
        setPasswordActivityRepository.getListRoleInfo(mListRoleInfo);
    }

    public void postSetPassword(MutableLiveData<Boolean> isDone, DocumentPassword documentPassword) {
        setPasswordActivityRepository.postSetPassword(isDone, documentPassword);
    }
}
