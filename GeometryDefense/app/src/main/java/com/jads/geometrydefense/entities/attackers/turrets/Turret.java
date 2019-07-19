package com.jads.geometrydefense.entities.attackers.turrets;

import android.graphics.Bitmap;
import android.util.Log;

import com.jads.geometrydefense.GameBoardCanvas;
import com.jads.geometrydefense.GameManager;
import com.jads.geometrydefense.entities.attackers.bullets.Bullet;
import com.jads.geometrydefense.entities.enemies.Enemy;
import com.jads.geometrydefense.interfaces.CompoundGSprite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import stanford.androidlib.graphics.GObject;
import stanford.androidlib.graphics.GOval;
import stanford.androidlib.graphics.GSprite;

public class Turret extends GSprite implements CompoundGSprite {
    public static float testingRange = 1500f;
    protected int fireRate = 1; // number of bullet/s
    protected boolean isUpgradable = true;
    protected Bullet bulletPrototype;
    protected float range = 1500f;
    protected int buildPrice = 0;
    protected int upgradePrice = 0;
    private float bulletFireProcess = 1;
    private float frameProgress = (float) fireRate / GameManager.FPS;
    private volatile boolean canFireNextBullet = true;
    private ArrayList<Bullet> bullets = new ArrayList<>();

    private GSprite rangeRec;

    private HashSet<Enemy> enemies;


    public Turret(GObject turret) {
        super(turret);
        this.rangeRec = new GSprite(new GOval(getCenterX() - range / 4,
                getCenterY() - range / 4, range / 2, range / 2));

    }

    public Turret(GObject turret, HashSet<Enemy> enemies) {
        this(turret);
        this.enemies = enemies;
    }


    protected void turretInit() {
        frameProgress = (float) fireRate / GameManager.FPS;
        for (int i = 0; i < fireRate; i++) {
            try {
                Bullet bullet = bulletPrototype.clone();
                bullets.add(bullet);
            } catch (CloneNotSupportedException e) {
                Log.e("Turret Class:", e.getMessage());
            }

        }
    }

    protected void upgradeTower() {
        if (isUpgradable) {
            isUpgradable = false;

        }
    }

    public boolean isUpgradable() {
        return isUpgradable;
    }

    public void recycleBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    @Override
    public GSprite setLocation(float x, float y) {
        this.rangeRec.setLocation(x + getWidth() / 2 - range / 4, y + getHeight() / 2 - range / 4);
        for (Bullet bullet : bullets) {
            bullet.setLocation(x + getWidth() / 2, y + getHeight() / 2);
        }
        return super.setLocation(x, y);
    }


    private boolean isInRange(GSprite other) {
        return rangeRec.collidesWith(other);
    }


    private void fire(Enemy enemy) {
        bullets.remove(bullets.size() - 1).dealDamage(enemy);
    }

    public void setEnemies(HashSet<Enemy> enemies) {
        this.enemies = enemies;
    }

    public int getUpgradePrice() {
        return upgradePrice;
    }

    public int getBuildPrice() {
        return buildPrice;
    }

    public int getSellingPrice() {
        return isUpgradable ? buildPrice / 2 : buildPrice;
    }


    private boolean okToFire() {
        return canFireNextBullet && bullets.size() > 0;
    }

    @Override
    public void update() {
        super.update();
        if (okToFire()) {
            for (Enemy enemy : enemies) {
                if (isInRange(enemy)) {
                    canFireNextBullet = false;
                    fire(enemy);
                    break;
                }
            }
        }

        if (!canFireNextBullet) {
            //            Log.v("Turret", "bulletFireProcess: " + bulletFireProcess + ". FrameProgress: " + frameProgress);
            bulletFireProcess -= frameProgress;
            if (bulletFireProcess < 0) {
                canFireNextBullet = true;
                bulletFireProcess = 1;
            }
        }
    }

    @Override
    public List<GSprite> getCompoundChildren() {
        List<GSprite> children = new ArrayList<>();
        children.addAll(bullets);
        return children;
    }

    @Override
    public void destoryCompoundChildren() {
        if (getGCanvas() != null) {
            GameBoardCanvas g = (GameBoardCanvas) getGCanvas();
            for (Bullet bullet : bullets) {
                bullet.destory();
            }
            g.remove(this);
        }

    }

    public void towerUpgrade() {
        isUpgradable = false;
    }
}
