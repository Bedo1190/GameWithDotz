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

public class GameWithDotz extends AppCompatActivity {
    long startTime;
    long elapsedTime;
    long elapsedSeconds;
    long secondsDisplay;
    long elapsedMinutes;
    String timeText;
    int settedTime;
    int settedScore;
    int delay = 500;
    int newAppearDelay = 1500;
    int score;
    TextView displayScore;
    TextView timePanel;
    Handler handler = new Handler();
    ConstraintLayout gwd;

    boolean scoreGameOverTriggered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_with_dotz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.gwd), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        settedTime = getIntent().getIntExtra("settedTime", 30000000);
        settedScore = getIntent().getIntExtra("settedScore", 240);

        timePanel = findViewById(R.id.timePanel);
        displayScore = findViewById(R.id.textView13);
        gwd = findViewById(R.id.gwd);

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
            if (elapsedTime > settedTime) {
                handler.removeCallbacks(this);
                handler.removeCallbacks(addDotsTask);
                gameOver();
                return;
            }

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

    public void gameOver() {
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

        specialDot dotView = new specialDot(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        int topBoundary = 200;
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

        gwd.addView(dotView);

        dotView.setTranslationX(left);
        dotView.setTranslationY(top);

        handler.postDelayed(() -> {
            if (!scoreGameOverTriggered) {
                gwd.removeView(dotView);
            }
        }, newAppearDelay);

        if (score >= settedScore && !scoreGameOverTriggered) {
            scoreGameOverTriggered = true;
            handler.removeCallbacks(addDotsTask);
            gameOver();
        }
    }

    public void incrementScore() {
        score++;
        displayScore.setText("Score: " + score);
    }

    public void incrementScoreBy(int increment) {
        score += increment;
        displayScore.setText("Score: " + score);
    }

    public void decrementScoreBy(int decrement) {
        score -= decrement;
        displayScore.setText("Score: " + score);
    }
}
