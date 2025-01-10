package com.example.gamewithdotz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class aDot extends View {
    private MediaPlayer mediaPlayer;

    private Drawable dotDrawable;
    private GameWithDotz gameWithDotz;
    private UnlimitedGame unlimitedGame;
    private GameDotzDuel gameDotzDuel;
    private int middleHeight = -1;  // Default to -1, indicating not set

    // Constructor for GameWithDotz
    public aDot(Context context) {
        super(context);
        init(context);
    }

    // Constructor for GameDotzDuel with middleHeight parameter
    public aDot(Context context, int middleHeight) {
        super(context);
        this.middleHeight = middleHeight;
        init(context);
    }

    public aDot(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public aDot(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (context instanceof GameWithDotz) {
            gameWithDotz = (GameWithDotz) context;
        } else if (context instanceof GameDotzDuel) {
            gameDotzDuel = (GameDotzDuel) context;
        } else if (context instanceof UnlimitedGame) {
            unlimitedGame = (UnlimitedGame) context;
        }

        dotDrawable = ContextCompat.getDrawable(context, R.drawable.dot); // Load dot drawable
        setBackground(dotDrawable); // Set dot drawable as the background

        // Set click listener to remove DotView from parent
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create MediaPlayer instance to play the sound
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.videoplayback);
                mediaPlayer.start();

                // Handling the score increment and view removal
                if (getParent() instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) getParent();
                    if (gameWithDotz != null) {
                        gameWithDotz.delay= gameWithDotz.delay - 2;
                        gameWithDotz.newAppearDelay= gameWithDotz.newAppearDelay -2;
                        gameWithDotz.incrementScore();
                    }
                    if (gameDotzDuel != null) {
                        // Check the position of the dot relative to the middle height
                        if (getY() < middleHeight) {
                            gameDotzDuel.incrementScoreP2();
                        } else {
                            gameDotzDuel.incrementScoreP1();
                        }
                    }
                    if (unlimitedGame != null) {
                        unlimitedGame.delay= unlimitedGame.delay - 2;
                        unlimitedGame.newAppearDelay= unlimitedGame.newAppearDelay -2;
                        unlimitedGame.incrementScore();
                    }

                    parent.removeView(aDot.this);
                }

                // Release MediaPlayer resources when done playing
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });

                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        mp.release();
                        return false;
                    }
                });
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = dotDrawable.getIntrinsicWidth();
        int height = dotDrawable.getIntrinsicHeight();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        dotDrawable.setBounds(0, 0, getWidth(), getHeight());
        dotDrawable.draw(canvas);
    }
}
