package com.example.tinder2.conversation;

public class ConversationList {
    private final String username;
    private final String message;
    private final String date;
    private final String time;

    public ConversationList(String username, String message, String date, String time) {
        this.username = username;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }


    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

}
