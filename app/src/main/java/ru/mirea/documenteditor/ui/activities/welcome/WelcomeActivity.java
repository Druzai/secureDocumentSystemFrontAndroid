package ru.mirea.documenteditor.ui.activities.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.documenteditor.databinding.ActivityWelcomeBinding;
import ru.mirea.documenteditor.ui.activities.main.MainActivity;
import ru.mirea.documenteditor.ui.activities.auth.AuthActivity;


public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;
    private WelcomeActivityViewModel welcomeActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        welcomeActivityViewModel = new ViewModelProvider(this).get(WelcomeActivityViewModel.class);
        welcomeActivityViewModel.init();
        setObservers();
    }

    private void setObservers() {
        welcomeActivityViewModel.getIsSignedIn().observe(this, isSignedIn -> {
            if (isSignedIn) {
                welcomeActivityViewModel.getGotUserKey().observe(WelcomeActivity.this, gotUserKey -> {
                    if (!gotUserKey){
                        Toast.makeText(getApplicationContext(), "Ключ шифрования пользователя не был получен!", Toast.LENGTH_LONG).show();
                    }
                    startMainActivity();
                });
                welcomeActivityViewModel.fetchUserKey();
            } else {
                startAuthActivity();
            }
        });
    }

    private void startAuthActivity() {
        Intent registerLoginIntent = new Intent(this, AuthActivity.class);
        startActivity(registerLoginIntent);
    }

    private void startMainActivity() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }
}