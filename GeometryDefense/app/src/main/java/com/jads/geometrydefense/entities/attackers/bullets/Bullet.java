package com.jads.geometrydefense.entities.attackers.bullets;


import com.jads.geometrydefense.GameBoardCanvas;
import com.jads.geometrydefense.entities.attackers.turrets.Turret;
import com.jads.geometrydefense.entities.enemies.Enemy;
import com.jads.geometrydefense.interfaces.Damager;

import java.util.ArrayList;

import stanford.androidlib.graphics.GColor;
import stanford.androidlib.graphics.GObject;
import stanford.androidlib.graphics.GSprite;

public class Bullet extends GSprite implements Damager, Cloneable {

    private int damage = 1;

    private Turret parent;
    private Enemy enemy;
    private ArrayList<BulletModifier> modifiers = new ArrayList<>();

    private Boolean isFired = false;

    public Bullet(GObject object, Turret parent) {
        super(object);
        this.parent = parent;
        this.setLocation(object.getX(), object.getY());
        this.setVisible(false);
        this.setFillColor(GColor.BLACK);
        this.setVelocityX(15f);
        this.setVelocityY(15f);
    }

    public Bullet(GObject object, Turret parent, BulletModifier modifier) {
        this(object, parent);
        modifiers.add(modifier);
    }

    public void registerBulletModifier(BulletModifier modifier) {
        modifiers.add(modifier);
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
    @Override
    public void dealDamage(Enemy enemy) {
        this.enemy = enemy;
        this.isFired = true;
//        this.setVisible(true);
    }

    @Override
    public Bullet clone() throws CloneNotSupportedException {
        return (Bullet) super.clone();
    }

    private void reset() {
        this.setLocation(parent.getX() + parent.getWidth() / 2,
                parent.getY() + parent.getHeight() / 2);
        this.setVisible(false);
        this.isFired = false;
        this.enemy = null;
        parent.recycleBullet(this);
    }

    public void destory() {
        this.setVisible(false);
        if (getGCanvas() != null) {
            GameBoardCanvas g = (GameBoardCanvas) getGCanvas();
            g.remove(this);
        }
    }

    @Override
    public void update() {
        if (isFired) {
            if (!this.isVisible()) {
                this.setVisible(true);
            }
            float distx = enemy.getCenterX() - getX();
            float disty = enemy.getCenterY() - getY();
            double angle = Math.atan2(disty, distx);
            float dx = (float) (getVelocityX() * Math.cos(angle));
            float dy = (float) (getVelocityY() * Math.sin(angle));

            translate(dx, dy);
            if (collidesWith(enemy)) {
                for (BulletModifier modifier : modifiers) {
                    modifier.dealDamage(enemy);
                }
                enemy.receiveDamage(damage);
                reset();
            }
        }
        super.update();
    }

}
