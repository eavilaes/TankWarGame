package io.github.eavilaes.tankwargame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class GameSettingsActivity extends AppCompatActivity {

    private static final int GAME_TIME_MAX_MINUTES = 10;
    private static final int GAME_SCORE_MAX = 15;
    private static SharedPreferences sharedPreferences;
    TextView timeTextView, scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_settings);

        timeTextView = findViewById(R.id.textView_time);
        scoreTextView = findViewById(R.id.textView_score);

        sharedPreferences = getSharedPreferences("SETTINGS_FILE", MODE_PRIVATE);

        timeTextView.setText(String.valueOf(sharedPreferences.getInt("game_time", 3)));
        scoreTextView.setText(String.valueOf(sharedPreferences.getInt("game_score", 10)));

    }

    public void playGameLocal(View view) {
        //Save settings in shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("game_time", Integer.parseInt(timeTextView.getText().toString()));
        editor.putInt("game_score", Integer.parseInt(scoreTextView.getText().toString()));
        editor.apply();

        Intent game = new Intent(this, LocalGameActivity.class);
        startActivity(game);
    }

    //Decreases the max time if it is >1
    public void decreaseTime(View view) {
        if(!timeTextView.getText().toString().equals("Unlimited"))
            if (Integer.parseInt(timeTextView.getText().toString()) > 1)
                timeTextView.setText(String.valueOf(Integer.parseInt(timeTextView.getText().toString()) - 1));
            else
                timeTextView.setText(R.string.unlimited);
        else
            timeTextView.setText(String.valueOf(GAME_TIME_MAX_MINUTES));
    }

    public void increaseTime(View view) {
        if(!timeTextView.getText().toString().equals("Unlimited"))
            if(Integer.parseInt(timeTextView.getText().toString()) < GAME_TIME_MAX_MINUTES)
                timeTextView.setText(String.valueOf(Integer.parseInt(timeTextView.getText().toString()) +1));
            else
                timeTextView.setText(R.string.unlimited);
        else
            timeTextView.setText(String.valueOf(1));
    }

    public void decreaseMaxScore(View view) {
        if(!scoreTextView.getText().toString().equals("Unlimited"))
            if(Integer.parseInt(scoreTextView.getText().toString()) > 1)
                scoreTextView.setText(String.valueOf(Integer.parseInt(scoreTextView.getText().toString()) -1));
            else
                scoreTextView.setText(R.string.unlimited);
        else
            scoreTextView.setText(String.valueOf(GAME_SCORE_MAX));
    }

    public void increaseMaxScore(View view) {
        if(!scoreTextView.getText().toString().equals("Unlimited"))
            if(Integer.parseInt(scoreTextView.getText().toString()) < GAME_SCORE_MAX)
                scoreTextView.setText(String.valueOf(Integer.parseInt(scoreTextView.getText().toString()) +1));
            else
                scoreTextView.setText(R.string.unlimited);
        else
            scoreTextView.setText(String.valueOf(1));
    }
}
