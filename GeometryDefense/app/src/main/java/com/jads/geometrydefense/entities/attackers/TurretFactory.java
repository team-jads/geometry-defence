package com.jads.geometrydefense.entities.attackers;

import android.graphics.Bitmap;
import android.util.Log;

import com.jads.geometrydefense.GameManager;
import com.jads.geometrydefense.R;
import com.jads.geometrydefense.entities.attackers.turrets.BasicTurret;
import com.jads.geometrydefense.entities.attackers.turrets.MovementSlowTurret;
import com.jads.geometrydefense.entities.attackers.turrets.Turret;
import com.jads.geometrydefense.entities.attackers.turrets.TurretType;

import java.util.HashMap;

import stanford.androidlib.SimpleBitmap;
import stanford.androidlib.graphics.GCanvas;
import stanford.androidlib.graphics.GSprite;

public class TurretFactory {
    GCanvas canvas;
    private final HashMap<TurretType, Integer> towerImageMap = new HashMap<TurretType, Integer>() {{
        put(TurretType.BASIC_TURRET, R.drawable.tower1);
        put(TurretType.MOVEMENT_SLOW_TURRET, R.drawable.tower2);
    }};

    public TurretFactory(GCanvas canvas) {
        this.canvas = canvas;
    }

    public Turret createTurret(TurretType type) {

        SimpleBitmap bitmap = SimpleBitmap.with(canvas);
        Bitmap scaled = bitmap.scaleToWidth(towerImageMap.get(type), canvas.getWidth() / GameManager.getImageScaleCoefficient());
        Log.v("Img scale coefficient: ", String.valueOf(GameManager.getImageScaleCoefficient()));
        GSprite turretSprite = new GSprite(scaled);
        Turret turret;

        if (type == TurretType.BASIC_TURRET) {
            turret = new BasicTurret(turretSprite);
            return turret;
        } else if (type == TurretType.MOVEMENT_SLOW_TURRET) {
            turret = new MovementSlowTurret(turretSprite);
            return turret;
        } else {
            return null;
        }
    }

}
