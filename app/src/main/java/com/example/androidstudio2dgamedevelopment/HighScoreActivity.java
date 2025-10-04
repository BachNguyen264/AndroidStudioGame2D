package com.example.androidstudio2dgamedevelopment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgamedevelopment.audio.MusicManager;

import java.util.ArrayList;
import java.util.List;

public class HighScoreActivity extends AppCompatActivity {

    private static final int MAX_SCORES = 6; // Lưu tối đa 6 điểm cao nhất

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_high_score);

        LinearLayout scoresLayout = findViewById(R.id.scoresLinearLayout);
        Button backButton = findViewById(R.id.backButton);

        // Đọc danh sách điểm cao nhất từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("HIGH_SCORES", Context.MODE_PRIVATE);
        List<Integer> scores = new ArrayList<>();
        List<Long> times = new ArrayList<>();

        for (int i = 0; i < MAX_SCORES; i++) {
            int score = prefs.getInt("SCORE_" + i, -1);
            long time = prefs.getLong("TIME_" + i, -1);
            if (score >= 0 && time >= 0) {
                scores.add(score);
                times.add(time);
            }
        }

// Nếu chưa có điểm nào
        if (scores.isEmpty()) {
            TextView noScoreText = createScoreTextView("No scores yet!", 24);
            scoresLayout.addView(noScoreText);
        } else {
            // Hiển thị danh sách điểm + thời gian
            for (int i = 0; i < scores.size(); i++) {
                String formattedTime = formatTime(times.get(i));
                String scoreText = (i + 1) + ".  " + scores.get(i) + " pts - " + formattedTime;
                TextView scoreTextView = createScoreTextView(scoreText, 28);
                scoresLayout.addView(scoreTextView);
            }
        }


        // Nút quay về menu
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(HighScoreActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Tạo TextView cho mỗi dòng điểm
    private TextView createScoreTextView(String text, int textSize) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        textView.setTextSize(textSize);
        textView.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 8, 0, 8);
        textView.setLayoutParams(params);
        return textView;
    }
    // Format time từ giây sang mm:ss
    private String formatTime(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MusicManager.playBackgroundMusic(this, R.raw.bg_music);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MusicManager.pause(); // tránh leak khi app bị minimize
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        MusicManager.stop(); // giải phóng tài nguyên
//    }
}
