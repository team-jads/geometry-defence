package com.jads.geometrydefense.entities.attackers.turrets;

import android.graphics.Bitmap;

import com.jads.geometrydefense.GameManager;
import com.jads.geometrydefense.R;
import com.jads.geometrydefense.entities.attackers.bullets.Bullet;

import stanford.androidlib.SimpleBitmap;
import stanford.androidlib.graphics.GObject;
import stanford.androidlib.graphics.GOval;

public class BasicTurret extends Turret {
    public static int BUILD_PRICE = 10;
    public static int UPGRADE_PRICE = 5;
    public BasicTurret(GObject turret) {
        super(turret);
        this.range = 1500f;
        this.fireRate = 2;
        this.buildPrice = BUILD_PRICE;
        this.upgradePrice = UPGRADE_PRICE;
        this.bulletPrototype = new Bullet(new GOval(getX() + getWidth() / 2,
                getY() + getHeight() / 2, 15f, 15f), this);
        turretInit();
    }

    @Override
    public void towerUpgrade() {
        super.towerUpgrade();
        this.fireRate += 2;
        SimpleBitmap bitmap = SimpleBitmap.with(getGCanvas());
        Bitmap scaled = bitmap.scaleToWidth(R.drawable.tower1_upgrade, getGCanvas().getWidth() / GameManager.getImageScaleCoefficient());
        setBitmap(scaled);
    }
}
