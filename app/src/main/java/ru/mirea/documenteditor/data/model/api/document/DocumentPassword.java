package ru.mirea.documenteditor.data.model.api.document;

public class DocumentPassword {
    private Long documentId;
    private String password;
    private Long roleId;

    public DocumentPassword(Long documentId, String password, Long roleId) {
        this.documentId = documentId;
        this.password = password;
        this.roleId = roleId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
