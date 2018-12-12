package partychat.mainapp;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SettingsPage extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Window w = getWindow();
        w.setTitle("Settings");
    }
    public void saveSettings(View view){

        TextInputEditText userNameField = findViewById(R.id.UsernameText);
        String username = userNameField.getText().toString();
        Toast.makeText(getApplicationContext(), username,
                Toast.LENGTH_SHORT).show();
        ChatroomList.owner = username;
        String userID = Long.toString(System.currentTimeMillis()) + username;
        ChatroomList.ID = userID;

        File directory = getApplicationContext().getFilesDir();
        File file = new File(directory, "UserSettings.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutputStream os = null;
        try {
            // below true flag tells OutputStream to append
            os = new FileOutputStream(file, true);
            os.write(username.getBytes(), 0, username.length());
            os.write(":".getBytes(), 0, ":".length());
            os.write(userID.getBytes(), 0, userID.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
