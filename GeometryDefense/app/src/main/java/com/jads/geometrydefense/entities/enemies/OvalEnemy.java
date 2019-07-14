package com.jads.geometrydefense.entities.enemies;

import android.graphics.Point;

import com.jads.geometrydefense.entities.ScoreLabel;

import java.util.List;

import stanford.androidlib.graphics.GColor;
import stanford.androidlib.graphics.GObject;

public class OvalEnemy extends Enemy {


    public OvalEnemy(GObject turret, int health, ScoreLabel label, List<Point> path) {
        super(turret, health, label, path);
        this.setFillColor(GColor.makeColor(255, 69, 0));
    }

    @Override
    public void receiveDamage(int damage) {
        if (--health <= 0) {
//            label.updateScore();
            setVisible(false);
            if (getGCanvas() != null) {
                getGCanvas().remove(this);
            }
        } else {
            this.setFillColor(GColor.makeColor(255, 160, 122));
        }


    }

    @Override
    public void slowMovement(float slowPercentage) {
        movingSpeed = movingSpeed * slowPercentage;
    }
}
