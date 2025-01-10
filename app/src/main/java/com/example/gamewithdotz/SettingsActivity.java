package com.example.gamewithdotz;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity {

    ImageButton muteBtn;
    ImageButton musicBtn;
    SeekBar soundBar;
    SeekBar musicBar;
    int volume;
    int prevVolume;
    int musicVolume;
    int prevMusicVolume; // Track previous music volume for unmute
    Drawable musicIcon;
    Drawable musicOffIcon;
    Drawable muteIcon;
    Drawable muteOffIcon;
    boolean isMuted = false; // Keep track of sound mute state
    boolean isMusicMuted = false; // Keep track of music mute state

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Settings), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("SettingsPreferences", MODE_PRIVATE);

        soundBar = findViewById(R.id.Sound);
        musicBar = findViewById(R.id.Music);
        muteBtn = findViewById(R.id.muteBtn);
        musicBtn = findViewById(R.id.musicBtn);

        musicIcon = ContextCompat.getDrawable(this, R.drawable.musicicon2);
        musicOffIcon = ContextCompat.getDrawable(this, R.drawable.musicofficon2);

        muteIcon = ContextCompat.getDrawable(this, R.drawable.muteicon1);
        muteOffIcon = ContextCompat.getDrawable(this, R.drawable.soundicon1);

        // Restore saved state
        prevVolume = sharedPreferences.getInt("prevVolume", 50); // Default to 50 if not set
        isMuted = sharedPreferences.getBoolean("isMuted", false);
        musicVolume = sharedPreferences.getInt("musicVolume", 50); // Default to 50 if not set
        prevMusicVolume = sharedPreferences.getInt("prevMusicVolume", 50); // Default to 50 if not set
        isMusicMuted = sharedPreferences.getBoolean("isMusicMuted", false);

        // Set the progress of the soundBar and musicBar
        soundBar.setProgress(isMuted ? 0 : prevVolume);
        musicBar.setProgress(isMusicMuted ? 0 : musicVolume);
        muteBtn.setImageDrawable(isMuted ? muteIcon : muteOffIcon);
        musicBtn.setImageDrawable(isMusicMuted ? musicOffIcon : musicIcon);

        // Set listener for sound seek bar change
        soundBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    muteBtn.setImageDrawable(muteIcon);
                    isMuted = true;
                } else {
                    muteBtn.setImageDrawable(muteOffIcon);
                    isMuted = false;
                }
                volume = soundBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No action needed when touch starts
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                volume = soundBar.getProgress();
                if (!isMuted) {
                    prevVolume = volume; // Update prevVolume if not muted
                }
                // Save the volume to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("prevVolume", prevVolume);
                editor.putBoolean("isMuted", isMuted);
                editor.apply();
            }
        });

        // Set listener for music seek bar change
        musicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    musicBtn.setImageDrawable(musicOffIcon);
                    isMusicMuted = true;
                } else {
                    musicBtn.setImageDrawable(musicIcon);
                    isMusicMuted = false;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No action needed when touch starts
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicVolume = musicBar.getProgress();
                if (!isMusicMuted) {
                    prevMusicVolume = musicVolume; // Update prevMusicVolume if not muted
                }
                // Save the music volume to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("musicVolume", musicVolume);
                editor.putInt("prevMusicVolume", prevMusicVolume);
                editor.putBoolean("isMusicMuted", isMusicMuted);
                editor.apply();
            }
        });

        muteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMuted) {
                    muteBtn.setImageDrawable(muteOffIcon);
                    soundBar.setProgress(prevVolume);
                    volume = soundBar.getProgress();
                    isMuted = false;
                } else {
                    muteBtn.setImageDrawable(muteIcon);
                    soundBar.setProgress(0);
                    volume = soundBar.getProgress();
                    isMuted = true;
                }
// Save the mute state to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isMuted", isMuted);
                editor.apply();
            }
        });

        musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMusicMuted) {
                    musicBtn.setImageDrawable(musicIcon);
                    musicBar.setProgress(prevMusicVolume);
                    isMusicMuted = false;
                } else {
                    musicBtn.setImageDrawable(musicOffIcon);
                    musicBar.setProgress(0);
                    isMusicMuted = true;
                }
                // Save the music mute state to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isMusicMuted", isMusicMuted);
                editor.apply();
            }
        });
    }
    public void goBack(View v){
        finish();
    }

}