package ru.mirea.documenteditor.util;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import ru.mirea.documenteditor.util.aes.Cipher;

public class CipherManager {
    private Map<String, Cipher> cipherMap;

    private static CipherManager instance;

    public static CipherManager getInstance() {
        if (instance == null) {
            instance = new CipherManager();
        }
        return instance;
    }

    public void init() {
        cipherMap = new HashMap<>();
    }

    public Cipher getCipher(String key) {
        if (!cipherMap.containsKey(key)) {
            Cipher cipher = new Cipher(key);
            cipherMap.put(key, cipher);
        }
        return cipherMap.get(key);
    }

    public boolean deleteCipher(String key) {
        try {
            cipherMap.remove(key);
            return true;
        } catch (RuntimeException e) {
            Log.e(Constants.LOG_TAG, e.getMessage());
            return false;
        }
    }

    public boolean isKeyValid(String key){
        try {
            new Cipher(key);
            return true;
        } catch (RuntimeException e){
            return false;
        }
    }
}
