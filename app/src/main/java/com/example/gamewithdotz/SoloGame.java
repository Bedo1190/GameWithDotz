package com.example.gamewithdotz;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class SoloGame extends AppCompatActivity {

    int clickno=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_solo_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.soloGame), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void goBack(View v){
        finish();
    }

    public void timeSetGame(View v){
        Intent i = new Intent(getApplicationContext(),TimeSettings.class);
        startActivity(i);

    }
    public void scoreSetGame(View v){
        Intent i = new Intent(getApplicationContext(),ScoreSettings.class);
        startActivity(i);
    }

    public void unlimited(View v){
        if (clickno==6){
            Intent i = new Intent(this,UnlimitedGame.class);
            startActivity(i);
        }else{
            clickno++;
        }

    }

}