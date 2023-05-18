package ru.mirea.documenteditor.ui.fragments.auth;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.mirea.documenteditor.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    public interface OnDataPass {
        void onLoginDataPass(String username, String password);
    }

    OnDataPass dataPasser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    public void passLoginData(String username, String password) {
        dataPasser.onLoginDataPass(username, password);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.btLogin.setOnClickListener(v -> passLoginData());
        super.onViewCreated(view, savedInstanceState);
    }

    public void setLoading() {
        binding.btLogin.setVisibility(View.INVISIBLE);
        binding.pbLoadingLogin.setVisibility(View.VISIBLE);
    }

    public void disableLoading() {
        binding.btLogin.setVisibility(View.VISIBLE);
        binding.pbLoadingLogin.setVisibility(View.INVISIBLE);
    }

    private void passLoginData() {
        String username = binding.etLoginUsername.getText().toString();
        String password = binding.etLoginPassword.getText().toString();
        passLoginData(username, password);
    }
}