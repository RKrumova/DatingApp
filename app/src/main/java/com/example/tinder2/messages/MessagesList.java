package com.example.tinder2.messages;

public class MessagesList {
    private final String username;
    private String lastMessage;
    private String profilePic;
    private final String convoKey;
    private String user_2;
    private int unseenMessages;

//    public MessagesList(String username, String lastMessage,String profilePic, int unseenMessages, String convoKey) {
//        this.username = username;
//        this.lastMessage = lastMessage;
//        this.profilePic = profilePic;
//        this.unseenMessages = unseenMessages;
//        this.convoKey = convoKey;
//
//    }

    public MessagesList(String username, String lastMessage, int unseenMessages, String profilePic,  String convoKey) {
        this.username = username;
        this.lastMessage = lastMessage;
        this.unseenMessages = unseenMessages;
        this.profilePic = profilePic;
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
    public String getUser_2() {
        return user_2;
    }

    public void setUser_2(String user_2) {
        this.user_2 = user_2;
    }
}
