package ru.mirea.documenteditor.data.payload;

public class DocumentRight {
    private Long documentId;
    private Long roleId;
    private Long userId;

    public DocumentRight(Long documentId, Long roleId, Long userId){
        this.documentId = documentId;
        this.roleId = roleId;
        this.userId = userId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
