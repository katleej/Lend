package com.example.lend;

import java.util.ArrayList;

public class Message {
    public String ID;
    public ArrayList<String> usersID;
    public ArrayList<String[]> messageList = new ArrayList<String[]>();

    public void addMessage(String message, String userid) {
        String[] oneMessage = new String[2];
        oneMessage[0] = message;
        oneMessage[1] = userid;
        messageList.add(oneMessage);
    }

    public ArrayList<String[]> getMessageList() {
        return messageList;
    }

    public void setUsersID(ArrayList<String> usersid) {
        usersID = usersid;
    }

    public ArrayList<String> getUsersID() {
        return usersID;
    }


}
