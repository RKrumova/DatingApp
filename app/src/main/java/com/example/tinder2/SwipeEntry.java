package com.example.tinder2;

public class SwipeEntry {
    private String username;
    private String newUser;
    private String swipeType;

    public SwipeEntry() {

    }

    public SwipeEntry(String username, String newUser) {
        this.username = username;
        this.newUser = newUser;
    }

    public SwipeEntry(String username, String newUser, String swipeType) {
        this.username = username;
        this.newUser = newUser;
        this.swipeType = swipeType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewUser() {
        return newUser;
    }

    public void setNewUser(String newUser) {
        this.newUser = newUser;
    }

    public String getSwipeType() {
        return swipeType;
    }

    public void setSwipeType(String swipeType) {
        this.swipeType = swipeType;
    }
}
