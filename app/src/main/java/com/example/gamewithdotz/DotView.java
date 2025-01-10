package com.example.gamewithdotz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class DotView extends View {
    private List<Dot> dots;
    private Drawable dotDrawable;
    private int dotSizePx;
    private int screenWidth;
    private int screenHeight;
    private Random random;

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DotView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        try {
            // Convert dp to pixels
            float density = context.getResources().getDisplayMetrics().density;
            dotSizePx = (int) (52 * density);

            // Load the dot drawable
            dotDrawable = context.getResources().getDrawable(R.drawable.dot, null);
            if (dotDrawable == null) {
                throw new IllegalArgumentException("Drawable resource not found");
            }

            // Get screen dimensions
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            screenWidth = displayMetrics.widthPixels;
            screenHeight = displayMetrics.heightPixels;

            // Initialize dots list
            dots = new ArrayList<>();

            // Initialize random generator
            random = new Random();

            // Generate random dots
            int numberOfDots = 3; // Change this to the number of dots you want to display
            for (int i = 0; i < numberOfDots; i++) {
                addNewDot();
            }
        } catch (Exception e) {
            Log.e("DotView", "Initialization error", e);
        }
    }

    private void addNewDot() {
        Dot newDot = new Dot();
        dots.add(newDot);
        newDot.scheduleUpdate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw each dot at its location
        Iterator<Dot> iterator = dots.iterator();
        while (iterator.hasNext()) {
            Dot dot = iterator.next();
            if (dot.isVisible()) {
                int x = dot.getLocX();
                int y = dot.getLocY();
                dotDrawable.setBounds(x, y, x + dotSizePx, y + dotSizePx);
                dotDrawable.draw(canvas);
            } else {
                iterator.remove();  // Remove the dot if it's not visible
            }
        }
    }

    public class Dot {
        private int locX;
        private int locY;
        private boolean visible;
        private Handler handler;
        private Runnable updateDotRunnable;

        public Dot() {
            handler = new Handler();
            locX = random.nextInt(screenWidth - dotSizePx);
            locY = random.nextInt(screenHeight - dotSizePx) +500;
            visible = true;
        }

        public int getLocX() {
            return locX;
        }

        public int getLocY() {
            return locY;
        }

        public boolean isVisible() {
            return visible;
        }

        public void scheduleUpdate() {
            updateDotRunnable = new Runnable() {
                @Override
                public void run() {
                    // Mark the dot as not visible
                    visible = false;
                    // Add a new dot
                    addNewDot();
                    // Redraw the view
                    invalidate();
                }
            };
            int delay = random.nextInt(1000) + 1000; // Random delay between 500ms to 1500ms
            handler.postDelayed(updateDotRunnable, delay);
        }

        public void resume() {
            handler.post(updateDotRunnable);
        }

        public void pause() {
            handler.removeCallbacks(updateDotRunnable);
        }
    }

    public void resume() {
        for (Dot dot : dots) {
            dot.resume();
        }
    }

    public void pause() {
        for (Dot dot : dots) {
            dot.pause();
        }
    }
}
