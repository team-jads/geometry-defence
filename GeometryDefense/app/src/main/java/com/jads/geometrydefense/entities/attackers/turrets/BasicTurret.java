package com.jads.geometrydefense.entities.attackers.turrets;

import com.jads.geometrydefense.entities.attackers.bullets.Bullet;

import stanford.androidlib.graphics.GObject;
import stanford.androidlib.graphics.GOval;

public class BasicTurret extends Turret {
    public BasicTurret(GObject turret) {
        super(turret);
        this.range = 1500f;
        this.fireRate = 2;
        this.bulletPrototype = new Bullet(new GOval(getX() + getWidth() / 2,
                getY() + getHeight() / 2, 15f, 15f), this);
        turretInit();
    }
}
