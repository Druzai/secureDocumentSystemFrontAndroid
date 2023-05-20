package ru.mirea.documenteditor.data.model.api.cipher;

public class InputMessage {
    private String text;
    private boolean toEncode;

    public InputMessage(String text, boolean toEncode) {
        this.text = text;
        this.toEncode = toEncode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isToEncode() {
        return toEncode;
    }

    public void setToEncode(boolean toEncode) {
        this.toEncode = toEncode;
    }
}
