package com.example.tinder2.messages;

public class MessagesList {
    private String username, email, lastMessage, profilePic;
    private int unseenMessages;

    public MessagesList(String username, String email, String lastMessage,String profilePic, int unseenMessages) {
        this.username = username;
        this.email = email;
        this.lastMessage = lastMessage;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }
}
