package com.jads.geometrydefense.entities.attackers.turrets;


import android.graphics.Bitmap;

import com.jads.geometrydefense.GameManager;
import com.jads.geometrydefense.R;
import com.jads.geometrydefense.entities.attackers.bullets.Bullet;
import com.jads.geometrydefense.entities.attackers.bullets.SlowModifier;

import stanford.androidlib.SimpleBitmap;
import stanford.androidlib.graphics.GColor;
import stanford.androidlib.graphics.GObject;
import stanford.androidlib.graphics.GOval;

public class MovementSlowTurret extends Turret {
    public static int BUILD_PRICE = 10;
    public static int UPGRADE_PRICE = 5;

    public MovementSlowTurret(GObject turret) {
        super(turret);
        this.fireRate = 1;
        this.buildPrice = BUILD_PRICE;
        this.upgradePrice = UPGRADE_PRICE;
        Bullet bullet = new Bullet(new GOval(getCenterX(),
                getCenterY(), 30f, 30f), this);
        bullet.setFillColor(GColor.makeColor(125, 200, 255));
        bullet.registerBulletModifier(new SlowModifier(bullet, 0.5f));
        bullet.setVelocityX(15f);
        bullet.setVelocityY(15f);
        bullet.setDamage(1);

        this.bulletPrototype = bullet;
        turretInit();
    }

    @Override
    public void towerUpgrade() {
        super.towerUpgrade();
        this.fireRate += 1;
        SimpleBitmap bitmap = SimpleBitmap.with(getGCanvas());
        Bitmap scaled = bitmap.scaleToWidth(R.drawable.tower2_upgrade, getGCanvas().getWidth() / GameManager.getImageScaleCoefficient());
        setBitmap(scaled);
    }
}
