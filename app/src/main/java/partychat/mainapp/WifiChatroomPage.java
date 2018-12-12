package partychat.mainapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WifiChatroomPage extends AppCompatActivity {
    private ListView listView;
    private TextInputLayout inputLayout;
    private ArrayAdapter<String> chatAdapter;
    private ArrayList<String> chatMessages;
    private ChatroomObject currentChatroom = null;
    private Integer currentChatroomIndex = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_chatroom_page);
        Intent intent = getIntent();
        if (intent.hasExtra("chatName")) {
            String name = intent.getStringExtra("chatName");
            //finding the alarm being edited
            for (int i = 0; i < ChatroomObject.getChatrooms().size(); i++) {
                if (ChatroomObject.getChatrooms().get(i).getName().equals(name)) {
                    currentChatroom = ChatroomObject.getChatrooms().get(i);
                    currentChatroomIndex = i;
                }
            }
        }
        findViewsByIds();

        //set chat adapter
        chatMessages = currentChatroom.getMessages();
        chatAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatMessages);
        listView.setAdapter(chatAdapter);
    }

    private void findViewsByIds() {
        listView = (ListView) findViewById(R.id.wifi_list);
        inputLayout = (TextInputLayout) findViewById(R.id.wifi_input_layout);
        View btnSend = findViewById(R.id.wifi_btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputLayout.getEditText().getText().toString().equals("")) {
                    Toast.makeText(WifiChatroomPage.this, "Please input some texts", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO: here
                    sendMessage(inputLayout.getEditText().getText().toString());
                    inputLayout.getEditText().setText("");
                }
            }
        });
    }

    private void sendMessage(String message) {
        currentChatroom.appendMessage(ChatroomList.owner, message);
        ChatroomList.saveFile(getApplicationContext());
        chatAdapter.notifyDataSetChanged();
    }
}
