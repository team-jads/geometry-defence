package com.jads.geometrydefense.entities.attackers.turrets;


import com.jads.geometrydefense.entities.attackers.bullets.Bullet;
import com.jads.geometrydefense.entities.attackers.bullets.SlowModifier;

import stanford.androidlib.graphics.GColor;
import stanford.androidlib.graphics.GObject;
import stanford.androidlib.graphics.GOval;

public class MovementSlowTurret extends Turret {
    public MovementSlowTurret(GObject turret) {
        super(turret);
        this.fireRate = 1;
        Bullet bullet = new Bullet(new GOval(getCenterX(),
                getCenterY(), 30f, 30f), this);
        bullet.setFillColor(GColor.makeColor(125, 200, 255));
        bullet.registerBulletModifier(new SlowModifier(bullet, 0.5f));
        bullet.setVelocityX(15f);
        bullet.setVelocityY(15f);
        bullet.setDamage(0);

        this.bulletPrototype = bullet;
        turretInit();
    }
}
