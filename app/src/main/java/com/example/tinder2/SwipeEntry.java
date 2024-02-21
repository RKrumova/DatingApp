package com.example.tinder2;

public class SwipeEntry {
    private String username;
    private String user2;
    private String swipeType;

    public SwipeEntry() {}
    public SwipeEntry(String username, String user2) {
        this.username = username;
        this.user2 = user2;
    }

    public SwipeEntry(String username, String user2, String swipeType) {
        this.username = username;
        this.user2 = user2;
        this.swipeType = swipeType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String newUser) {
        this.user2 = user2;
    }

    public String getSwipeType() {
        return swipeType;
    }

    public void setSwipeType(String swipeType) {
        this.swipeType = swipeType;
    }
}
