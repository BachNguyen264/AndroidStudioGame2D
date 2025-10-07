package com.example.androidstudio2dgamedevelopment;

public class DifficultyManager {
    private static final int DIFFICULTY_INTERVAL_SEC = 60; // tăng độ khó mỗi 60 giây

    private double spawnMultiplier = 1.0;  // ảnh hưởng tốc độ spawn
    private double speedMultiplier = 1.0;  // ảnh hưởng tốc độ di chuyển
    private double healthMultiplier = 1.0; // ảnh hưởng máu enemy

    private long lastUpdateTime = 0;

    public void update(long elapsedTimeSeconds) {
        // Mỗi phút tăng độ khó 1 lần
        if (elapsedTimeSeconds / DIFFICULTY_INTERVAL_SEC > lastUpdateTime) {
            lastUpdateTime = elapsedTimeSeconds / DIFFICULTY_INTERVAL_SEC;

            spawnMultiplier *= 1.15;  // tăng 15% spawn rate mỗi phút
            speedMultiplier *= 1.1;   // tăng 10% tốc độ mỗi phút
            healthMultiplier *= 1.1;  // tăng 10% máu mỗi phút
        }
    }

    public double getSpawnMultiplier() {
        return spawnMultiplier;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public double getHealthMultiplier() {
        return healthMultiplier;
    }
}

