package com.example.gamewithdotz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameOver extends AppCompatActivity {

    String timeText;
    TextView scoreDisplay, timeDisplay;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove the title and make the window full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Apply the rounded background drawable to the window
        getWindow().setBackgroundDrawableResource(R.drawable.settings_bg);

        // Enable edge-to-edge content
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_DEFAULT);

        setContentView(R.layout.activity_game_over);

        View contentView = findViewById(R.id.gameOver);
        ViewCompat.setOnApplyWindowInsetsListener(contentView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);

        scoreDisplay = findViewById(R.id.ScoreDisplay);
        timeDisplay = findViewById(R.id.TimeDisplay);

        // Get the score and time from the intent
        score = getIntent().getIntExtra("score", 0);
        scoreDisplay.setText(String.valueOf(score));  // Display the score

        timeText = getIntent().getStringExtra("timeText");
        timeDisplay.setText(timeText);
    }

    private void saveScoreAndTime() {
        SharedPreferences prefs = getSharedPreferences("game_scores", MODE_PRIVATE);

        // Load existing scores
        int count = prefs.getInt("count", 0);
        List<ScoreEntry> scoreList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int savedScore = prefs.getInt("score_" + i, 0);
            String savedTime = prefs.getString("time_" + i, "00:00");
            scoreList.add(new ScoreEntry(savedScore, savedTime));
        }

        // Add the new score
        scoreList.add(new ScoreEntry(score, timeText));

        // Sort descending by score
        Collections.sort(scoreList, new Comparator<ScoreEntry>() {
            @Override
            public int compare(ScoreEntry o1, ScoreEntry o2) {
                return Integer.compare(o2.score, o1.score);
            }
        });

        // Keep only top 5
        if (scoreList.size() > 5) {
            scoreList = scoreList.subList(0, 5);
        }

        // Save back to SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putInt("count", scoreList.size());
        for (int i = 0; i < scoreList.size(); i++) {
            editor.putInt("score_" + i, scoreList.get(i).score);
            editor.putString("time_" + i, scoreList.get(i).time);
        }
        editor.apply();
    }

    // Helper class (same as in SeeScores)
    private static class ScoreEntry {
        int score;
        String time;

        ScoreEntry(int score, String time) {
            this.score = score;
            this.time = time;
        }
    }


    public void goMenu(View v) {
        saveScoreAndTime();  // Save current game data
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
