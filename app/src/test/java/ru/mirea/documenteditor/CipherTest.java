package ru.mirea.documenteditor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import ru.mirea.documenteditor.util.aes.Cipher;
import ru.mirea.documenteditor.util.aes.Type;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
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
}