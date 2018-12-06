package partychat.mainapp;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomAdapter.ChatroomViewHolder> {
    public static int count;
    public String chatName;
    public ArrayList<ChatroomObject> chatroomsList;
    public ChatroomAdapter(ArrayList<ChatroomObject> chatrooms) {
        chatroomsList = chatrooms;
    }

    @NonNull
    @Override
    public  ChatroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_listing, parent, false);
        return new ChatroomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatroomViewHolder chatroomViewHolder, int i) {
        //TODO change and set content on the listing
        chatroomViewHolder.chatName.setText(chatroomsList.get(i).getName());
        int implement = 0;
    }

    @Override
    public int getItemCount() {
        return chatroomsList.size();//TODO return proper count
    }

    protected static class ChatroomViewHolder extends RecyclerView.ViewHolder {
        //TODO declare variables for content
        TextView chatName;

        public ChatroomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.chatName = itemView.findViewById(R.id.ChatroomName);
            //TODO link variables to proper item
        }
    }


}
