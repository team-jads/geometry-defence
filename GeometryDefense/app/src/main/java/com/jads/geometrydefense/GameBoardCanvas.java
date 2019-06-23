package com.jads.geometrydefense;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.media.Image;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jads.geometrydefense.entities.Bullet;
import com.jads.geometrydefense.entities.Enemy;
import com.jads.geometrydefense.entities.Turret;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;


import stanford.androidlib.AnimationTickListener;
import stanford.androidlib.graphics.GCanvas;
import stanford.androidlib.graphics.GObject;
import stanford.androidlib.graphics.GOval;
import stanford.androidlib.graphics.GRect;
import stanford.androidlib.graphics.GSprite;
import stanford.androidlib.graphics.GImage;

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
        // temporary towers - user need to add towers manually

        Turret turret = new Turret(new GRect(50f, 1900f, 100f, 200f), enemies);
        Turret turret1 = new Turret(new GRect(200f, 1900f, 100f, 200f), enemies);
        Turret turret2 = new Turret(new GRect(450f, 1900f, 100f, 200f), enemies);
        Turret turret3 = new Turret(new GRect(750f, 1900f, 100f, 200f), enemies);

        Bullet bullet = new Bullet(new GOval(turret.getX() + turret.getWidth() / 2 - 30f,
                turret.getY() -30f, 30f, 30f), turret);
        Bullet bullet1 = new Bullet(new GOval(turret1.getX() + turret1.getWidth() / 2 - 30f,
                turret1.getY() -30f, 30f, 30f), turret1);
        Bullet bullet2 = new Bullet(new GOval(turret2.getX() + turret2.getWidth() / 2 - 30f,
                turret2.getY() -30f, 30f, 30f), turret2);
        Bullet bullet3 = new Bullet(new GOval(turret3.getX() + turret3.getWidth() / 2 - 30f,
                turret3.getY() -30f, 30f, 30f), turret3);

        turret.addBullet(bullet);
        turret1.addBullet(bullet1);
        turret2.addBullet(bullet2);
        turret3.addBullet(bullet3);
        addSprite(turret);
        addSprite(turret1);
        addSprite(turret2);
        addSprite(turret3);
        addSprite(bullet);
        addSprite(bullet1);
        addSprite(bullet2);
        addSprite(bullet3);
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
        Enemy enemy = new Enemy(new GOval(rand.nextInt(500) + 400f, 0f, 100f, 100f), 5f);
        addSprite(enemy);
        enemies.add(enemy);
    }

    private void addSprite(GSprite sprite) {
        gameSprites.add(sprite);
        add(sprite);
    }
}
