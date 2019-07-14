package com.jads.geometrydefense.entities.attackers;

import com.jads.geometrydefense.entities.enemies.Enemy;

import java.util.ArrayList;
import java.util.HashSet;

import stanford.androidlib.graphics.GObject;
import stanford.androidlib.graphics.GRect;
import stanford.androidlib.graphics.GSprite;

public class Turret extends GSprite {
    float range = 2000f;
    float rate = 1; // 1 bullet/s
    ArrayList<Bullet> bullets = new ArrayList<>();

    private GSprite rangeRec;


    private HashSet<Enemy> enemies;


    public Turret(GObject turret) {
        super(turret);
        this.rangeRec = new GSprite(new GRect(getCenterX() - range / 4, getCenterY() - range / 4, range / 2, range / 2));
    }

    public Turret(GObject turret, HashSet<Enemy> enemies) {
        this(turret);
        this.enemies = enemies;
    }


    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }


    public boolean isInRange(GSprite other) {
        return rangeRec.collidesWith(other);
    }


    private void fire(Enemy enemy) {
        Bullet bullet = bullets.remove(bullets.size() - 1);
        bullet.dealDamage(enemy);
    }

    public void setEnemies(HashSet<Enemy> enemies) {
        this.enemies = enemies;
    }

    @Override
    public void update() {
        super.update();
        for (Enemy enemy : enemies) {
            if (isInRange(enemy) && getGCanvas().getAnimationTickCount() % 60 == 0 && bullets.size() != 0) {
                fire(enemy);
            }
        }
    }
}
