package com.example.gamewithdotz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class ScoreSettings extends Activity {

    private TextView scoreGoal;
    int settedScore;
    private SeekBar score;
    private int[] snapPoints = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100}; // Example snap points

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

        setContentView(R.layout.activity_score_settings);

        View contentView = findViewById(R.id.scoreSet);
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

        score = findViewById(R.id.Score);
        scoreGoal = findViewById(R.id.scoreGoal);

        // Set listener for seek bar change
        score.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // No need to handle progress changed here for snapping
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No action needed when touch starts
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Snap to the nearest snap point
                int nearestSnapPoint = findNearestSnapPoint(seekBar.getProgress());
                seekBar.setProgress(nearestSnapPoint);
                scoreGoal.setText(String.valueOf(nearestSnapPoint)); // Convert integer to String
            }
        });
    }

    private int findNearestSnapPoint(int progress) {
        int nearest = snapPoints[0];
        int distance = Math.abs(progress - snapPoints[0]);
        for (int i = 1; i < snapPoints.length; i++) {
            int newDistance = Math.abs(progress - snapPoints[i]);
            if (newDistance < distance) {
                nearest = snapPoints[i];
                distance = newDistance;
            }
        }
        return nearest;
    }

    public void playWithScore(View v) {
        scoreGoal = findViewById(R.id.scoreGoal);
        if (scoreGoal.getText().toString().equals("...")) {
            Toast.makeText(this, "Please set a score limit!", Toast.LENGTH_SHORT).show();
        } else {
            settedScore = score.getProgress();
            Intent i = new Intent(this, GameWithDotz.class);
            i.putExtra("settedScore", settedScore );  // Pass the score to the GameWithDots activity
            startActivity(i);
        }
    }
}
