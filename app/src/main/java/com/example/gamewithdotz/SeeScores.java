package com.example.gamewithdotz;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SeeScores extends AppCompatActivity {

    TextView score1, score2, score3, score4, score5;
    TextView time1, time2, time3, time4, time5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_scores);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.seeScores), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Link TextViews with layout
        score1 = findViewById(R.id.score1);
        score2 = findViewById(R.id.score2);
        score3 = findViewById(R.id.score3);
        score4 = findViewById(R.id.score4);
        score5 = findViewById(R.id.score5);

        time1 = findViewById(R.id.time1);
        time2 = findViewById(R.id.time2);
        time3 = findViewById(R.id.time3);
        time4 = findViewById(R.id.time4);
        time5 = findViewById(R.id.time5);

        // Load scores and display
        loadScores();
    }

    private void loadScores() {
        SharedPreferences prefs = getSharedPreferences("game_scores", MODE_PRIVATE);

        int count = prefs.getInt("count", 0);

        // Load all saved scores into a list
        List<ScoreEntry> scoreList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int savedScore = prefs.getInt("score_" + i, 0);
            String savedTime = prefs.getString("time_" + i, "00:00");
            scoreList.add(new ScoreEntry(savedScore, savedTime));
        }

        // Sort scores in descending order
        Collections.sort(scoreList, new Comparator<ScoreEntry>() {
            @Override
            public int compare(ScoreEntry o1, ScoreEntry o2) {
                return Integer.compare(o2.score, o1.score); // Descending
            }
        });

        // Keep only top 5 scores
        if (scoreList.size() > 5) {
            scoreList = scoreList.subList(0, 5);
        }

        // Save back trimmed & sorted scores
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putInt("count", scoreList.size());
        for (int i = 0; i < scoreList.size(); i++) {
            editor.putInt("score_" + i, scoreList.get(i).score);
            editor.putString("time_" + i, scoreList.get(i).time);
        }
        editor.apply();

        // Display scores
        for (int i = 0; i < scoreList.size(); i++) {
            ScoreEntry entry = scoreList.get(i);
            switch (i) {
                case 0:
                    score1.setText("Score: " + entry.score);
                    time1.setText("Time: " + entry.time);
                    break;
                case 1:
                    score2.setText("Score: " + entry.score);
                    time2.setText("Time: " + entry.time);
                    break;
                case 2:
                    score3.setText("Score: " + entry.score);
                    time3.setText("Time: " + entry.time);
                    break;
                case 3:
                    score4.setText("Score: " + entry.score);
                    time4.setText("Time: " + entry.time);
                    break;
                case 4:
                    score5.setText("Score: " + entry.score);
                    time5.setText("Time: " + entry.time);
                    break;
            }
        }
    }

    public void goBack(View v) {
        finish();
    }

    // Helper class for scores
    private static class ScoreEntry {
        int score;
        String time;

        ScoreEntry(int score, String time) {
            this.score = score;
            this.time = time;
        }
    }
}
