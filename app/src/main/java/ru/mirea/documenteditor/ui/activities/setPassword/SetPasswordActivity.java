package ru.mirea.documenteditor.ui.activities.setPassword;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ru.mirea.documenteditor.data.model.api.document.DocumentPassword;
import ru.mirea.documenteditor.data.model.api.user.RoleInfo;
import ru.mirea.documenteditor.databinding.ActivitySetPasswordBinding;
import ru.mirea.documenteditor.util.StringWithTag;

public class SetPasswordActivity extends AppCompatActivity {

    private ActivitySetPasswordBinding binding;
    private SetPasswordActivityViewModel setPasswordActivityViewModel;

    private TextView welcomeTextView;
    private Spinner rolesSpinner;

    private LinearLayout llRoles;

    private EditText passwordEditText;

    private Button btnCreateDocumentPassword;
    private Button btnBack;
    private Long documentId;
    private String documentName;
    private boolean owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setPasswordActivityViewModel = new ViewModelProvider(this).get(SetPasswordActivityViewModel.class);
        setPasswordActivityViewModel.init();

        Intent intent = getIntent();
        documentId = intent.getLongExtra("id", -1);
        documentName = intent.getStringExtra("name");
        owner = intent.getBooleanExtra("owner", false);

        welcomeTextView = binding.textWelcomeDocumentSetPassword;
        rolesSpinner = binding.spinnerRoleRight;
        llRoles = binding.linearLayoutRolesRight;
        passwordEditText = binding.textDocumentEditPassword;

        if (documentName != null)
            welcomeTextView.setText(MessageFormat.format("Создание пароля для документа \"{0}\"!", documentName));


        btnCreateDocumentPassword = binding.btCreateDocumentPassword;
        btnCreateDocumentPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringWithTag roleId = (StringWithTag) rolesSpinner.getSelectedItem();
                String newPassword = passwordEditText.getText().toString();
                if (newPassword.trim().length() == 0){
                    Toast.makeText(getApplicationContext(), "Пароль документа не может быть пустым!", Toast.LENGTH_SHORT).show();
                    return;
                }

                MutableLiveData<Boolean> isDone = new MutableLiveData<>();
                setPasswordActivityViewModel.postSetPassword(
                        isDone,
                        new DocumentPassword(
                                documentId,
                                newPassword,
                                (long) roleId.tag
                        )
                );
                isDone.observe(SetPasswordActivity.this, bIsDone -> {
                    if (bIsDone) {
                        Toast.makeText(getApplicationContext(), "Пароль успешно создан!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Пароль не удалось создать!", Toast.LENGTH_SHORT).show();
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

        setPasswordActivityViewModel.getListRoleInfo().observe(this, listRoleInfo -> {
            if (listRoleInfo != null) {
                if (!owner) {
                    listRoleInfo = listRoleInfo.stream()
                            .filter(r -> r.getId() != 4L)
                            .collect(Collectors.toList());
                }
                // Role spinner
                ArrayList<StringWithTag> roleArray = listRoleInfo.stream()
                        .map(r -> new StringWithTag(r.getUserName(), r.getId()))
                        .collect(Collectors.toCollection(ArrayList::new));
                ArrayAdapter<StringWithTag> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roleArray);
                rolesSpinner.setAdapter(roleAdapter);
                llRoles.setVisibility(View.VISIBLE);
            } else {
                llRoles.setVisibility(View.GONE);
                btnCreateDocumentPassword.setVisibility(View.GONE);
            }
        });
        if (savedInstanceState != null) {
            passwordEditText.setText(savedInstanceState.getString("password"));
            owner = savedInstanceState.getBoolean("owner");
            setPasswordActivityViewModel.getListRoleInfo().setValue(savedInstanceState.getParcelableArrayList("allRoles"));
        } else {
            setPasswordActivityViewModel.fetchListRoleInfo();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        List<RoleInfo> roleInfoList = setPasswordActivityViewModel.getListRoleInfo().getValue();
        outState.putString("password", passwordEditText.getText().toString());
        outState.putBoolean("owner", owner);
        outState.putParcelableArrayList("allRoles", (ArrayList<? extends Parcelable>) roleInfoList);
    }
}