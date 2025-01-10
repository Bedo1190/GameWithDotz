package com.example.gamewithdotz;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class DotzDuel extends AppCompatActivity {

    String Player1Name, Player2Name;
    EditText player1, player2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dotz_duel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dotzduel), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goBack(View v) {
        finish();
    }

    public void duelPlay(View v) {
        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);
        Player1Name = player1.getText().toString(); // Get text as String
        Player2Name = player2.getText().toString(); // Get text as String
        Intent i = new Intent(this, DuelPlay.class);
        i.putExtra("Player1Name", Player1Name);
        i.putExtra("Player2Name", Player2Name);
        startActivity(i);
    }
}
