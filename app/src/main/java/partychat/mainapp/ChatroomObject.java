package partychat.mainapp;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatroomObject implements Serializable {
    public static ArrayList<ChatroomObject> chatroomsList;
    private String owner;
    private String chatName;
    private ArrayList<String> messages;
    private String filepath;

    public ChatroomObject(String owner, String chatName){
        this.owner = owner;
        this.chatName = chatName;
        messages = new ArrayList<String>();
        filepath = chatName + ".txt";
        chatroomsList.add(this);
    }

    public String getOwner(){
        return owner;
    }

    public void setOwner(String owner){
        this.owner = owner;
    }

    public String getName(){
        return chatName;
    }

    public void setName(String chatName){
        this.chatName = chatName;
    }

    public void appendMessage(String owner, String message){
        messages.add(owner + ": " + message);
    }

    public ArrayList<String> getMessages(){
        return messages;
    }

    public String getFilepath(){
        return filepath;
    }


    public static ArrayList<ChatroomObject> getChatrooms(){
        if(chatroomsList == null)
            chatroomsList = new ArrayList<ChatroomObject>();
        return chatroomsList;
    }


}
