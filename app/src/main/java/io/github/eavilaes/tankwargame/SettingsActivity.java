package io.github.eavilaes.tankwargame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private static SharedPreferences sharedPreferences;
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Enable fullscreen to hide status bar.
        //Action bar is already hidden because of the app's theme.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("SETTINGS_FILE", MODE_PRIVATE);
        username = findViewById(R.id.nameEditText);
        String username_local = sharedPreferences.getString("username_local", "Player");
        username.setText(username_local);
    }

    public void saveSettings(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username_local", username.getText().toString());
        editor.apply();
        Intent ret = new Intent();
        ret.putExtra("newname", username.getText().toString());
        setResult(RESULT_OK, ret);
        finish();
    }
}
