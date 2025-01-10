package com.example.gamewithdotz;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.View;

public class SeeScores extends AppCompatActivity {

    int score,score1,score2,score3,score4,score5;
    String timeText,timeText1,timeText2,timeText3,timeText4,timeText5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_scores);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.seeScores), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        score = getIntent().getIntExtra("score", 0);
        timeText = getIntent().getStringExtra("timeText");



    }




    public void goBack(View v){
        finish();
    }
}