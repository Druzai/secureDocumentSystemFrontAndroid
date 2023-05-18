package ru.mirea.documenteditor.data.payload;

import java.util.List;

public class MyUserInfo {
    private String username;
    private List<RoleInfo> myRoles;
    private List<RoleInfo> allRoles;

    public MyUserInfo(String username, List<RoleInfo> myRoles, List<RoleInfo> allRoles) {
        this.username = username;
        this.myRoles = myRoles;
        this.allRoles = allRoles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<RoleInfo> getMyRoles() {
        return myRoles;
    }

    public void setMyRoles(List<RoleInfo> myRoles) {
        this.myRoles = myRoles;
    }

    public List<RoleInfo> getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(List<RoleInfo> allRoles) {
        this.allRoles = allRoles;
    }
}
