package com.example.androidstudio2dgamedevelopment.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;

public class MusicManager {
    private static MediaPlayer mediaPlayer;
    private static float bgmVolume = 1.0f; // mặc định 100%

    public static void playBackgroundMusic(Context context, int resId) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, resId);
            mediaPlayer.setLooping(true); // lặp vô hạn
            // 👇 set usage là GAME thay vì MUSIC
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
            );
            setVolume(bgmVolume, bgmVolume); // áp dụng volume hiện tại
            mediaPlayer.start();
        } else if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public static void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static void setVolume(float left, float right) {
        bgmVolume = (left + right) / 2; // lưu lại để còn hiển thị trong seekbar
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(left, right);
        }
    }
    public static float getVolume() {
        return bgmVolume;
    }
}

