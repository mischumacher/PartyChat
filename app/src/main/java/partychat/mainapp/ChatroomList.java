package partychat.mainapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ChatroomList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private boolean isWifiP2pEnabled = false;
    String name = "";
    public static String owner = null;
    public static String ID = null;

    //WIFI stuff
    final IntentFilter intentFilter = new IntentFilter();
    WifiP2pManager.Channel mChannel;
    WifiP2pManager mManager;
    WifiReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File directory = getApplicationContext().getFilesDir();
        File file = new File(directory, "UserSettings.txt");


        File file2 = new File(directory, "Chatrooms.txt");
        try {
            file2.createNewFile();
            ChatroomObject.chatroomsList = getSavedChatrooms(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_chatroom_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.menu_spinner_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner

        //WIFI stuff
        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = (WifiP2pManager) getSystemService(getApplicationContext().WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        receiver = new WifiReceiver(mManager, mChannel, this);
        registerReceiver(receiver, intentFilter);



        setupRecyclerView();

        //First time info
        if(!file.exists()) {
            Intent intent = new Intent(this, FirstTimeSettings.class);
            startActivity(intent);
        }
        else{
            InputStream fis = null;
            String temp = null;
            try {
                // below true flag tells OutputStream to append
                fis = new FileInputStream(file);
                int content;
                while ((content = fis.read()) != -1) {
                    // convert to char and display it
                    temp += (char) content;
                }
                int colonIndex = temp.indexOf(":");
                owner = temp.substring(0, colonIndex);
                ID = temp.substring(colonIndex + 1, temp.length() - 1);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chatroom_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId())
        {
            case R.id.action_settings:
            {
                // Here you can set your intent and start the activity
                Intent myIntent = new Intent(this, SettingsPage.class);
                startActivity(myIntent);
                return true;
            }

            case R.id.bluetooth_chat_start:
            {

                Intent intent = new Intent(this, ChatroomPage.class);
                startActivity(intent);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void setupRecyclerView() {

        recyclerView  = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(!(name == null))
            recyclerView.setAdapter(new ChatroomAdapter(ChatroomObject.getChatrooms()));
    }

    public void openCreateChatroom(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chatroom Name");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = input.getText().toString();
                ChatroomObject chatroom = new ChatroomObject("ME", name);//TODO replace me with chatroom owner
                setupRecyclerView();
                saveFile(getApplicationContext());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void openWifiChatroom(View view){
        Intent intent = new Intent(this, WifiChatroomPage.class);
        String name = ((TextView)view.findViewById(R.id.ChatroomName)).getText().toString();
        intent.putExtra("chatName", name);
        startActivity(intent);
    }

    public void connect(View view) {
        // Picking the first device found on the network.
        List<WifiP2pDevice> peers = WifiReceiver.getPeers();
        if (peers.size() == 0){
            Toast.makeText(getApplicationContext(), "Connect failed. Retry.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        WifiP2pDevice device = peers.get(0);

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver notifies us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(getApplicationContext(), "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void resetData(){
        String filler;
    }

    public static void saveFile(Context context) {

        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput("Chatrooms.txt", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);

            for (ChatroomObject c : ChatroomObject.getChatrooms()) {
                os.writeObject(c);
            }

            os.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ChatroomObject> getSavedChatrooms(Context context) throws IOException, ClassNotFoundException {
        ArrayList<ChatroomObject> tempRooms = new ArrayList<ChatroomObject>();
        File directory = context.getFilesDir();
        File file = new File(directory, "Chatrooms.txt");
        Boolean keepGoing = true;
        FileInputStream fi = null;
        ObjectInputStream oi = null;
        try {
            fi = new FileInputStream(file);
            oi = new ObjectInputStream(fi);
            while (keepGoing) {
                tempRooms.add((ChatroomObject) oi.readObject());
            }
        } catch (EOFException e) {
            keepGoing = false;
        }
        if (fi != null)
            fi.close();
        if (oi != null)
            oi.close();


        return tempRooms;
    }

    public void onDelete(View view){
        String name = ((TextView)((LinearLayout)view.getParent()).findViewById(R.id.ChatroomName)).getText().toString();
        for(int i = 0; i < ChatroomObject.chatroomsList.size(); i++){
            if(ChatroomObject.chatroomsList.get(i).getName().equals(name)){
                ChatroomObject.chatroomsList.remove(i);
                break;
            }
        }
        saveFile(getApplicationContext());
        setupRecyclerView();
    }

}
