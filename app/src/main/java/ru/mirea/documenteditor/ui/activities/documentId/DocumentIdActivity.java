package ru.mirea.documenteditor.ui.activities.documentId;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;

import ru.mirea.documenteditor.R;
import ru.mirea.documenteditor.data.payload.ParagraphInfo;
import ru.mirea.documenteditor.databinding.ActivityDocumentIdBinding;

public class DocumentIdActivity extends AppCompatActivity {

    private ActivityDocumentIdBinding binding;
    private DocumentIdActivityViewModel documentIdActivityViewModel;

    private EditText paragraphsEditText;
    private TextView lastEditTextView;
    private Integer documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDocumentIdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        documentIdActivityViewModel = new ViewModelProvider(this).get(DocumentIdActivityViewModel.class);
        documentIdActivityViewModel.init();

        setSupportActionBar(binding.toolbar);

        Intent intent = getIntent();
        documentId = (int) intent.getLongExtra("id", -1);

        paragraphsEditText = binding.edittextDocument;
        lastEditTextView = binding.textviewLastEditBy;

        documentIdActivityViewModel.getDocumentIdEditor().observe(this, documentIdEditor -> {
            String username = documentIdEditor.getDocument().getLastEditBy() == null ? "никого" : documentIdEditor.getDocument().getLastEditBy();
            lastEditTextView.setText(MessageFormat.format("Последнее изменение от {0}", username));
            paragraphsEditText.setText(
                    documentIdEditor.getDocumentParagraphs().stream()
                            .sorted(ParagraphInfo::compareTo)
                            .map(ParagraphInfo::getContent)
                            .collect(Collectors.joining("\n"))
            );
            documentIdActivityViewModel.setDocumentParagraphListInMemory(
                    (ArrayList<ParagraphInfo>) documentIdEditor.getDocumentParagraphs()
            );
        });
        documentIdActivityViewModel.getDocument(documentId);

        MutableLiveData<Boolean> isDone = new MutableLiveData<>();
        documentIdActivityViewModel.fetchDocumentKey(isDone, documentId);
        isDone.observe(this, done -> {
            if (!done) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении ключа шифрования документа!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save_document) {
            Toast.makeText(getApplicationContext(), "Сохранение документа ", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.action_go_back) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}