package ru.mirea.documenteditor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import ru.mirea.documenteditor.util.aes.Cipher;
import ru.mirea.documenteditor.util.aes.Type;

public class CipherTest {
    private Cipher cipher;

    @Before
    public void init() {
        cipher = new Cipher(Type.AES_256);
    }

    @Test
    public void TextShouldBeDecryptedEnglish() {
        String msg = "Testing cipher 123456789";

        String encodedString = cipher.encrypt(msg);
        String decodedString = cipher.decrypt(encodedString);

        assertEquals(msg, decodedString);
    }

    @Test
    public void TextShouldBeDecryptedRussian() {
        String msg = "Тестирование шифратора cipher 123456789";

        String encodedString = cipher.encrypt(msg);
        String decodedString = cipher.decrypt(encodedString);

        assertEquals(msg, decodedString);
    }

    @Test
    public void TextShouldBeEncryptedRussian() {
        String msg = "Тестирование шифратора cipher 123456789";
        String key = "NQNMBD12QmU9NxRhCAAEawdrBwYzcTp5R3tJZgV+Yn8=";
        Cipher cipherStatic = new Cipher(key);

        String encodedString = "RRf+fuxaopTJJ0obPVXgCqgq88eNdKcOmKAjUPtJ37vrzYsvYLv6nhgH1Zgl82XUvgSf5kSC/y/SJokk8lX7GDSgPpHWW+cT2jN6ZpLDxUU=";
        String decodedString = cipherStatic.decrypt(encodedString);

        assertEquals(msg, decodedString);
    }
}