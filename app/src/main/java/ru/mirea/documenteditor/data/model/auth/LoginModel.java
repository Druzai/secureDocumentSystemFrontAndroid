package ru.mirea.documenteditor.data.model.auth;


public class LoginModel {

    private String username, password;
    private boolean isLoginValid;

    public LoginModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getIsLoginValid() {
        return isLoginValid;
    }

    public void setIsLoginValid(boolean loginValid) {
        isLoginValid = loginValid;
    }
}
