package ru.mirea.documenteditor.ui.activities.documentId;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.mirea.documenteditor.R;
import ru.mirea.documenteditor.data.payload.DocumentIdEditor;
import ru.mirea.documenteditor.data.payload.ParagraphInfo;
import ru.mirea.documenteditor.data.payload.WSContent;
import ru.mirea.documenteditor.data.payload.WSMessage;
import ru.mirea.documenteditor.databinding.ActivityDocumentIdBinding;
import ru.mirea.documenteditor.util.CipherManager;
import ru.mirea.documenteditor.util.Constants;
import ru.mirea.documenteditor.util.PreferenceManager;
import ru.mirea.documenteditor.util.aes.Cipher;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

public class DocumentIdActivity extends AppCompatActivity {

    private ActivityDocumentIdBinding binding;
    private DocumentIdActivityViewModel documentIdActivityViewModel;

    private EditText paragraphsEditText;
    private TextView lastEditTextView;
    private Integer documentId;
    private String username;

    private Cipher cipher;
    private StompClient stompClient;
    private Gson mGson = new GsonBuilder().create();
    private CompositeDisposable compositeDisposable;

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
        String name = intent.getStringExtra("name");
        if (name != null)
            setTitle(name);
        username = PreferenceManager.getInstance().getString(Constants.USER_NAME);

        paragraphsEditText = binding.edittextDocument;
        lastEditTextView = binding.textviewLastEditBy;

        MutableLiveData<Boolean> isFetchedKey = new MutableLiveData<>();
        isFetchedKey.observe(this, done -> {
            if (!done) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении ключа шифрования документа!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                cipher = CipherManager.getInstance().getCipher(PreferenceManager.getInstance().getString(Constants.DOCUMENT_WS_KEY));
                documentIdActivityViewModel.getDocument(documentId);
            }
        });
        documentIdActivityViewModel.fetchDocumentKey(isFetchedKey, documentId);

        MutableLiveData<Boolean> isFetchedDocumentIdEditor = new MutableLiveData<>();
        documentIdActivityViewModel.getDocumentIdEditor().observe(this, documentIdEditor -> {
            if (documentIdEditor.isEditor() || documentIdEditor.isOwner()) {
                paragraphsEditText.setEnabled(true);
                paragraphsEditText.setFocusable(true);
            } else {
                paragraphsEditText.setEnabled(false);
                paragraphsEditText.setFocusable(false);
            }
            String username = documentIdEditor.getDocument().getLastEditBy() == null ? "никого" : documentIdEditor.getDocument().getLastEditBy();
            lastEditTextView.setText(MessageFormat.format("Последнее изменение от {0}", username));
            paragraphsEditText.setText(
                    documentIdEditor.getDocumentParagraphs().stream()
                            .sorted(ParagraphInfo::compareTo)
                            .map(ParagraphInfo::getContent)
                            .collect(Collectors.joining("\n"))
            );
            isFetchedDocumentIdEditor.setValue(true);
        });

        isFetchedDocumentIdEditor.observe(this, done -> {
            documentIdActivityViewModel.setDocumentParagraphListInMemory(
                    Objects.requireNonNull(documentIdActivityViewModel.getDocumentIdEditor().getValue()).getDocumentParagraphs()
            );
            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Constants.WEB_SOCKET_URL);
            resetSubscriptions();
            connectStomp();
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
            Toast.makeText(getApplicationContext(), "Выполняем сохранение документа...", Toast.LENGTH_SHORT).show();
            compareAndSaveChanges();
            return true;
        } else if (item.getItemId() == R.id.action_go_back) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void connectStomp() {
        stompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        resetSubscriptions();

        Disposable dLifecycle = stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.i(Constants.LOG_TAG, "Stomp connection opened");
                            break;
                        case ERROR:
                            Log.e(Constants.LOG_TAG, "Stomp connection error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.i(Constants.LOG_TAG, "Stomp connection closed");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.w(Constants.LOG_TAG, "Stomp failed server heartbeat");
                            break;
                    }
                });

        compositeDisposable.add(dLifecycle);

        // Receive messages
        Disposable dTopic = stompClient.topic(Constants.WEB_SOCKET_TOPIC_LISTEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(Constants.LOG_TAG, "Received " + topicMessage.getPayload());
                    listenOnStompTopic(topicMessage);
                }, throwable -> {
                    Log.e(Constants.LOG_TAG, "Error on subscribe topic", throwable);
                });

        compositeDisposable.add(dTopic);

        stompClient.connect();
    }

    public void listenOnStompTopic(StompMessage stompMessage) {
        WSMessage wsMessage = mGson.fromJson(stompMessage.getPayload(), WSMessage.class);

        String decodedFromUser = cipher.decrypt(wsMessage.getFromUser());
        if (!Objects.equals(wsMessage.getDocumentId(), documentId) || decodedFromUser.equals(username)) {
            return;
        }

        // Decrypt message
        WSMessage decodedMessage = new WSMessage(
                wsMessage.getDocumentId(),
                decodedFromUser,
                cipher.decrypt(wsMessage.getCommand()),
                (ArrayList<WSContent>)
                        wsMessage.getContent().stream()
                                .map(c -> new WSContent(
                                        c.getNumber(),
                                        cipher.decrypt(c.getData()),
                                        cipher.decrypt(c.getAlign())
                                ))
                                .collect(Collectors.toList())
        );

        DocumentIdEditor documentIdEditor = documentIdActivityViewModel.getDocumentIdEditor().getValue();
        assert documentIdEditor != null;
        List<ParagraphInfo> paragraphInfoList = documentIdEditor.getDocumentParagraphs();

        switch (decodedMessage.getCommand()) {
            case "create":
                paragraphInfoList.addAll(
                        decodedMessage.getContent().stream()
                                .map(ParagraphInfo::new)
                                .collect(Collectors.toList())
                );
                break;
            case "delete":
                for (WSContent wsContent : decodedMessage.getContent()) {
                    paragraphInfoList.remove((int) wsContent.getNumber());
                }
                break;
            case "edit":
                for (WSContent wsContent : decodedMessage.getContent()) {
                    paragraphInfoList.set((int) wsContent.getNumber(), new ParagraphInfo(wsContent));
                }
                break;
        }
        documentIdEditor.getDocument().setLastEditBy(decodedMessage.getFromUser());
        documentIdActivityViewModel.setDocumentParagraphListInMemory(paragraphInfoList);
        documentIdEditor.setDocumentParagraphs(paragraphInfoList);
        documentIdActivityViewModel.getDocumentIdEditor().setValue(documentIdEditor);
    }

    public void compareAndSaveChanges() {
        final String align = "left";

        ArrayList<ParagraphInfo> contentsCopy = documentIdActivityViewModel.getDocumentParagraphListInMemory();
        DocumentIdEditor documentIdEditor = documentIdActivityViewModel.getDocumentIdEditor().getValue();

        String[] arrayParagraphs = paragraphsEditText.getText().toString().split("\n");

        List<ParagraphInfo> dos = IntStream.range(0, arrayParagraphs.length).boxed()
                .map(i -> new ParagraphInfo(i, arrayParagraphs[i], align))
                .collect(Collectors.toList());

        ArrayList<WSContent> toSend = new ArrayList<>();

        MutableLiveData<Boolean> isSent = new MutableLiveData<>();
        isSent.observe(this, sent -> {
            if (!sent) {
                Toast.makeText(getApplicationContext(), "Ошибка при отправке изменений!", Toast.LENGTH_SHORT).show();
            }
        });
        int limit = contentsCopy.size();
        if (contentsCopy.size() < dos.size()) {
            for (int i = contentsCopy.size(); i < dos.size(); i++) {
                toSend.add(new WSContent(i, dos.get(i).getContent(), align));
            }
            isSent.setValue(sendStompMessage(new WSMessage(documentId, username, "create", toSend)));
        } else if (contentsCopy.size() > dos.size()) {
            for (int i = dos.size(); i < contentsCopy.size(); i++) {
                toSend.add(new WSContent(i, contentsCopy.get(i).getContent(), align));
            }
            isSent.setValue(sendStompMessage(new WSMessage(documentId, username, "delete", toSend)));
            limit = dos.size();
        }
        toSend.clear();


        for (int i = 0; i < limit; i++) {
            if (!contentsCopy.get(i).getContent().equals(dos.get(i).getContent()) ||
                    !contentsCopy.get(i).getAlign().equals(dos.get(i).getAlign())) {
                toSend.add(new WSContent(i, dos.get(i).getContent(), align));
            }
        }
        isSent.setValue(sendStompMessage(new WSMessage(documentId, username, "edit", toSend)));

        documentIdActivityViewModel.setDocumentParagraphListInMemory(dos);
        documentIdEditor.getDocument().setLastEditBy(username);
        documentIdEditor.setDocumentParagraphs(dos);
        documentIdActivityViewModel.getDocumentIdEditor().setValue(documentIdEditor);
    }

    public boolean sendStompMessage(WSMessage wsMessage) {
        if (!stompClient.isConnected())
            return false;

        if (wsMessage.getContent().size() == 0) {
            return true;
        }
        // Encrypt message
        WSMessage encodedMessage = new WSMessage(
                wsMessage.getDocumentId(),
                cipher.encrypt(wsMessage.getFromUser()),
                cipher.encrypt(wsMessage.getCommand()),
                (ArrayList<WSContent>)
                        wsMessage.getContent().stream()
                                .map(c -> new WSContent(
                                        c.getNumber(),
                                        cipher.encrypt(c.getData()),
                                        cipher.encrypt(c.getAlign())
                                ))
                                .collect(Collectors.toList())
        );

        compositeDisposable.add(stompClient.send(Constants.WEB_SOCKET_MESSAGE_SEND, mGson.toJson(encodedMessage))
                .compose(applySchedulers())
                .subscribe(() -> {
                    Log.d(Constants.LOG_TAG, "STOMP wsMessage sent successfully");
                }, throwable -> {
                    Log.e(Constants.LOG_TAG, "Error send STOMP wsMessage", throwable);
                }));
        return true;
    }

    protected CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onDestroy() {
        stompClient.disconnect();

        if (compositeDisposable != null)
            compositeDisposable.dispose();
        super.onDestroy();
    }
}