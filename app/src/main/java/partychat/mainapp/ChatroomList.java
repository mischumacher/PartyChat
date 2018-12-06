package partychat.mainapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.IOException;

public class ChatroomList extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File directory = getApplicationContext().getFilesDir();
        File file = new File(directory, "UserSettings.txt");
        setContentView(R.layout.activity_chatroom_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupRecyclerView(ChatroomAdapter.count);
        if(!file.exists()) {
            Intent intent = new Intent(this, FirstTimeSettings.class);
            startActivity(intent);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent myIntent = new Intent(this, SettingsPage.class);
            startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setupRecyclerView(int i) {

        recyclerView  = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ChatroomAdapter(i));
    }

    public void openCreateChatroom(View view){
        setupRecyclerView(ChatroomAdapter.count + 1);
    }

    public void openChatroom(View view){
        Intent intent = new Intent(this, ChatroomPage.class);
        startActivity(intent);
    }

    //Opens the settings page
}
