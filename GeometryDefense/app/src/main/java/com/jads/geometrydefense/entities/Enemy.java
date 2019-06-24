package com.jads.geometrydefense.entities;

import stanford.androidlib.graphics.GColor;
import stanford.androidlib.graphics.GObject;

import stanford.androidlib.graphics.GSprite;


public class Enemy extends GSprite {

    private int health;
    private ScoreLabel label;

    public Enemy(GObject turret, float dy, int health, ScoreLabel label) {
        super(turret);
        setVelocityY(dy);
        this.health = health;
        this.label = label;
        if (this.health == 2) {
            this.setFillColor(GColor.makeColor(255, 69, 0));
        } else {
            this.setFillColor(GColor.makeColor(255, 160, 122));
        }

    }

    public void getHit(Bullet bullet) {
        if (--health <= 0) {
            label.updateScore();
            setVisible(false);
            if (getGCanvas() != null) {
                getGCanvas().remove(this);
            }
        } else {
            this.setFillColor(GColor.makeColor(255, 160, 122));
        }
    }

    @Override
    public void update() {
        super.update();
    }
}
