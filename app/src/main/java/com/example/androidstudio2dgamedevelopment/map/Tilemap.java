package com.example.androidstudio2dgamedevelopment.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.androidstudio2dgamedevelopment.GameDisplay;
import com.example.androidstudio2dgamedevelopment.graphics.SpriteSheet;

import static com.example.androidstudio2dgamedevelopment.map.MapLayout.NUMBER_OF_COLUMN_TILES;
import static com.example.androidstudio2dgamedevelopment.map.MapLayout.NUMBER_OF_ROW_TILES;
import static com.example.androidstudio2dgamedevelopment.map.MapLayout.TILE_HEIGHT_PIXELS;
import static com.example.androidstudio2dgamedevelopment.map.MapLayout.TILE_WIDTH_PIXELS;

public class Tilemap {

    private final MapLayout mapLayout;
    private Tile[][] tilemap;
    private SpriteSheet spriteSheet;
    private Bitmap mapBitmap;

    public Tilemap(Context context, SpriteSheet spriteSheet) {
        mapLayout = new MapLayout(context);
        this.spriteSheet = spriteSheet;
        initializeTilemap();
    }

    private void initializeTilemap() {
        int[][] layout = mapLayout.getLayout();
        tilemap = new Tile[NUMBER_OF_ROW_TILES][NUMBER_OF_COLUMN_TILES];
        for (int iRow = 0; iRow < NUMBER_OF_ROW_TILES; iRow++) {
            for (int iCol = 0; iCol < NUMBER_OF_COLUMN_TILES; iCol++) {
                tilemap[iRow][iCol] = Tile.getTile(
                    layout[iRow][iCol],
                    spriteSheet,
                    getRectByIndex(iRow, iCol)
                );
            }
        }

        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        mapBitmap = Bitmap.createBitmap(
                NUMBER_OF_COLUMN_TILES*TILE_WIDTH_PIXELS,
                NUMBER_OF_ROW_TILES*TILE_HEIGHT_PIXELS,
                config
        );

        Canvas mapCanvas = new Canvas(mapBitmap);

        for (int iRow = 0; iRow < NUMBER_OF_ROW_TILES; iRow++) {
            for (int iCol = 0; iCol < NUMBER_OF_COLUMN_TILES; iCol++) {
                tilemap[iRow][iCol].draw(mapCanvas);
            }
        }

    }

    private Rect getRectByIndex(int idxRow, int idxCol) {
        return new Rect(
                idxCol*TILE_WIDTH_PIXELS,
                idxRow*TILE_HEIGHT_PIXELS,
                (idxCol + 1)*TILE_WIDTH_PIXELS,
                (idxRow + 1)*TILE_HEIGHT_PIXELS
        );
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        int mapWidth = getMapWidth();
        int mapHeight = getMapHeight();

        Rect gameRect = gameDisplay.getGameRect();

        // Vẽ map chính
        canvas.drawBitmap(mapBitmap, gameRect, gameDisplay.DISPLAY_RECT, null);

        // Vẽ thêm 8 map phụ (loop)
        int[] offsetX = {-mapWidth, 0, mapWidth};
        int[] offsetY = {-mapHeight, 0, mapHeight};

        for (int dx : offsetX) {
            for (int dy : offsetY) {
                if (dx == 0 && dy == 0) continue;
                Rect shiftedRect = new Rect(
                        gameRect.left - dx,
                        gameRect.top - dy,
                        gameRect.right - dx,
                        gameRect.bottom - dy
                );
                canvas.drawBitmap(mapBitmap, shiftedRect, gameDisplay.DISPLAY_RECT, null);
            }
        }
    }

    public int getMapWidth() {
        return MapLayout.NUMBER_OF_COLUMN_TILES * MapLayout.TILE_WIDTH_PIXELS;
    }

    public int getMapHeight() {
        return MapLayout.NUMBER_OF_ROW_TILES * MapLayout.TILE_HEIGHT_PIXELS;
    }

    public Tile getTileAt(int row, int col) {
        return tilemap[row][col];
    }

}
