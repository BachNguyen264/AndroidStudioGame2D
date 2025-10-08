package com.example.androidstudio2dgamedevelopment.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.example.androidstudio2dgamedevelopment.R;

public class SoundManager {
    private static SoundManager instance;
    private SoundPool soundPool;
    private SparseIntArray soundMap;  // lưu id -> soundID
    private Context context;
    // static volume chung cho toàn bộ game
    private static float sfxVolume = 1.0f;
    public static final int SOUND_SHOOT = 1;
    public static final int SOUND_HIT = 2;
    public static final int SOUND_COLLECT = 3;
    public static final int SOUND_FIREBALL = 4;
    public static final int SOUND_MISSILE = 5;
    // sau này có thể thêm SOUND_EXPLOSION, SOUND_JUMP, SOUND_RELOAD,...
    // Private constructor (chỉ dùng qua getInstance)
    private SoundManager(Context context) {
        this.context = context.getApplicationContext(); // tránh leak
        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build())
                .build();
        soundMap = new SparseIntArray();
        loadSounds();
    }
    // Singleton getter
    public static synchronized SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context);
        }
        return instance;
    }

    private void loadSounds() {
        // load file .wav/.mp3 trong res/raw/
        soundMap.put(SOUND_SHOOT, soundPool.load(context, R.raw.shoot, 1));
        soundMap.put(SOUND_HIT, soundPool.load(context, R.raw.hit, 1));
        soundMap.put(SOUND_COLLECT, soundPool.load(context, R.raw.collect_item, 1));
        soundMap.put(SOUND_MISSILE, soundPool.load(context, R.raw.missile, 1));
        soundMap.put(SOUND_FIREBALL, soundPool.load(context, R.raw.fire_ball, 1));
    }

    public void play(int soundType) {
        if (sfxVolume <= 0f) return;  // nếu mute → bỏ qua
        int soundId = soundMap.get(soundType);
        if (soundId != 0) {
            soundPool.play(soundId, sfxVolume, sfxVolume, 1, 0, 1f);
        }
    }

    // static getter & setter
    public static void setSfxVolume(float volume) {
        sfxVolume = volume;
    }

    public static float getSfxVolume() {
        return sfxVolume;
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        instance = null; // reset để lần sau tạo lại
    }
}
