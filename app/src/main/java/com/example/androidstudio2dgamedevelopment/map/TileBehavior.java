package com.example.androidstudio2dgamedevelopment.map;

public interface TileBehavior {
    default boolean isBlocking() { return false; } // Có chặn di chuyển không
    default float getSpeedMultiplier() { return 1.0f; } // Giảm tốc độ nếu đi trên
    default int getDamagePerSecond() { return 0; } // Gây sát thương theo thời gian
}

