package com.example.androidstudio2dgamedevelopment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.androidstudio2dgamedevelopment.audio.SoundManager;
import com.example.androidstudio2dgamedevelopment.gameobject.Circle;
import com.example.androidstudio2dgamedevelopment.gameobject.Enemy;
import com.example.androidstudio2dgamedevelopment.gameobject.Fireball;
import com.example.androidstudio2dgamedevelopment.gameobject.Freeze;
import com.example.androidstudio2dgamedevelopment.gameobject.HealthBoost;
import com.example.androidstudio2dgamedevelopment.gameobject.Item;
import com.example.androidstudio2dgamedevelopment.gameobject.Missile;
import com.example.androidstudio2dgamedevelopment.gameobject.Player;
import com.example.androidstudio2dgamedevelopment.gameobject.Shield;
import com.example.androidstudio2dgamedevelopment.gameobject.Spell;
import com.example.androidstudio2dgamedevelopment.gamepanel.Attack;
import com.example.androidstudio2dgamedevelopment.gamepanel.GameOver;
import com.example.androidstudio2dgamedevelopment.gamepanel.GameTimer;
import com.example.androidstudio2dgamedevelopment.gamepanel.Joystick;
import com.example.androidstudio2dgamedevelopment.gamepanel.Performance;
import com.example.androidstudio2dgamedevelopment.gamepanel.Score;
import com.example.androidstudio2dgamedevelopment.graphics.PlayerAnimator;
import com.example.androidstudio2dgamedevelopment.graphics.SimpleAnimator;
import com.example.androidstudio2dgamedevelopment.graphics.Sprite;
import com.example.androidstudio2dgamedevelopment.graphics.SpriteSheet;
import com.example.androidstudio2dgamedevelopment.map.Tilemap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Game manages all objects in the game and is responsible for updating all states and render all
 * objects to the screen
 */
class Game extends SurfaceView implements SurfaceHolder.Callback {
    private int screenWidth;
    private int screenHeight;
    private final Tilemap tilemap;
    private int joystickPointerId = 0;
    private final Joystick joystick;
    private final Attack attack; // Add attack button panel
    private final Player player;
    private GameLoop gameLoop;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private Sprite[] enemySprites;
    private List<Spell> spellList = new ArrayList<Spell>();
    private int normalSpellsToCast = 0; // Spells for main button
    private int fireballsToCast = 0;    // Spells for skill button 1
    private int missilesToCast = 0;   // Spells for skill button 2
    private GameOver gameOver;
    private Score score;
    private GameTimer gameTimer;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Performance performance;
    private GameDisplay gameDisplay;
    private SoundManager soundManager;

    // Item related variables
    private List<Item> itemList = new ArrayList<Item>();
    private Random random = new Random();
    private long nextItemSpawnTime = 0;
    private static final long ITEM_SPAWN_INTERVAL_MIN_MS = 1000; // Spawn item every 10-20 seconds
    private static final long ITEM_SPAWN_INTERVAL_MAX_MS = 5000;
    private static final double ITEM_RADIUS = 20; // Adjust as needed
    //Score
    private static final int ENEMY_SCORE = 10;
    private static final int ITEM_SCORE = 5;
    private boolean hasSavedScore = false;

    // Freeze related variables
    private boolean isEnemiesFrozen = false;
    private long freezeEndTimeMs = 0;

    public Game(Context context) {
        super(context);
        //get screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         screenWidth = displayMetrics.widthPixels;
         screenHeight = displayMetrics.heightPixels;
        // Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        // Initialize game panels
        performance = new Performance(context, gameLoop);
        score = new Score(context, screenWidth / 2 - 200, 100);
        gameTimer = new GameTimer( screenWidth / 2 + 200, 100);
        gameOver = new GameOver(context,screenWidth,screenHeight,score.getScore(), gameTimer.getElapsedTimeSeconds());
        joystick = new Joystick(275, 800, 125, 75);
        attack = new Attack(screenWidth - 275, 800); // Position attack button on the right

        // Initialize game objects
        SpriteSheet spriteSheet = new SpriteSheet(context);
        PlayerAnimator playerAnimator = new PlayerAnimator(spriteSheet.getPlayerSpriteArray());
        player = new Player(context, joystick, 2*500, 500, 32, playerAnimator);

        enemySprites = spriteSheet.getEnemySpriteArray();

        // Initialize display and center it around the player
        gameDisplay = new GameDisplay(screenWidth, screenHeight, player);

        // Initialize Tilemap
        tilemap = new Tilemap(context,spriteSheet);

        // Initialize SoundManager
        soundManager = new SoundManager(context);
        setFocusable(true);

        // Initialize next item spawn time
        setNextItemSpawnTime();
        // SharedPreferences để lưu High Score
        prefs = context.getSharedPreferences("HIGH_SCORES", Context.MODE_PRIVATE);
        editor = prefs.edit();
        //map looping for player
        player.setMapSize(tilemap.getMapWidth(), tilemap.getMapHeight());
    }

    private void setNextItemSpawnTime() {
        nextItemSpawnTime = System.currentTimeMillis() + ITEM_SPAWN_INTERVAL_MIN_MS +
                random.nextInt((int) (ITEM_SPAWN_INTERVAL_MAX_MS - ITEM_SPAWN_INTERVAL_MIN_MS + 1));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Game Over
        if (player.getHealthPoint() <= 0) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float x = event.getX();
                float y = event.getY();

                if (gameOver.isRestartPressed(x, y)) {
                    restartGame(); // viết hàm reset lại Player, Enemy, Score
                } else if (gameOver.isMenuPressed(x, y)) {
                    Intent intent = new Intent(getContext(), MenuActivity.class);
                    getContext().startActivity(intent);
                } else if (gameOver.isHighScorePressed(x, y)) {
                    Intent intent = new Intent(getContext(), HighScoreActivity.class);
                    getContext().startActivity(intent);
                }
            }
            return true;
        }
        // Handle user input touch event actions
        int action = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                // Check if a button is pressed
                if (joystick.isPressed(x, y)) {
                    joystickPointerId = pointerId;
                    joystick.setIsPressed(true);
                } else if (attack.isMainButtonPressed(x, y)) {
                    attack.setIsMainButtonPressed(true, pointerId);
                    normalSpellsToCast++;
                } else if (attack.isSkillButton1Pressed(x, y)) {
                    attack.setIsSkillButton1Pressed(true, pointerId);
                    fireballsToCast++;
                } else if (attack.isSkillButton2Pressed(x, y)) {
                    attack.setIsSkillButton2Pressed(true, pointerId);
                    missilesToCast++; // Logic for missile can be added here
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                // If joystick is pressed, update its actuator
                if (joystick.getIsPressed()) {
                    // Find the pointer associated with the joystick
                    for (int i = 0; i < event.getPointerCount(); i++) {
                        if (joystickPointerId == event.getPointerId(i)) {
                            joystick.setActuator(event.getX(i), event.getY(i));
                            break;
                        }
                    }
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                // Check which pointer was lifted
                if (joystickPointerId == pointerId) {
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                } else if (attack.getMainButtonPointerId() == pointerId) {
                    attack.setIsMainButtonPressed(false, -1);
                } else if (attack.getSkillButton1PointerId() == pointerId) {
                    attack.setIsSkillButton1Pressed(false, -1);
                } else if (attack.getSkillButton2PointerId() == pointerId) {
                    attack.setIsSkillButton2Pressed(false, -1);
                }
                return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("Game.java", "surfaceCreated()");
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            SurfaceHolder surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("Game.java", "surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("Game.java", "surfaceDestroyed()");
        if (soundManager != null) {
            soundManager.release();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Draw Tilemap
        tilemap.draw(canvas, gameDisplay);

        // Draw game objects
        player.draw(canvas, gameDisplay);

        for (Enemy enemy : enemyList) {
            enemy.draw(canvas, gameDisplay);
            enemy.drawLoop(canvas, gameDisplay, tilemap.getMapWidth(), tilemap.getMapHeight());
        }

        for (Spell spell : spellList) {
            spell.draw(canvas, gameDisplay);
        }

        // Draw items
        for (Item item : itemList) {
            item.draw(canvas, gameDisplay);
            item.drawLoop(canvas, gameDisplay, tilemap.getMapWidth(), tilemap.getMapHeight());
        }

        // Draw game panels
        joystick.draw(canvas);
        attack.draw(canvas); // Draw the attack buttons
        performance.draw(canvas);
        // Vẽ score + timer
        score.draw(canvas);
        gameTimer.draw(canvas);

        // Draw Game over if the player is dead
        if (player.getHealthPoint() <= 0) {
            // Cập nhật dữ liệu final cho GameOver
            gameOver.setFinalScore(score.getScore());
            gameOver.setFinalTime(gameTimer.getElapsedTimeSeconds());
            gameOver.draw(canvas);
        }
    }

    public void update() {
        // Stop updating the game if the player is dead
        if (player.getHealthPoint() <= 0) {
            if (!hasSavedScore) {
                // Lưu điểm 1 lần duy nhất
                saveHighScore(score.getScore(),gameTimer.getElapsedTimeSeconds());
                hasSavedScore = true;
            }
            return;
        }

        // Update game state
        joystick.update();
        player.update();
        gameTimer.update();

        // Spawn enemy
        if(Enemy.readyToSpawn()) {
            enemyList.add(new Enemy(getContext(), player,new SimpleAnimator(enemySprites, 5)));
        }

        // Spawn items
        if (System.currentTimeMillis() >= nextItemSpawnTime) {
            double mapWidth = tilemap.getMapWidth();
            double mapHeight = tilemap.getMapHeight();

            double itemX = random.nextDouble() * (mapWidth - 2 * ITEM_RADIUS) + ITEM_RADIUS;
            double itemY = random.nextDouble() * (mapHeight - 2 * ITEM_RADIUS) + ITEM_RADIUS;

            Item.ItemType type = Item.ItemType.getRandomType();
            Item newItem = null;

            switch (type) {
                case HEALTH_BOOST:
                    newItem = new HealthBoost(getContext(), itemX, itemY, ITEM_RADIUS);
                    break;
                case SHIELD:
                    newItem = new Shield(getContext(), itemX, itemY, ITEM_RADIUS);
                    break;
                case FREEZE:
                    newItem = new Freeze(getContext(), itemX, itemY, ITEM_RADIUS);
                    break;
            }

            if (newItem != null) {
                itemList.add(newItem);
            }

            setNextItemSpawnTime();
        }


        // Update states of all enemies
        for (Enemy enemy : enemyList) {
            enemy.update();
        }

        // Cast normal spells from the queue
        while (normalSpellsToCast > 0) {
            spellList.add(new Spell(getContext(), player));
            soundManager.play(SoundManager.SOUND_SHOOT);
            normalSpellsToCast--;
        }

        // Cast fireballs from the queue
        while (fireballsToCast > 0) {
            spellList.add(new Fireball(getContext(), player));
            soundManager.play(SoundManager.SOUND_SHOOT); // You can add a different sound
            fireballsToCast--;
        }

        // Cast missile from the queue
        while (missilesToCast > 0) {
            spellList.add(new Missile(getContext(), player));
            soundManager.play(SoundManager.SOUND_SHOOT); // You can add a different sound
            missilesToCast--;
        }

        // Update states of all spells
        for (Spell spell : spellList) {
            spell.update();
        }

        // Iterate through enemyList and Check for collision between each enemy and the player and
        // spells in spellList.
        Iterator<Enemy> iteratorEnemy = enemyList.iterator();
        while (iteratorEnemy.hasNext()) {
            Circle enemy = iteratorEnemy.next();
            if (Circle.isColliding(enemy, player)) {
                // Remove enemy if it collides with the player
                iteratorEnemy.remove();
                player.takeDamage(1);
                // Phát âm thanh bị hit
                soundManager.play(SoundManager.SOUND_HIT);
                continue;
            }

            Iterator<Spell> iteratorSpell = spellList.iterator();
            while (iteratorSpell.hasNext()) {
                Circle spell = iteratorSpell.next();
                // Remove enemy if it collides with a spell
                if (Circle.isColliding(spell, enemy)) {
                    iteratorSpell.remove();
                    iteratorEnemy.remove();
                    score.addScore(ENEMY_SCORE);
                    break;
                }
            }
        }

        // Iterate through itemList and check for collision between each item and the player
        // Update items and check collision with player
        Iterator<Item> it = itemList.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            if (!item.isCollected() && Circle.isColliding(item, player)) {
                item.applyEffect(player);
                score.addScore(ITEM_SCORE);
                it.remove(); // remove khỏi list sau khi collect
            }
        }

        // Update gameDisplay so that it's center is set to the new center of the player's
        // game coordinates
        gameDisplay.update();
    }
    public void restartGame() {
        // Reset Joystick
        joystick.resetActuator();
        joystick.setIsPressed(false);

        // Reset player về trạng thái ban đầu
        player.reset(2 * 500, 500);

        // Xoá kẻ địch và phép
        enemyList.clear();
        spellList.clear();
        normalSpellsToCast = 0;
        fireballsToCast = 0;
        missilesToCast = 0;
        itemList.clear(); // Clear all items

        // Cập nhật lại camera
        gameDisplay.update();

        // Reset score & timer
        score.reset();
        gameTimer.reset();

        // Reset item spawn timer
        setNextItemSpawnTime();
        // Reset freeze effect
        isEnemiesFrozen = false;
    }
    private void saveHighScore(int newScore, long playTimeMillis) {
        List<Integer> scores = new ArrayList<>();
        List<Long> times = new ArrayList<>();

        // Lấy danh sách cũ
        for (int i = 0; i < 6; i++) {
            int score = prefs.getInt("SCORE_" + i, -1);
            long time = prefs.getLong("TIME_" + i, -1);
            if (score >= 0 && time >= 0) {
                scores.add(score);
                times.add(time);
            }
        }

        // Thêm điểm + thời gian mới
        scores.add(newScore);
        times.add(playTimeMillis);

        // Sắp xếp giảm dần theo điểm
        List<Integer> sortedIndexes = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) sortedIndexes.add(i);
        sortedIndexes.sort((a, b) -> Integer.compare(scores.get(b), scores.get(a)));

        // Lưu top 6
        int size = Math.min(6, sortedIndexes.size());
        for (int i = 0; i < size; i++) {
            int idx = sortedIndexes.get(i);
            editor.putInt("SCORE_" + i, scores.get(idx));
            editor.putLong("TIME_" + i, times.get(idx));
        }

        // Xóa phần dư nếu có
        for (int i = size; i < 6; i++) {
            editor.remove("SCORE_" + i);
            editor.remove("TIME_" + i);
        }

        editor.apply();
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}