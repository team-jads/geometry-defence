package com.jads.geometrydefense.entities.attackers;

import android.util.Log;

import com.jads.geometrydefense.GameManager;
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

    public int getUpgradePrice() {
        if (turret != null) {
            return turret.getUpgradePrice();
        }
        return Integer.MAX_VALUE;
    }

    public void upgradeTower() {
        if (turret != null) {
            turret.towerUpgrade();
        }
    }

    public boolean isUpgradable() {
        if (turret != null) {
            return turret.isUpgradable();
        } else {
            return false;
        }
    }

    public int getBuildPrice() {
        if (turret != null) {
            return turret.getBuildPrice();
        }
        return Integer.MAX_VALUE;
    }


    public int getSellingPrice() {
        if (turret != null) {
            return turret.getSellingPrice();
        }
        return 0;
    }


    public void sellTurret() {
        isOccupied = false;
        GameManager gm = GameManager.getInstance();
        gm.setPlayerGold(gm.getPlayerGold() + getSellingPrice());
        turret.destoryCompoundChildren();
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
