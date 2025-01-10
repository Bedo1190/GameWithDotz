package com.example.gamewithdotz;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

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

public class TimeSettings extends Activity {

    private TextView timeGoal;
    private SeekBar time;
    int settedTime;
    private int[] snapPoints = {30, 50, 70, 90, 110,}; // Example snap points


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

        setContentView(R.layout.activity_time_settings);

        View contentView = findViewById(R.id.timeSet);
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

        time = findViewById(R.id.time);
        timeGoal = findViewById(R.id.timeGoal);

        // Set listener for seek bar change
        time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                timeGoal.setText(String.valueOf(nearestSnapPoint) + " s"); // Convert integer to String
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

    public void playWithTime(View v) {
        timeGoal = findViewById(R.id.timeGoal);
        if (timeGoal.getText().toString().equals("...")) {
            Toast.makeText(this, "Please set a time limit!", Toast.LENGTH_SHORT).show();
        } else {
            settedTime = time.getProgress() * 1000;
            Intent i = new Intent(this, GameWithDotz.class);
            i.putExtra("settedTime", settedTime );  // Pass the time to the GameWithDots activity
            startActivity(i);
        }
    }

}