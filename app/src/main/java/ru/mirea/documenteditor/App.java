package ru.mirea.documenteditor;

import android.app.Application;

import ru.mirea.documenteditor.util.PreferenceManager;
import ru.mirea.documenteditor.util.RetrofitManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.getInstance().init(getApplicationContext());
        RetrofitManager.getInstance().init();
    }
}