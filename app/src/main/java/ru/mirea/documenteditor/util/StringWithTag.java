package ru.mirea.documenteditor.util;

import androidx.annotation.NonNull;

public class StringWithTag {
    public String string;
    public Object tag;

    public StringWithTag(String stringPart, Object tagPart) {
        string = stringPart;
        tag = tagPart;
    }

    @NonNull
    @Override
    public String toString() {
        return string;
    }
}