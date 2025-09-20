package com.example.androidstudio2dgamedevelopment.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.example.androidstudio2dgamedevelopment.R;

public class SoundManager {
    private SoundPool soundPool;
    private SparseIntArray soundMap;  // lưu id -> soundID
    private Context context;

    public static final int SOUND_SHOOT = 1;
    public static final int SOUND_HIT = 2;
    // sau này có thể thêm SOUND_EXPLOSION, SOUND_JUMP, SOUND_RELOAD,...

    public SoundManager(Context context) {
        this.context = context;
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5) // tối đa 5 âm thanh phát đồng thời
                .build();
        soundMap = new SparseIntArray();
        loadSounds();
    }

    private void loadSounds() {
        // load file .wav/.mp3 trong res/raw/
        soundMap.put(SOUND_SHOOT, soundPool.load(context, R.raw.shoot, 1));
        soundMap.put(SOUND_HIT, soundPool.load(context, R.raw.hit, 1));
    }

    public void play(int soundType) {
        int soundId = soundMap.get(soundType);
        if (soundId != 0) {
            soundPool.play(soundId, 1, 1, 1, 0, 1);
        }
    }

    public void release() {
        soundPool.release();
        soundPool = null;
    }
}
