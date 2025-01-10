package com.example.gamewithdotz;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private DotView dotView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dotView = findViewById(R.id.dotView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dotView != null) {
            dotView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dotView != null) {
            dotView.pause();
        }
    }

    public void openSettings(View v){
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }
    public void howToPlay(View v){
        Intent i = new Intent(this, HowToPlay.class);
        startActivity(i);
    }
    public void soloPlay(View v){
        Intent i = new Intent(this, SoloGame.class);
        startActivity(i);
    }
    public void dotzDuel(View v){
        Intent i = new Intent(this, DotzDuel.class);
        startActivity(i);
    }
    public void seeScores(View v){
        Intent i = new Intent(this, SeeScores.class);
        startActivity(i);
    }


}