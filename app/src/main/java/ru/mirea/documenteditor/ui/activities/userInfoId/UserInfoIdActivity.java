package ru.mirea.documenteditor.ui.activities.userInfoId;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;

import ru.mirea.documenteditor.data.model.api.document.DocumentRight;
import ru.mirea.documenteditor.data.model.api.user.UserIdInfo;
import ru.mirea.documenteditor.databinding.ActivityUserInfoIdBinding;
import ru.mirea.documenteditor.util.StringWithTag;

public class UserInfoIdActivity extends AppCompatActivity {

    private ActivityUserInfoIdBinding binding;
    private UserInfoIdActivityViewModel userInfoIdActivityViewModel;

    private TextView welcomeTextView;
    private TextView settingTextView;
    private Spinner documentsSpinner;
    private Spinner rolesSpinner;

    private LinearLayout llDocuments;
    private LinearLayout llRoles;

    private Button btnChangeRights;
    private Button btnBack;
    private Integer userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserInfoIdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userInfoIdActivityViewModel = new ViewModelProvider(this).get(UserInfoIdActivityViewModel.class);
        userInfoIdActivityViewModel.init();

        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);

        welcomeTextView = binding.textWelcomeChangeRights;
        settingTextView = binding.textChangeSection;
        documentsSpinner = binding.spinnerDocumentRight;
        rolesSpinner = binding.spinnerRoleRight;
        llDocuments = binding.linearLayoutDocumentsRight;
        llRoles = binding.linearLayoutRolesRight;

        btnChangeRights = binding.btChangeRights;
        btnChangeRights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringWithTag documentId = (StringWithTag) documentsSpinner.getSelectedItem();
                StringWithTag roleId = (StringWithTag) rolesSpinner.getSelectedItem();

                MutableLiveData<Boolean> isDone = new MutableLiveData<>();
                userInfoIdActivityViewModel.postChangedRights(
                        isDone,
                        new DocumentRight(
                                (Long) documentId.tag,
                                (Long) roleId.tag,
                                Long.valueOf(userId)
                        )
                );
                isDone.observe(UserInfoIdActivity.this, bIsDone -> {
                    if (bIsDone) {
                        Toast.makeText(getApplicationContext(), "Роль успешно изменена!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Роль не удалось измененить!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnBack = binding.btBack;
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userInfoIdActivityViewModel.getUserIdInfo().observe(this, userIdInfo -> {
            if (userIdInfo.isMe()) {
                welcomeTextView.setText("Добро пожаловать на вашу страницу!");
            } else {
                welcomeTextView.setText(MessageFormat.format("Добро пожаловать на страницу \"{0}\"!", userIdInfo.getUsername()));
            }
            if (userIdInfo.getDocuments() != null) {
                // Document spinner
                ArrayList<StringWithTag> docArray = userIdInfo.getDocuments().stream()
                        .map(d -> new StringWithTag(d.getName(), d.getId()))
                        .collect(Collectors.toCollection(ArrayList::new));
                ArrayAdapter<StringWithTag> docAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, docArray);
                documentsSpinner.setAdapter(docAdapter);
                llDocuments.setVisibility(View.VISIBLE);
            } else {
                llDocuments.setVisibility(View.GONE);
            }
            if (userIdInfo.getAllRoles() != null) {
                // Role spinner
                ArrayList<StringWithTag> roleArray = userIdInfo.getAllRoles().stream()
                        .map(r -> new StringWithTag(r.getUserName(), r.getId()))
                        .collect(Collectors.toCollection(ArrayList::new));
                ArrayAdapter<StringWithTag> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roleArray);
                rolesSpinner.setAdapter(roleAdapter);
                llRoles.setVisibility(View.VISIBLE);
            } else {
                llRoles.setVisibility(View.GONE);
            }
            if (userIdInfo.getDocuments() == null && userIdInfo.getAllRoles() == null) {
                btnChangeRights.setVisibility(View.GONE);
                settingTextView.setVisibility(View.GONE);
            } else {
                btnChangeRights.setVisibility(View.VISIBLE);
                settingTextView.setVisibility(View.VISIBLE);
            }
        });
        if (savedInstanceState != null) {
            UserIdInfo userIdInfo = new UserIdInfo(
                    savedInstanceState.getString("username"),
                    savedInstanceState.getBoolean("me"),
                    savedInstanceState.getParcelableArrayList("documents"),
                    savedInstanceState.getParcelableArrayList("allRoles")
            );
            userInfoIdActivityViewModel.getUserIdInfo().setValue(userIdInfo);
        } else {
            userInfoIdActivityViewModel.fetchUserInfoById(userId);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        UserIdInfo userIdInfo = userInfoIdActivityViewModel.getUserIdInfo().getValue();
        outState.putBoolean("me", userIdInfo.isMe());
        outState.putString("username", userIdInfo.getUsername());
        outState.putParcelableArrayList("documents", (ArrayList<? extends Parcelable>) userIdInfo.getDocuments());
        outState.putParcelableArrayList("allRoles", (ArrayList<? extends Parcelable>) userIdInfo.getAllRoles());
    }
}