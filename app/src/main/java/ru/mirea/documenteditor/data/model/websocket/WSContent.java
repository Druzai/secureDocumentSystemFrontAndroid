package ru.mirea.documenteditor.data.model.websocket;

public class WSContent {
    private Integer number;
    private String data;
    private String align;

    public WSContent(Integer number, String data, String align) {
        this.number = number;
        this.data = data;
        this.align = align;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }
}