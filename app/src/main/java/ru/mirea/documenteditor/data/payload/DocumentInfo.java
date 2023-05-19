package ru.mirea.documenteditor.data.payload;

public class DocumentInfo {
    private Long id;
    private String name;
    private String lastEditBy;
    private UserInfo owner;

    public DocumentInfo(Long id, String name, String lastEditBy, UserInfo owner) {
        this.id = id;
        this.name = name;
        this.lastEditBy = lastEditBy;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastEditBy() {
        return lastEditBy;
    }

    public void setLastEditBy(String lastEditBy) {
        this.lastEditBy = lastEditBy;
    }

    public UserInfo getOwner() {
        return owner;
    }

    public void setOwner(UserInfo owner) {
        this.owner = owner;
    }
}
