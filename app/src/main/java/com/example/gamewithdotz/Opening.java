package com.example.gamewithdotz;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class Opening extends AppCompatActivity {
    int delaystart= 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_opening);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.opening), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Start the fade-in animation immediately
        TextView textView = findViewById(R.id.textView1);
        TextView textView2 = findViewById(R.id.textView2);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f);
        ObjectAnimator fadeIn2 = ObjectAnimator.ofFloat(textView2, "alpha", 0f, 1f);
        fadeIn.setDuration(delaystart); // Duration in milliseconds (1.5 seconds)
        fadeIn2.setDuration(delaystart);
        fadeIn.start();
        fadeIn2.start();
        // Proceed to the next activity after the fade-in animation
        Handler handler = new Handler();
        Intent i = new Intent(this, MainActivity.class);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        }, delaystart);
    }
}
