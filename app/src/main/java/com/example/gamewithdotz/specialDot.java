package com.example.gamewithdotz;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class specialDot extends View {
    private MediaPlayer mediaPlayer;
    private final int MAX_VOLUME = 101;
    private Drawable currentDrawable;
    private GameWithDotz gameWithDotz;
    private GameDotzDuel gameDotzDuel;
    private int middleHeight = -1;
    private int dotNumber;
    private Paint textPaint;

    public specialDot(Context context) {
        super(context);
        init(context);
    }

    public specialDot(Context context, int middleHeight) {
        super(context);
        this.middleHeight = middleHeight;
        init(context);
    }

    public specialDot(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public specialDot(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (context instanceof GameWithDotz) {
            gameWithDotz = (GameWithDotz) context;
        } else if (context instanceof GameDotzDuel) {
            gameDotzDuel = (GameDotzDuel) context;
        }

        Drawable dotDrawableGreen = ContextCompat.getDrawable(context, R.drawable.specialdot_green);
        Drawable dotDrawableBlue = ContextCompat.getDrawable(context, R.drawable.specialdot_blue);
        Drawable dotDrawableRed = ContextCompat.getDrawable(context, R.drawable.specialdot_red);
        Drawable dotDrawable = ContextCompat.getDrawable(context, R.drawable.dot);

        Random random = new Random();
        dotNumber = random.nextInt(10) + 1;

        if (gameDotzDuel != null) {
            Drawable[] drawables = {dotDrawableGreen, dotDrawableRed, dotDrawable, dotDrawable, dotDrawable, dotDrawable, dotDrawable, dotDrawable, dotDrawable, dotDrawable, dotDrawable, dotDrawable, dotDrawableBlue};
            currentDrawable = drawables[random.nextInt(drawables.length)];
        } else {
            Drawable[] drawables = {dotDrawableGreen, dotDrawableGreen, dotDrawableRed, dotDrawableRed, dotDrawable, dotDrawable, dotDrawable, dotDrawable, dotDrawable, dotDrawable};
            currentDrawable = drawables[random.nextInt(drawables.length)];
        }

        setBackground(currentDrawable);
        if (currentDrawable == dotDrawableGreen) {
            setTag("green");
        } else if (currentDrawable == dotDrawableRed) {
            setTag("red");
        } else if (currentDrawable == dotDrawableBlue) {
            setTag("blue");
        } else {
            setTag("normal");
        }

        textPaint = new Paint();
        textPaint.setColor(getResources().getColor(android.R.color.black));
        textPaint.setTypeface(Typeface.create("@font/leaguespartan", Typeface.BOLD));
        textPaint.setTextSize(25 * getResources().getDisplayMetrics().density);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Retrieve saved volume from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("SettingsPreferences", Context.MODE_PRIVATE);
        int savedVolume = sharedPreferences.getInt("prevVolume", 50); // Default to 50 if not set
        final float volume;
        if (savedVolume == 0) {
            volume = 0;
        } else {
            volume = (float) (1 - (Math.log(MAX_VOLUME - savedVolume) / Math.log(MAX_VOLUME)));
        }
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.videoplayback);
                mediaPlayer.setVolume(volume, volume);
                mediaPlayer.start();

                if (getParent() instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) getParent();

                    if (gameWithDotz != null) {
                        adjustScoreForGameWithDotz();
                    }
                    if (gameDotzDuel != null) {
                        adjustScoreForGameDotzDuel();
                    }
                    parent.removeView(specialDot.this);
                }

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

    private void adjustScoreForGameWithDotz() {
        String tag = (String) getTag();
        if ("green".equals(tag)) {
            gameWithDotz.incrementScoreBy(dotNumber);
        } else if ("red".equals(tag)) {
            gameWithDotz.decrementScoreBy(dotNumber);
        } else if ("normal".equals(tag)) {
            gameWithDotz.incrementScore();
        }
        if(gameWithDotz.delay >= 200 || gameWithDotz.newAppearDelay >= 1200) {
            gameWithDotz.delay -= 2;
            gameWithDotz.newAppearDelay -= 2;
        }
    }

    private void adjustScoreForGameDotzDuel() {
        String tag = (String) getTag();
        if (getY() < middleHeight) {
            if ("green".equals(tag)) {
                gameDotzDuel.incrementScoreP2By(dotNumber);
            } else if ("red".equals(tag)) {
                gameDotzDuel.decrementScoreP2By(dotNumber);
            } else if ("normal".equals(tag)) {
                gameDotzDuel.incrementScoreP2();
            } else if ("blue".equals(tag)) {
                gameDotzDuel.decrementScoreP1By(dotNumber);
            }
        } else {
            if ("green".equals(tag)) {
                gameDotzDuel.incrementScoreP1By(dotNumber);
            } else if ("red".equals(tag)) {
                gameDotzDuel.decrementScoreP1By(dotNumber);
            } else if ("normal".equals(tag)) {
                gameDotzDuel.incrementScoreP1();
            } else if ("blue".equals(tag)) {
                gameDotzDuel.decrementScoreP2By(dotNumber);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = currentDrawable.getIntrinsicWidth();
        int height = currentDrawable.getIntrinsicHeight();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String tag = (String) getTag();
        super.onDraw(canvas);
        currentDrawable.setBounds(0, 0, getWidth(), getHeight());
        currentDrawable.draw(canvas);
        int xPos = getWidth() / 2;
        int yPos = (int) ((getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        if (!tag.equals("normal")) {
            if (getY() < middleHeight) {
                canvas.save();
                canvas.rotate(180, xPos, yPos - 23);
                canvas.drawText(String.valueOf(dotNumber), xPos, yPos, textPaint);
                canvas.restore();
            } else {
                canvas.drawText(String.valueOf(dotNumber), xPos, yPos, textPaint);
            }
        }
    }
}