package com.jads.geometrydefense.entities.enemies;

import android.graphics.Point;

import com.jads.geometrydefense.GameBoardCanvas;
import com.jads.geometrydefense.GameManager;
import com.jads.geometrydefense.entities.HealthBar;
import com.jads.geometrydefense.interfaces.CompoundGSprite;
import com.jads.geometrydefense.interfaces.Damageable;

import java.util.ArrayList;
import java.util.List;

import stanford.androidlib.graphics.GObject;

import stanford.androidlib.graphics.GSprite;


public abstract class Enemy extends GSprite implements Damageable, CompoundGSprite {

    protected GameManager gm;


    protected int currentHealth;
    protected int totalHealth;
    protected List<Point> path;

    protected Point previousPoint;
    protected Point nextPoint;

    protected float movingProgress = 0;
    protected float movingSpeed = 0.02f;
//        protected float movingSpeed = 0.1f;
    protected int counter = 1;

    protected HealthBar healthBar;

    public Enemy(GObject object) {
        super(object);
        healthBar = new HealthBar(this, totalHealth);
        gm = GameManager.getInstance();
    }

    public void initilizeHealth(int health) {
        this.totalHealth = health;
        this.currentHealth = health;
        healthBar.initilizeHealth(health);
    }


    @Override
    public void slowMovement(float slowPercentage) {
        movingSpeed = movingSpeed * slowPercentage;
    }

    @Override
    public GSprite setLocation(float x, float y) {
        healthBar.setLocation(x, y);
        return super.setLocation(x, y);
    }

    protected void destory() {
        destoryCompoundChildren();
        setVisible(false);
        if (getGCanvas() != null) {
            GameBoardCanvas g = (GameBoardCanvas) getGCanvas();
            g.remove(this);
        }
    }

    @Override
    public void destoryCompoundChildren() {
        healthBar.destory();
    }

    public void setEnemyPath(List<Point> path) {
        this.path = path;
        this.previousPoint = path.get(0);
        this.nextPoint = path.get(1);
    }

    @Override
    public List<GSprite> getCompoundChildren() {
        return new ArrayList<GSprite>() {{
            add(healthBar);
        }};
    }

    @Override
    public void receiveDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) {
            gm.onEnemyDeath(totalHealth);
            destory();
        } else {
            healthBar.updateHealth(currentHealth);
        }

    }

    @Override
    public void update() {
        super.update();
        if (movingProgress >= 1) {
            movingProgress -= 1;
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
                gm.onEnemyEscape(totalHealth);
                destory();
            }
        }


    }
}
