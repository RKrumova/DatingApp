package com.example.tinder2.messages;

public class MessagesList {
    private String username, lastMessage, profilePic, convoKey;
    private int unseenMessages;

    public MessagesList(String username, String lastMessage,String profilePic, int unseenMessages, String convoKey) {
        this.username = username;
        this.lastMessage = lastMessage;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
        this.convoKey = convoKey;

    }
    public String getUsername() {
        return username;
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

    public String getConvoKey() {
        return convoKey;
    }

}
