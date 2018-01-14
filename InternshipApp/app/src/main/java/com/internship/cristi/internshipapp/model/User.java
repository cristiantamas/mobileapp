package com.internship.cristi.internshipapp.model;

/**
 * Created by cristi on 12/17/17.
 */

public class User {
    String username;
    String password;


    public User(){}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
