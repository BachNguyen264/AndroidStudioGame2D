package com.example.androidstudio2dgamedevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidstudio2dgamedevelopment.audio.MusicManager;
import com.example.androidstudio2dgamedevelopment.audio.SoundManager;

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
        // Nút Setting
        Button btnSetting = findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(v -> showSettingDialog());
        //Nút HighScore
        Button btnHighScore = findViewById(R.id.btnHighScore);
        btnHighScore.setOnClickListener(v -> {
            Intent intent = new Intent(com.example.androidstudio2dgamedevelopment.MenuActivity.this, HighScoreActivity.class);
            startActivity(intent);
        });
    }
    private void showSettingDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_setting, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();

        // --- BGM SeekBar ---
        SeekBar seekBGM = dialogView.findViewById(R.id.seekBGM);
        // Lấy volume hiện tại từ MusicManager (mặc định 100%)
        seekBGM.setProgress((int)(MusicManager.getVolume() * 100));
        seekBGM.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                MusicManager.setVolume(volume, volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // --- SFX SeekBar ---
        SeekBar seekSFX = dialogView.findViewById(R.id.seekSFX);
        seekSFX.setProgress((int) (SoundManager.getSfxVolume() * 100));
        seekSFX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                SoundManager.setSfxVolume(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Resume button
        Button btnResume = dialogView.findViewById(R.id.btnResume);
        btnResume.setOnClickListener(v -> dialog.dismiss());

        // Exit button
        Button btnExit = dialogView.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> {
            dialog.dismiss();
            finish(); // thoát menu
        });

        dialog.show();
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
        if (isFinishing() && isTaskRoot()) {
            MusicManager.stop();
        }
    }

}
