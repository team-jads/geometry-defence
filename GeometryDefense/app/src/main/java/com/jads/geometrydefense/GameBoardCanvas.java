package com.jads.geometrydefense;

import android.content.Context;
import android.util.AttributeSet;

import com.jads.geometrydefense.entities.Bullet;
import com.jads.geometrydefense.entities.Enemy;
import com.jads.geometrydefense.entities.Turret;

import java.util.ArrayList;
import java.util.Random;

import stanford.androidlib.AnimationTickListener;
import stanford.androidlib.graphics.GCanvas;
import stanford.androidlib.graphics.GObject;
import stanford.androidlib.graphics.GOval;
import stanford.androidlib.graphics.GRect;
import stanford.androidlib.graphics.GSprite;

public class GameBoardCanvas extends GCanvas {
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<GSprite> gameSprites = new ArrayList<>();
    private boolean isNewGame = true;
    private int enemyCount = 20;
    Random rand = new Random();

    public GameBoardCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init() {
        Turret turret = new Turret(new GRect(50f, 400f, 200f, 200f), enemies);
        Bullet bullet = new Bullet(new GOval(turret.getX() + turret.getWidth(),
                turret.getY() + turret.getHeight() / 2 - 15f, 30f, 30f), turret);
        turret.addBullet(bullet);
        addSprite(turret);
        addSprite(bullet);
    }


    public void resumeGame() {
        if (isNewGame) {
            animate(60, new AnimationTickListener() {
                @Override
                public void onAnimateTick() {
                    canvasOnDraw();
                }
            });
            isNewGame = false;
        } else{
            animationResume();
        }

    }

    public void pauseGame(){
        animationPause();
    }

    private void canvasOnDraw() {
        if (getAnimationTickCount() % 60 == 0 && enemyCount != 0) {
            addNewEnemy();
            enemyCount--;
        }
        for (GSprite sprite : gameSprites) {
            sprite.update();
        }
    }

    @Override
    public void remove(GObject obj) {
        super.remove(obj);
        enemies.remove(obj);
    }

    private void addNewEnemy() {
        Enemy enemy = new Enemy(new GOval(rand.nextInt(500) + 400f, 0f, 100f, 100f), 4f);
        addSprite(enemy);
        enemies.add(enemy);
    }

    private void addSprite(GSprite sprite) {
        gameSprites.add(sprite);
        add(sprite);
    }
}
