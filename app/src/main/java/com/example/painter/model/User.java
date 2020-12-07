package com.example.painter.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String login;
    private String password;


    public User() {
        setLogin("Guest");
        setPassword("");
    }

    @Ignore
    public User(String login, String password) {
        setPassword(password);
        setLogin(login);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if(this.id == 0) {
            this.id = id;
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
