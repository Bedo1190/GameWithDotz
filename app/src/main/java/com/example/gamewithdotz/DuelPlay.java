package com.example.gamewithdotz;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DuelPlay extends AppCompatActivity {
    String Player1Name;
    String Player2Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_duel_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Player1Name = getIntent().getStringExtra("Player1Name");
        Player2Name = getIntent().getStringExtra("Player2Name");
    }
    public void goBack(View v){
        finish();
    }

    public void timeSetGame(View v){
        Intent i = new Intent(getApplicationContext(),DuelTimeSettings.class);
        i.putExtra("Player1Name", Player1Name);
        i.putExtra("Player2Name", Player2Name);
        startActivity(i);

    }
    public void scoreSetGame(View v){
        Intent i = new Intent(getApplicationContext(),DuelScoreSettings.class);
        i.putExtra("Player1Name", Player1Name);
        i.putExtra("Player2Name", Player2Name);
        startActivity(i);
    }
}