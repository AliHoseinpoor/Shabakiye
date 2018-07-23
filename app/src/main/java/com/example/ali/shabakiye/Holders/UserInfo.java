package com.example.ali.shabakiye.Holders;

/**
 * Created by ali on 7/14/18.
 */

public class UserInfo {
    private User info;

    public UserInfo(String result, User user) {
        this.info = user;
    }

    public User getUser() {
        return info;
    }
}
