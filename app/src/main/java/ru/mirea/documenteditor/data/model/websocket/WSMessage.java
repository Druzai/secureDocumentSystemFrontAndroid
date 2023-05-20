package ru.mirea.documenteditor.data.model.websocket;

import java.util.ArrayList;

public class WSMessage {
    private Integer documentId;
    private String fromUser;
    private String command;
    private ArrayList<WSContent> content;

    public WSMessage(Integer documentId, String fromUser, String command, ArrayList<WSContent> content) {
        this.documentId = documentId;
        this.fromUser = fromUser;
        this.command = command;
        this.content = content;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public ArrayList<WSContent> getContent() {
        return content;
    }

    public void setContent(ArrayList<WSContent> content) {
        this.content = content;
    }
}

