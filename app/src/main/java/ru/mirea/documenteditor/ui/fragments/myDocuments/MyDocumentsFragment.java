package ru.mirea.documenteditor.ui.fragments.myDocuments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import ru.mirea.documenteditor.data.adapter.DocumentsAdapter;
import ru.mirea.documenteditor.data.model.api.document.DocumentInfo;
import ru.mirea.documenteditor.databinding.FragmentMyDocumentsBinding;
import ru.mirea.documenteditor.ui.activities.documentId.DocumentIdActivity;
import ru.mirea.documenteditor.ui.activities.setPassword.SetPasswordActivity;
import ru.mirea.documenteditor.util.Constants;
import ru.mirea.documenteditor.util.PreferenceManager;

public class MyDocumentsFragment extends Fragment {

    private MyDocumentsViewModel myDocumentsViewModel;
    private SwipeRefreshLayout rlDocuments;
    private RecyclerView rvDocuments;
    private Context context;
    private FragmentMyDocumentsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myDocumentsViewModel = new ViewModelProvider(this).get(MyDocumentsViewModel.class);

        binding = FragmentMyDocumentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rlDocuments = binding.rlDocuments;
        rvDocuments = binding.rvDocuments;
        context = getContext();

        rlDocuments.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(context, "Обновляем документы", Toast.LENGTH_SHORT).show();
                myDocumentsViewModel.queryDocuments();
            }
        });

        // Initialize users
        myDocumentsViewModel.getArrayDocumentInfo().observe(getViewLifecycleOwner(), this::setUpRecycleView);

        if (savedInstanceState != null) {
            myDocumentsViewModel.getArrayDocumentInfo().setValue(
                    savedInstanceState.getParcelableArrayList("arrayDocumentInfo")
            );
        } else {
            myDocumentsViewModel.queryDocuments();
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("arrayDocumentInfo", myDocumentsViewModel.getArrayDocumentInfo().getValue());
    }

    private void setUpRecycleView(ArrayList<DocumentInfo> arrayDocumentInfo) {
        if (rvDocuments.getAdapter() == null) {
            DocumentsAdapter adapter = new DocumentsAdapter(arrayDocumentInfo);
            adapter.setOnOpenButtonClickListener(new DocumentsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Long id = arrayDocumentInfo.get(position).getId();
                    String name = arrayDocumentInfo.get(position).getName();
                    Toast.makeText(context, "Загружаем документ " + name, Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(context, DocumentIdActivity.class);
                    myIntent.putExtra("id", id);
                    myIntent.putExtra("name", name);
                    context.startActivity(myIntent);
                }
            });

            adapter.setOnPasswordButtonClickListener(new DocumentsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    Long id = arrayDocumentInfo.get(position).getId();
                    String name = arrayDocumentInfo.get(position).getName();
                    String ownerUsername = arrayDocumentInfo.get(position).getOwner().getUsername();
                    Toast.makeText(context, "Переходим к задаванию пароля на документ " + name, Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(context, SetPasswordActivity.class);
                    myIntent.putExtra("id", id);
                    myIntent.putExtra("name", name);
                    myIntent.putExtra("owner", ownerUsername.equals(PreferenceManager.getInstance().getString(Constants.USER_NAME)));
                    context.startActivity(myIntent);
                }
            });

            rvDocuments.setAdapter(adapter);
            rvDocuments.setLayoutManager(new LinearLayoutManager(context));
            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            rvDocuments.addItemDecoration(itemDecoration);
        } else {
            DocumentsAdapter adapter = (DocumentsAdapter) rvDocuments.getAdapter();
            adapter.swap(arrayDocumentInfo);
        }
        rlDocuments.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}