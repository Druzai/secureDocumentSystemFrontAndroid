package ru.mirea.documenteditor.ui.fragments.auth;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.mirea.documenteditor.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    public FragmentRegisterBinding binding;

    public interface OnDataPass{
        void onRegisterDataPass(String username, String password, String passwordConfirm);
    }

    OnDataPass dataPasser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    public void passRegisterData(String username, String password, String passwordConfirm){
        dataPasser.onRegisterDataPass(username, password, passwordConfirm);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.btRegister.setOnClickListener(v -> {
            collectRegisterData();
        });

        super.onViewCreated(view, savedInstanceState);
    }

    public void setLoading(){
        binding.btRegister.setVisibility(View.INVISIBLE);
        binding.pbLoadingRegistration.setVisibility(View.VISIBLE);
    }

    public void disableLoading(){
        binding.btRegister.setVisibility(View.VISIBLE);
        binding.pbLoadingRegistration.setVisibility(View.INVISIBLE);
    }

    private void collectRegisterData() {
        String username = binding.etRegisterUsername.getText().toString();
        String password = binding.etRegisterPassword.getText().toString();
        String passwordConfirm = binding.etRegisterPasswordConfirm.getText().toString();

        passRegisterData(username, password, passwordConfirm);
    }
}