package ru.mirea.documenteditor.ui.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.documenteditor.R;
import ru.mirea.documenteditor.data.model.auth.LoginModel;
import ru.mirea.documenteditor.data.model.auth.RegisterModel;
import ru.mirea.documenteditor.databinding.ActivityAuthBinding;
import ru.mirea.documenteditor.ui.activities.main.MainActivity;
import ru.mirea.documenteditor.ui.fragments.auth.LoginFragment;
import ru.mirea.documenteditor.ui.fragments.auth.RegisterFragment;


public class AuthActivity extends AppCompatActivity implements RegisterFragment.OnDataPass, LoginFragment.OnDataPass {

    private FragmentTransaction ft;
    private ActivityAuthBinding binding;
    private AuthActivityViewModel authActivityViewModel;

    private boolean passwordDontMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authActivityViewModel = new ViewModelProvider(this).get(AuthActivityViewModel.class);
        authActivityViewModel.init();

        passwordDontMatch = false;

        setBinding();
        setObservers();
    }

    private void setBinding() {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_auth, new LoginFragment(), "LoginFragment");
        ft.commit();

        binding.btMovingButton.setOnClickListener(view -> {
            String directionOfAnimation = binding.btMovingButton.getText().toString();
            switchLoginRegistration(directionOfAnimation);
        });
    }

    private void setObservers() {
        authActivityViewModel.getIsRegisterLoading().observe(this, this::loadingRegistration);
        authActivityViewModel.getIsLoginLoading().observe(this, this::loadingLogin);
        authActivityViewModel.getLoginModel().observe(this, this::handleLogin);
        authActivityViewModel.getRegisterModel().observe(this, this::handleRegister);
    }

    private void registerNewUser(String username, String password, String passwordConfirm) {
        authActivityViewModel.registerUser(username, password, passwordConfirm);
    }

    private void loginUser(String username, String password) {
        authActivityViewModel.loginUser(username, password);
    }

    private void handleRegister(RegisterModel registerModel) {
        if (registerModel.getIsRegisterValid() && !passwordDontMatch) {
            switchLoginRegistration("Вход");
        } else if (passwordDontMatch) {
            Toast.makeText(this, "Пароли не совпадают!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ошибка при регистрации!", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLogin(LoginModel loginModel) {
        if (loginModel.getIsLoginValid()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Ошибка при входе!", Toast.LENGTH_SHORT).show();
        }
    }

    private void switchLoginRegistration(String key) {
        if (key.equals("Вход")) {
            binding.btMovingButton.setText("Регистрация");
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fl_auth, new LoginFragment(), "LoginFragment");
            ft.commit();
        } else {
            binding.btMovingButton.setText("Вход");
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fl_auth, new RegisterFragment(), "RegisterFragment");
            ft.commit();
        }
    }

    private void loadingRegistration(boolean isLoading) {
        RegisterFragment fragment = (RegisterFragment) getSupportFragmentManager().findFragmentByTag("RegisterFragment");
        if (isLoading) {
            fragment.setLoading();
        } else {
            fragment.disableLoading();
        }
    }

    private void loadingLogin(boolean isLoading) {
        LoginFragment fragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("LoginFragment");
        if (isLoading) {
            fragment.setLoading();
        } else {
            fragment.disableLoading();
        }
    }

    @Override
    public void onRegisterDataPass(String username, String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)){
            passwordDontMatch = true;
            authActivityViewModel.getRegisterModel().setValue(new RegisterModel(username, password, ""));
        } else {
            passwordDontMatch = false;
            registerNewUser(username, password, passwordConfirm);
        }
    }

    @Override
    public void onLoginDataPass(String username, String password) {
        loginUser(username, password);
    }
}