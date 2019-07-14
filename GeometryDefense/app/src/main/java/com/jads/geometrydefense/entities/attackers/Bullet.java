package com.jads.geometrydefense.entities.attackers;


import com.jads.geometrydefense.entities.enemies.Enemy;
import com.jads.geometrydefense.interfaces.Damager;

import java.util.ArrayList;

import stanford.androidlib.graphics.GColor;
import stanford.androidlib.graphics.GObject;
import stanford.androidlib.graphics.GSprite;

public class Bullet extends GSprite implements Damager {

    private int damage = 0;

    private Turret parent;
    private Enemy enemy;
    private ArrayList<BulletModifier> modifiers = new ArrayList<>();

    private Boolean isFired = false;

    public Bullet(GObject object, Turret parent) {
        super(object);
        this.parent = parent;
        this.setFillColor(GColor.BLACK);
        this.setLocation(object.getX(), object.getY());
        this.modifiers.add(new SlowModifier(this, 0.5f));
//        this.setVisible(false);
    }

    public void registerBulletModifier(BulletModifier modifier) {
        modifiers.add(modifier);
    }

    @Override
    public void dealDamage(Enemy enemy) {
        this.setVelocityX(15f);
        this.setVelocityY(15f);
        this.enemy = enemy;
        this.setVisible(true);
        this.isFired = true;

    }

//    public void fire(Enemy enemy) {
//        this.setVelocityX(15f);
//        this.setVelocityY(15f);
//        this.enemy = enemy;
//        this.setVisible(true);
//        this.isFired = true;
//        this.modifiers.add(new SlowModifier(this, 0.5f));
//    }

    private void reset() {
        this.setVisible(false);
        this.setLocation(parent.getX() + parent.getWidth(),
                parent.getY() + parent.getHeight() / 2 - 15f);
        this.isFired = false;
        this.enemy = null;
        parent.bullets.add(this);

    }

    @Override
    public void update() {
        if (isFired) {
            float distx = enemy.getX() - getX();
            float disty = enemy.getY() - getY();
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
