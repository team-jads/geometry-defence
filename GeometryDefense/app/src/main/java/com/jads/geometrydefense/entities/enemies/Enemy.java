package com.jads.geometrydefense.entities.enemies;

import android.graphics.Point;

import com.jads.geometrydefense.entities.ScoreLabel;
import com.jads.geometrydefense.interfaces.Damageable;

import java.util.List;

import stanford.androidlib.graphics.GObject;

import stanford.androidlib.graphics.GSprite;


public abstract class Enemy extends GSprite implements Damageable {

    protected int health;
    protected ScoreLabel label;
    protected List<Point> path;

    protected Point previousPoint;
    protected Point nextPoint;

    protected float movingProgress = 0;
    protected float movingSpeed = 0.02f;
    protected int counter = 1;

    public Enemy(GObject turret, int health, ScoreLabel label, List<Point> path) {
        super(turret);
        this.health = health;
        this.label = label;
        this.path = path;

        previousPoint = path.get(0);
        nextPoint = path.get(1);
//        if (this.health == 2) {
//            this.setFillColor(GColor.makeColor(255, 69, 0));
//        } else {
//            this.setFillColor(GColor.makeColor(255, 160, 122));
//        }

    }

//    public void getHit(Bullet bullet) {
//        if (--health <= 0) {
//            label.updateScore();
//            setVisible(false);
//            if (getGCanvas() != null) {
//                getGCanvas().remove(this);
//            }
//        } else {
//            this.setFillColor(GColor.makeColor(255, 160, 122));
//        }
//    }

    private void destoryMySelf() {
        setVisible(false);
        if (getGCanvas() != null) {
            getGCanvas().remove(this);
        }
    }

    @Override
    public void update() {
        super.update();
        if (movingProgress >= 1) {
            movingProgress = 0;
            previousPoint = nextPoint;
            if (counter + 1 != path.size()) {
                nextPoint = path.get(++counter);
            }

        } else {
            movingProgress += movingSpeed;
            float newX = (float) (nextPoint.x * movingProgress + previousPoint.x * (1.0 - movingProgress));
            float newY = (float) (nextPoint.y * movingProgress + previousPoint.y * (1.0 - movingProgress));
            setLocation(newX, newY);
            if (movingProgress >= 0.75f && counter + 1 == path.size()) {
                destoryMySelf();
            }
        }


    }
}
