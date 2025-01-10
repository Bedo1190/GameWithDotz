package com.example.gamewithdotz;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class GameDotzDuel extends AppCompatActivity {
    String Player1Name;
    CardView cardView;
    MediaPlayer mediaPlayer;
    String Player2Name;
    long startTime;
    long elapsedTime;
    long elapsedSeconds;
    long secondsDisplay;
    long elapsedMinutes;
    String timeText;
    int settedTime;
    int settedScore;
    int score1, score2 = 0;
    TextView displayScore1, displayScore2;
    TextView timePanel1, timePanel2;
    Handler handler = new Handler();
    ConstraintLayout gdd;

    boolean scoreGameOverTriggered = false;

    int musicVolume; // To store the passed music volume

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_dotz_duel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.gdd), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get musicVolume from Intent
        musicVolume = getIntent().getIntExtra("musicVolume", 50); // Default to 50 if not set

        mediaPlayer = MediaPlayer.create(this, R.raw.gamewithdotztheme);
        mediaPlayer.setLooping(true);
        setMediaPlayerVolume(musicVolume);

        mediaPlayer.start();

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.release();
                return false;
            }
        });

        Player1Name = getIntent().getStringExtra("Player1Name");
        Player2Name = getIntent().getStringExtra("Player2Name");

        settedTime = getIntent().getIntExtra("settedTime", 30000000);
        settedScore = getIntent().getIntExtra("settedScore", 240);

        timePanel1 = findViewById(R.id.timePanel1);
        displayScore1 = findViewById(R.id.textView131);

        timePanel2 = findViewById(R.id.timePanel2);
        displayScore2 = findViewById(R.id.textView132);

        gdd = findViewById(R.id.gdd);

        startTime = System.currentTimeMillis();
        handler.post(updateTimeTask);

        handler.post(addDotsTask);
    }

    private void setMediaPlayerVolume(int volume) {
        float log1 = (float) (1 - (Math.log(101 - volume) / Math.log(101)));
        mediaPlayer.setVolume(log1, log1);
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
                duelGameOver();
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
        timePanel1.setText(timeText);
        timePanel2.setText(timeText);
    }

    public void duelGameOver() {
        mediaPlayer.stop();
        Intent i = new Intent(this, DuelGameOver.class);
        i.putExtra("score1", score1);
        i.putExtra("score2", score2);
        i.putExtra("Player1Name", Player1Name);
        i.putExtra("Player2Name", Player2Name);
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
            handler.postDelayed(this, 250);
        }
    };

    private void addViewAtRandomPosition() {
        if (scoreGameOverTriggered) {
            return;
        }

        // Get display metrics to get the screen dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Find the CardView and get its boundaries
        cardView = findViewById(R.id.DuelLabel);
        int[] location = new int[2];
        cardView.getLocationOnScreen(location);
        int cardViewTop = location[1];
        int cardViewBottom = cardViewTop + cardView.getHeight();

        specialDot dotView = new specialDot(this, screenHeight / 2);

        // Define the boundaries excluding the CardView
        int topBoundary = 200;
        int bottomBoundary = screenHeight - 200;

        // Measure the dot view to ensure correct width and height
        dotView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int dotWidth = dotView.getMeasuredWidth();
        int dotHeight = dotView.getMeasuredHeight();

        // Random position within the screen bounds
        Random random = new Random();
        int left = random.nextInt(screenWidth - dotWidth);
        int top;

        do {
            top = topBoundary + random.nextInt(bottomBoundary - topBoundary - dotHeight);
        } while (top > cardViewTop-dotHeight && top < cardViewBottom);

        // Set the position of the dot view
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.leftMargin = left;
        params.topMargin = top;
        dotView.setLayoutParams(params);

        // Add the dot view to the parent layout
        gdd.addView(dotView);

        // Set the dot's translationX and translationY to position it correctly
        dotView.setTranslationX(left);
        dotView.setTranslationY(top);

        // Schedule the removal of the dot after 1500 milliseconds
        handler.postDelayed(() -> {
            if (!scoreGameOverTriggered) {
                gdd.removeView(dotView);
            }
        }, 1500);

        // Check if the score has reached the settedScore and the game over flag is not triggered
        if (score1 >= settedScore && !scoreGameOverTriggered) {
            scoreGameOverTriggered = true;
            handler.removeCallbacks(addDotsTask);
            duelGameOver();
        }
        if (score2 >= settedScore && !scoreGameOverTriggered) {
            scoreGameOverTriggered = true;
            handler.removeCallbacks(addDotsTask);
            duelGameOver();
        }
    }

    public void incrementScoreP1() {
        incrementScoreP1By(1); // Default increment by 1
    }

    public void incrementScoreP1By(int increment) {
        score1 += increment;
        displayScore1.setText("Score: " + score1);
    }

    public void decrementScoreP1By(int decrement) {
        score1 -= decrement;
        displayScore1.setText("Score: " + score1);
    }

    public void incrementScoreP2() {
        incrementScoreP2By(1); // Default increment by 1
    }

    public void incrementScoreP2By(int increment) {
        score2 += increment;
        displayScore2.setText("Score: " + score2);
    }

    public void decrementScoreP2By(int decrement) {
        score2 -= decrement;
        displayScore2.setText("Score: " + score2);
    }
}
