package com.jads.geometrydefense.entities.attackers;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.jads.geometrydefense.R;

import java.util.HashMap;
import java.util.Map;

import stanford.androidlib.SimpleBitmap;
import stanford.androidlib.graphics.GCanvas;
import stanford.androidlib.graphics.GSprite;

public class TurretFactory {
    GCanvas canvas;
    private static final HashMap<TurretType, Integer> towerImageMap = new HashMap<TurretType, Integer>() {{
        put(TurretType.BASIC_TURRET, R.drawable.tower1);
        put(TurretType.MOVEMENT_SLOW_TURRET, R.drawable.tower2);
    }};

    public TurretFactory(GCanvas canvas) {
        this.canvas = canvas;
    }

    public Turret createTurret(TurretType type, int x, int y) {

        SimpleBitmap bitmap = SimpleBitmap.with(canvas);
        Bitmap scaled = bitmap.scaleToWidth(towerImageMap.get(type), canvas.getWidth() / 7.5f);
        GSprite turretSprite = new GSprite(scaled,
                x + 10, y + 10);
        turretSprite.setLocation(x + 10, y + 10);
        Turret turret = new Turret(turretSprite);

        if (type == TurretType.BASIC_TURRET) {

            return turret;

        } else if (type == TurretType.MOVEMENT_SLOW_TURRET) {
            return turret;
        } else {
            return null;
        }
    }

}
