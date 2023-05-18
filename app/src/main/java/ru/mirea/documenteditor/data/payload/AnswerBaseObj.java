package ru.mirea.documenteditor.data.payload;

import java.util.Map;

public class AnswerBaseObj<T> {
    private String error;
    private T result;

    public AnswerBaseObj(String error, T result) {
        this.error = error;
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public T getResult() {
        return result;
    }
}
