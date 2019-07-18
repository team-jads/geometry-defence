package com.jads.geometrydefense.entities.enemies;

import com.jads.geometrydefense.GameBoardCanvas;

import java.util.HashMap;

import stanford.androidlib.graphics.GOval;
import stanford.androidlib.graphics.GRect;

public class EnemyFactory {
    GameBoardCanvas canvas;
    private final HashMap<EnemyType, Integer> towerImageMap = new HashMap<EnemyType, Integer>() {{
//        put(TurretType.BASIC_TURRET, R.drawable.tower1);
//        put(TurretType.MOVEMENT_SLOW_TURRET, R.drawable.tower2);
    }};

    public EnemyFactory(GameBoardCanvas canvas) {
        this.canvas = canvas;
    }

    public Enemy createEnemy(EnemyType type) {
        Enemy enemy = null;
        int diameter = (int) (canvas.getCellWidth() / 1.5);
//        Log.v("Diameter: ",String.valueOf(diameter));
        if (type == EnemyType.OVAL_ENEMY) {
            enemy = new OvalEnemy(new GOval(diameter, diameter));
        } else if (type == EnemyType.SQUARE_ENEMY) {
            enemy = new SquareEnemy(new GRect(diameter, diameter));
        }

        return enemy;
    }
}
