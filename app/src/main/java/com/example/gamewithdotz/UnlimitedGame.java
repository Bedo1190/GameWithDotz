package com.example.gamewithdotz;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class UnlimitedGame extends AppCompatActivity {
    long startTime;
    long elapsedTime;
    long elapsedSeconds;
    long secondsDisplay;
    long elapsedMinutes;
    String timeText;
    int delay = 500;
    int newAppearDelay = 1500;
    int score;
    TextView displayScore;
    TextView timePanel;
    Handler handler = new Handler();
    ConstraintLayout ulg;

    boolean scoreGameOverTriggered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_unlimited_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ulg), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        timePanel = findViewById(R.id.timePanel);
        displayScore = findViewById(R.id.textView13);
        ulg = findViewById(R.id.ulg);

        startTime = System.currentTimeMillis();
        handler.post(updateTimeTask);

        handler.post(addDotsTask);
    }

    private final Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            if (scoreGameOverTriggered) {
                return;
            }

            elapsedTime = System.currentTimeMillis() - startTime;

            elapsedSeconds = elapsedTime / 1000;
            secondsDisplay = elapsedSeconds % 60;
            elapsedMinutes = elapsedSeconds / 60;
            showTime();

            handler.postDelayed(this, 1000);
        }
    };

    public void showTime() {
        timeText = elapsedMinutes + ":" + (secondsDisplay < 10 ? "0" + secondsDisplay : secondsDisplay);
        timePanel.setText(timeText);
    }

    public void gameOver(View v) {
        handler.removeCallbacks(updateTimeTask);
        handler.removeCallbacks(addDotsTask);
        Intent i = new Intent(this, GameOver.class);
        i.putExtra("score", score);
        i.putExtra("timeText", timeText);
        startActivity(i);
    }

    private final Runnable addDotsTask = new Runnable() {
        @Override
        public void run() {
            if (scoreGameOverTriggered) {
                return;
            }

            addViewAtRandomPosition();
            handler.postDelayed(this, delay);
        }
    };

    private void addViewAtRandomPosition() {
        if (scoreGameOverTriggered) {
            return;
        }

        aDot dotView = new aDot(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        int topBoundary = 250;
        int bottomBoundary = screenHeight - 200;

        dotView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int dotWidth = dotView.getMeasuredWidth();
        int dotHeight = dotView.getMeasuredHeight();

        Random random = new Random();
        int left = random.nextInt(screenWidth - dotWidth);
        int top = topBoundary + random.nextInt(bottomBoundary - topBoundary - dotHeight);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.leftMargin = left;
        params.topMargin = top;
        dotView.setLayoutParams(params);

        ulg.addView(dotView);

        dotView.setTranslationX(left);
        dotView.setTranslationY(top);

        handler.postDelayed(() -> {
            if (!scoreGameOverTriggered) {
                ulg.removeView(dotView);
            }
        }, newAppearDelay);

    }

    public void incrementScore() {
        score++;
        displayScore.setText("Score: " + score);
    }
}
