package com.jads.geometrydefense.entities.attackers;

import com.jads.geometrydefense.entities.attackers.turrets.Turret;

import stanford.androidlib.graphics.GColor;
import stanford.androidlib.graphics.GObject;
import stanford.androidlib.graphics.GSprite;

public class TurretLand extends GSprite {
    boolean isOccupied = false;
    private Turret turret;

    public TurretLand(GObject turretLand) {
        super(turretLand);
        setFillColor(GColor.makeColor(230, 230, 230));
        setColor(GColor.WHITE);
    }

    public Turret setTurret(Turret turret) {
        this.turret = turret;
        isOccupied = true;
        return this.turret;
    }

    public void focusOn() {
        setFillColor(GColor.LIGHT_GRAY);
    }

    public void focusOff() {
        setFillColor(GColor.makeColor(230, 230, 230));
    }

    public boolean isOccupied() {
        return isOccupied;
    }


}
