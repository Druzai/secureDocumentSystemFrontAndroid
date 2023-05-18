package ru.mirea.documenteditor.ui.fragments.myProfile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.documenteditor.databinding.FragmentMyProfileBinding;

public class MyProfileFragment extends Fragment {

    private FragmentMyProfileBinding binding;
    private MyProfileViewModel myProfileViewModel;

    private TextView usernameTextView;
    private TextView rolesTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myProfileViewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);
        myProfileViewModel.init();

        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        usernameTextView = binding.textUsername;
        rolesTextView = binding.textRoles;
        myProfileViewModel.getUsernameText().observe(getViewLifecycleOwner(), usernameTextView::setText);
        myProfileViewModel.getRolesText().observe(getViewLifecycleOwner(), rolesTextView::setText);

        myProfileViewModel.getUserInfo();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}