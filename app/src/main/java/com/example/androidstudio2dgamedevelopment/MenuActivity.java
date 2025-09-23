package com.example.androidstudio2dgamedevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidstudio2dgamedevelopment.audio.MusicManager;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window  window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_main_menu);

        Button btnPlay = findViewById(R.id.btnPlay);

        // Sự kiện bấm nút Play
        btnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(com.example.androidstudio2dgamedevelopment.MenuActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicManager.playBackgroundMusic(this, R.raw.bg_music);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicManager.pause(); // tránh leak khi app bị minimize
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicManager.stop(); // giải phóng tài nguyên
    }

}
