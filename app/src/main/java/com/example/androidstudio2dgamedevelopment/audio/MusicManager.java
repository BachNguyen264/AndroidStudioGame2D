package com.example.androidstudio2dgamedevelopment.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;

public class MusicManager {
    private static MediaPlayer mediaPlayer;

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
            mediaPlayer.setVolume(1.0f, 1.0f); // âm lượng 100%
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
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(left, right);
        }
    }
}

