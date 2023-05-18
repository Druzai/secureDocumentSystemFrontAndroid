package ru.mirea.documenteditor.ui.fragments.gallery;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.documenteditor.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), e -> {
            if (!textView.getText().toString().equals(galleryViewModel.getText().getValue()))
                textView.setText(e);
            Log.d("RRR", textView.getText().toString());
            Log.d("RRR", galleryViewModel.getText().getValue());
        });
        textView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                galleryViewModel.getText().setValue(s.toString());
                Log.d("RRRR-", s.toString());
                Log.d("RRRR", textView.getText().toString());
                Log.d("RRRR", galleryViewModel.getText().getValue());
            }
        });
        galleryViewModel.addText(" t");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}