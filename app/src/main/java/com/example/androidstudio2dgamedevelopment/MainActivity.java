package com.example.androidstudio2dgamedevelopment;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.androidstudio2dgamedevelopment.audio.MusicManager;

/**
 * MainActivity is the entry point to our application.
 */
public class MainActivity extends Activity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity.java", "onCreate()");
        super.onCreate(savedInstanceState);

        // Set content view to game, so that objects in the Game class can be rendered to the screen
        game = new Game(this);
        setContentView(game);
    }

    @Override
    protected void onStart() {
        Log.d("MainActivity.java", "onStart()");
        MusicManager.playBackgroundMusic(this, R.raw.bg_music);
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("MainActivity.java", "onResume()");
        //game.resumeGame();
        MusicManager.playBackgroundMusic(this, R.raw.bg_music);
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("MainActivity.java", "onPause()");
        game.pause();
        MusicManager.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("MainActivity.java", "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivity.java", "onDestroy()");
        MusicManager.stop();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // Comment out "super.onBackPressed()" to disable button
        //super.onBackPressed();
    }
}
