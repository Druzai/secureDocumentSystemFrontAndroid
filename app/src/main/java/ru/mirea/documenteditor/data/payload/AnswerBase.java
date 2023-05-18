package ru.mirea.documenteditor.data.payload;

import java.util.Map;

public class AnswerBase<T> {
    private String error;
    private Map<String, T> result;

    public AnswerBase(String error, Map<String, T> result) {
        this.error = error;
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public Map<String, T> getResult() {
        return result;
    }
}
