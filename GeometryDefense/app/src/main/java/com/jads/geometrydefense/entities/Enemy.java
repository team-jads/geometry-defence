package com.jads.geometrydefense.entities;


import com.jads.geometrydefense.GameBoardCanvas;

import stanford.androidlib.graphics.GObject;

import stanford.androidlib.graphics.GSprite;

public class Enemy extends GSprite {
    boolean targeted = false;

    public Enemy(GObject turret, float dy) {
        super(turret);
        setVelocityY(dy);
    }

    public void getHit(Bullet bullet) {
        setVisible(false);
        ((GameBoardCanvas)getGCanvas()).remove(this);
    }

    public void setTargeted(boolean isTarget) {
        this.targeted = isTarget;
    }

    @Override
    public void update() {
        super.update();
    }
}
