package partychat.mainapp;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomAdapter.ChatroomViewHolder> {
    public static int count;
    public ChatroomAdapter(int i) {
        count = i;
    }

    @NonNull
    @Override
    public  ChatroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_listing, parent, false);
        return new ChatroomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatroomViewHolder chatroomiewHolder, int i) {
        //TODO change and set content on the listing
        int implement = 0;
    }

    @Override
    public int getItemCount() {
        return count;//TODO return proper count
    }

    protected static class ChatroomViewHolder extends RecyclerView.ViewHolder {
        //TODO declare variables for content

        public ChatroomViewHolder(@NonNull View itemView) {
            super(itemView);
            //TODO link variables to proper item
        }
    }


}
