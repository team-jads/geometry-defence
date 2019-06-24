package com.jads.geometrydefense;

import android.content.Context;

import android.graphics.Bitmap;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jads.geometrydefense.entities.Bullet;
import com.jads.geometrydefense.entities.Enemy;
import com.jads.geometrydefense.entities.ScoreLabel;
import com.jads.geometrydefense.entities.Turret;
import com.jads.geometrydefense.entities.TurretLand;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import stanford.androidlib.AnimationTickListener;
import stanford.androidlib.SimpleBitmap;
import stanford.androidlib.graphics.GCanvas;
import stanford.androidlib.graphics.GColor;
import stanford.androidlib.graphics.GLabel;
import stanford.androidlib.graphics.GObject;
import stanford.androidlib.graphics.GOval;
import stanford.androidlib.graphics.GRect;
import stanford.androidlib.graphics.GSprite;


public class GameBoardCanvas extends GCanvas {
    private final HashSet<Enemy> enemies = new HashSet<>();
    private final ArrayList<TurretLand> turretLands = new ArrayList<>();
    private final ArrayList<GSprite> gameSprites = new ArrayList<>();

    private TurretLand focusedTurretLand;
    private ScoreLabel scoreLabel;

    private boolean isNewGame = true;
    private int enemyCount = 30;
    Random rand = new Random();

    public GameBoardCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init() {
        setBackgroundColor(GColor.makeColor(72, 72, 72));
        createBorder();
        createTurretBase();
        createScoreLabel();
    }

    private void createScoreLabel() {
        ScoreLabel label = new ScoreLabel(new GLabel("0", (float) getWidth() / 2, 10f));
        scoreLabel = label;
        addSprite(label);
    }

    public void onTowerSelected() {
        if (!focusedTurretLand.isOccupied()) {
            SimpleBitmap bitmap = SimpleBitmap.with(this);
            Bitmap scaled = bitmap.scaleToWidth(R.drawable.tower_icon, getWidth() / 6.5f);
            GSprite turretSprite = new GSprite(scaled,
                    focusedTurretLand.getX() + 15, focusedTurretLand.getY() + 15);
            turretSprite.setLocation(focusedTurretLand.getX() + 15, focusedTurretLand.getY() + 15);
            Turret turret = new Turret(turretSprite, enemies);

            Bullet bullet = new Bullet(new GOval(turret.getX() + turret.getWidth(),
                    turret.getY() + turret.getHeight() / 2 - 15f, 15f, 15f), turret);
            turret.addBullet(bullet);

            addSprite(turretSprite);
            addSprite(turret);
            addSprite(bullet);
            focusedTurretLand.setTurret(turret);
        }

    }

    private void focusOn(TurretLand land) {
        this.focusedTurretLand = land;
        land.focusOn();
        ((GamePageActivity) getActivity()).focusOn();
    }

    private void focusOff() {
        this.focusedTurretLand = null;
        for (TurretLand land : turretLands) {
            land.focusOff();
        }

        ((GamePageActivity) getActivity()).focusOff();
    }


    private Pair isTouchingTurretBase(float x, float y) {
        for (TurretLand land : turretLands) {
            if (land.contains(x, y)) {
                return Pair.create(true, land);
            }
        }
        return Pair.create(false, null);
    }


    private void createTurretBase() {
        int interval = getHeight() / 5;
        float landWidth = (float) (interval * 0.6);
        float margin = (float) (interval * 0.9);
        for (int i = 0; i < 5; i++) {
            TurretLand turretLand = new TurretLand(new GRect(40f, 180f + i * margin, landWidth, landWidth));
            addSprite(turretLand);
            turretLands.add(turretLand);
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        focusOff();
                        float x = event.getX();
                        float y = event.getY();
                        Pair pair = isTouchingTurretBase(x, y);
                        if ((boolean) pair.first) {
                            TurretLand land = (TurretLand) pair.second;
                            focusOn(land);
                        }
                    }
                    return false;
                }
            });
        }
    }

    private void createBorder() {
        GSprite topBorder = new GSprite(new GRect(0, 100f, getWidth(), 1));
        GSprite leftBorder = new GSprite(new GRect((float) getWidth() / 4, 100, 1, getHeight()));
        GSprite bottomBorder = new GSprite(new GRect(0, getHeight(), getWidth(), 1));
        addSprite(topBorder);
        addSprite(leftBorder);
        addSprite(bottomBorder);
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
        } else {
            animationResume();
        }

    }

    public void pauseGame() {
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
        Enemy enemy = new Enemy(new GOval(rand.nextInt(500) + 400f, 100f, 100f, 100f),
                4f, rand.nextInt(2) + 1, scoreLabel);
        addSprite(enemy);
        enemies.add(enemy);
    }

    private void addSprite(GSprite sprite) {
        gameSprites.add(sprite);
        add(sprite);
    }
}
