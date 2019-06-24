package com.jads.geometrydefense.entities;


import stanford.androidlib.graphics.GColor;
import stanford.androidlib.graphics.GObject;
import stanford.androidlib.graphics.GSprite;

public class Bullet extends GSprite {

    private Turret parent;
    private Enemy enemy;
    private Boolean isFired = false;

    public Bullet(GObject object, Turret parent) {
        super(object);
        this.parent = parent;
        this.setFillColor(GColor.BLACK);
        this.setLocation(object.getX(), object.getY());
        this.setVisible(false);
    }

    public void fire(Enemy enemy) {
        this.setVelocityX(15f);
        this.setVelocityY(15f);
        this.enemy = enemy;
        this.setVisible(true);
        this.isFired = true;
    }

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
                enemy.getHit(this);
                reset();
            }
        }

        super.update();
    }

}
