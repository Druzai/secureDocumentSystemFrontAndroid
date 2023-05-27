package ru.mirea.documenteditor.ui.fragments.addDocumentPassword;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;

import ru.mirea.documenteditor.data.model.api.document.DocumentPassword;
import ru.mirea.documenteditor.data.model.api.user.RoleInfo;
import ru.mirea.documenteditor.databinding.FragmentAddDocumentPasswordBinding;
import ru.mirea.documenteditor.util.StringWithTag;

public class AddDocumentPasswordFragment extends Fragment {

    private FragmentAddDocumentPasswordBinding binding;
    private AddDocumentPasswordViewModel addDocumentPasswordViewModel;

    private TextView settingTextView;
    private Spinner documentsSpinner;

    private EditText passwordEditText;

    private LinearLayout llDocuments;

    private SwipeRefreshLayout rlAddDocumentPassword;

    private Button btnChangeRights;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addDocumentPasswordViewModel = new ViewModelProvider(this).get(AddDocumentPasswordViewModel.class);
        addDocumentPasswordViewModel.init();

        binding = FragmentAddDocumentPasswordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        settingTextView = binding.textChangeSection;
        documentsSpinner = binding.spinnerDocumentRight;
        llDocuments = binding.linearLayoutDocumentsRight;
        passwordEditText = binding.textDocumentEditPassword;
        rlAddDocumentPassword = binding.rlAddDocumentPassword;

        rlAddDocumentPassword.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), "Список документов", Toast.LENGTH_SHORT).show();
                addDocumentPasswordViewModel.fetchListDocumentInfoShort();
            }
        });

        btnChangeRights = binding.btChangeRights;
        btnChangeRights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringWithTag documentId = (StringWithTag) documentsSpinner.getSelectedItem();
                String newPassword = passwordEditText.getText().toString();
                if (newPassword.trim().length() == 0){
                    Toast.makeText(getContext(), "Пароль документа не может быть пустым!", Toast.LENGTH_SHORT).show();
                    return;
                }

                MutableLiveData<RoleInfo> roleInfo = new MutableLiveData<>();
                addDocumentPasswordViewModel.postCheckPassword(
                        roleInfo,
                        new DocumentPassword(
                                (long) documentId.tag,
                                newPassword,
                                null
                        )
                );
                roleInfo.observe(getViewLifecycleOwner(), mRoleInfo -> {
                    if (mRoleInfo == null) {
                        Toast.makeText(getContext(), "Не удалось получить доступ!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(
                                getContext(),
                                MessageFormat.format(
                                        "Доступ к документу \"{0}\" получен с ролью \"{1}\"!",
                                        documentId.string,
                                        mRoleInfo.getUserName()
                                ),
                                Toast.LENGTH_LONG
                        ).show();
                        passwordEditText.setText("");
                    }
                });
            }
        });

        addDocumentPasswordViewModel.getListDocumentInfoShort().observe(getViewLifecycleOwner(), listDocumentInfoShort -> {
            if (listDocumentInfoShort != null) {
                // Document spinner
                ArrayList<StringWithTag> docArray = listDocumentInfoShort.stream()
                        .map(d -> new StringWithTag(d.getName(), d.getId()))
                        .collect(Collectors.toCollection(ArrayList::new));
                ArrayAdapter<StringWithTag> docAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, docArray);
                documentsSpinner.setAdapter(docAdapter);
                llDocuments.setVisibility(View.VISIBLE);
            } else {
                llDocuments.setVisibility(View.GONE);
                btnChangeRights.setVisibility(View.GONE);
                settingTextView.setVisibility(View.GONE);
            }
            rlAddDocumentPassword.setRefreshing(false);
        });
        if (savedInstanceState != null) {
            addDocumentPasswordViewModel.getListDocumentInfoShort().setValue(savedInstanceState.getParcelableArrayList("documentList"));
            passwordEditText.setText(savedInstanceState.getString("password"));
        } else {
            addDocumentPasswordViewModel.fetchListDocumentInfoShort();
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (addDocumentPasswordViewModel != null) {
            outState.putParcelableArrayList("documentList", (ArrayList<? extends Parcelable>) addDocumentPasswordViewModel.getListDocumentInfoShort().getValue());
            outState.putString("password", passwordEditText.getText().toString());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}