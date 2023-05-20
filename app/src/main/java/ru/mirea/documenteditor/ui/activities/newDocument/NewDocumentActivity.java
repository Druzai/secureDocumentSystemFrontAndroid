package ru.mirea.documenteditor.ui.activities.newDocument;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.documenteditor.databinding.ActivityNewDocumentBinding;
import ru.mirea.documenteditor.util.Constants;

public class NewDocumentActivity extends AppCompatActivity {

    private ActivityNewDocumentBinding binding;
    private NewDocumentActivityViewModel newDocumentActivityViewModel;

    private EditText nameEditText;

    private Button btnCreateDocument;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewDocumentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        newDocumentActivityViewModel = new ViewModelProvider(this).get(NewDocumentActivityViewModel.class);
        newDocumentActivityViewModel.init();

        nameEditText = binding.textDocumentEditName;
        newDocumentActivityViewModel.getNewDocumentName().observe(this, newDocumentName -> {
            if (!nameEditText.getText().toString().equals(newDocumentActivityViewModel.getNewDocumentName().getValue()))
                nameEditText.setText(newDocumentName);
        });
        nameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                newDocumentActivityViewModel.getNewDocumentName().setValue(s.toString());
                Log.d(Constants.LOG_TAG, "Updating LiveData for EditText!");
                Log.d(Constants.LOG_TAG, s.toString());
            }
        });

        btnCreateDocument = binding.btCreateDocument;
        btnCreateDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = nameEditText.getText().toString();
                if (newName.trim().length() == 0){
                    Toast.makeText(getApplicationContext(), "Имя документа не может быть пустым или состоять из пробельных символов!", Toast.LENGTH_SHORT).show();
                    return;
                }

                MutableLiveData<Boolean> isDone = new MutableLiveData<>();
                newDocumentActivityViewModel.postNewDocument(isDone);
                isDone.observe(NewDocumentActivity.this, bIsDone -> {
                    if (bIsDone) {
                        Toast.makeText(getApplicationContext(), "Документ успешно создан!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Документ не удалось создать!", Toast.LENGTH_SHORT).show();
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

        if (savedInstanceState != null) {
            newDocumentActivityViewModel.getNewDocumentName().setValue(savedInstanceState.getString("newDocumentName"));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("newDocumentName", newDocumentActivityViewModel.getNewDocumentName().getValue());
    }
}