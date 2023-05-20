package ru.mirea.documenteditor.data.model.api.user;

import java.util.List;

import ru.mirea.documenteditor.data.model.api.document.DocumentInfo;

public class UserIdInfo {
    private String username;
    private boolean me;
    private List<DocumentInfo> documents;
    private List<RoleInfo> allRoles;

    public UserIdInfo(String username, boolean me, List<DocumentInfo> documents, List<RoleInfo> allRoles) {
        this.username = username;
        this.me = me;
        this.documents = documents;
        this.allRoles = allRoles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }

    public List<DocumentInfo> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentInfo> documents) {
        this.documents = documents;
    }

    public List<RoleInfo> getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(List<RoleInfo> allRoles) {
        this.allRoles = allRoles;
    }
}
