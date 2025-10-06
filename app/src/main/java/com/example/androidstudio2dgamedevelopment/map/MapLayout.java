package com.example.androidstudio2dgamedevelopment.map;

import android.content.Context;

import com.example.androidstudio2dgamedevelopment.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MapLayout {
    public static final int TILE_WIDTH_PIXELS = 64;
    public static final int TILE_HEIGHT_PIXELS = 64;
    public static final int NUMBER_OF_ROW_TILES = 60;
    public static final int NUMBER_OF_COLUMN_TILES = 60;

    private int[][] layout;

    // ⚠️ Thêm Context để load file raw
    public MapLayout(Context context) {
        initializeLayout(context);
    }

    public int[][] getLayout() {
        return layout;
    }

    private void initializeLayout(Context context) {
        layout = new int[NUMBER_OF_ROW_TILES][NUMBER_OF_COLUMN_TILES];
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getResources().openRawResource(R.raw.map_layout)))) {

            String line;
            int row = 0;

            while ((line = reader.readLine()) != null && row < NUMBER_OF_ROW_TILES) {
                String[] tokens = line.split(",");
                for (int col = 0; col < tokens.length && col < NUMBER_OF_COLUMN_TILES; col++) {
                    layout[row][col] = Integer.parseInt(tokens[col].trim());
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
