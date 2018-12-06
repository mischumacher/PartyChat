package partychat.mainapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FirstTimeSettings extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_time_settings);
        Window w = getWindow();
        w.setTitle("First Time Settings");
        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveSettings(v);

                Intent intent = new Intent(FirstTimeSettings.this, ChatroomList.class);
                startActivity(intent);
            }
        });
    }

    public void saveSettings(View view){

        TextInputEditText userNameField = findViewById(R.id.UsernameText);
        String username = userNameField.getText().toString();
        //TODO check for safe input
        String userID = Long.toString(System.currentTimeMillis()) + username;
        //TODO store theme selection

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
