package com.jads.geometrydefense;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jads.geometrydefense.entities.attackers.Bullet;
import com.jads.geometrydefense.entities.attackers.TurretType;
import com.jads.geometrydefense.entities.enemies.Enemy;
import com.jads.geometrydefense.entities.ScoreLabel;
import com.jads.geometrydefense.entities.attackers.Turret;
import com.jads.geometrydefense.entities.attackers.TurretLand;
import com.jads.geometrydefense.entities.enemies.OvalEnemy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

import static com.jads.geometrydefense.GameManager.getInstance;


public class GameBoardCanvas extends GCanvas {
    private GameManager gm;

    private final HashSet<Enemy> enemies = new HashSet<>();
    private final ArrayList<TurretLand> turretLands = new ArrayList<>();
    private final ArrayList<GSprite> gameSprites = new ArrayList<>();

    private List<Point> realPath = new ArrayList<>();


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
        gm = getInstance();
//        createBorder();

        int rows = gm.getMap().size();
        int cols = gm.getMap().get(0).size();

        int cellWidth = getWidth() / cols;
        int cellHeight = getHeight() / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                float x = j * cellWidth;
                float y = i * cellWidth;
                if (MyApplication.debugging()) {
                    GSprite rec = new GSprite(new GRect(x, y, cellWidth, cellWidth));
                    if (gm.getMap().get(i).get(j) == 1) {
                        rec.setFillColor(GColor.makeColor(255, 69, 0));
                    }
                    addSprite(rec);
                }


                if (gm.getMap().get(i).get(j) != 1) {
                    TurretLand turretLand = new TurretLand(new GRect(x, y, cellWidth, cellWidth));
                    addSprite(turretLand);
                    turretLands.add(turretLand);
                }
            }
        }

        List<Point> conceptPath = gm.getPath();
        for (int i = 0; i < conceptPath.size(); i++) {
            int x = conceptPath.get(i).y * cellWidth;
            int y = conceptPath.get(i).x * cellWidth;
            realPath.add(new Point(x + cellWidth / 4, y + cellWidth / 4));
        }

        // Extra path point to make enemy disappear
        realPath.add(new Point(realPath.get(realPath.size() - 1).x,
                (int) realPath.get(realPath.size() - 1).y + cellWidth));

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    focusOff();
                    float touchingX = event.getX();
                    float touchingY = event.getY();
                    Pair pair = isTouchingTurretBase(touchingX, touchingY);
                    if ((boolean) pair.first) {
                        TurretLand land = (TurretLand) pair.second;
                        focusOn(land);
                    }
                }
                return false;
            }
        });

//        GSprite topBorder = new GSprite(new GRect(0, 100f, getWidth(), 1));
//        GSprite leftBorder = new GSprite(new GRect((float) getWidth() / 4, 100, 1, getHeight()));
//        GSprite bottomBorder = new GSprite(new GRect(0, getHeight(), getWidth(), 1));
//        addSprite(topBorder, leftBorder, bottomBorder);
//        createTurretBase();
//        createScoreLabel();
    }

    private void createScoreLabel() {
        ScoreLabel label = new ScoreLabel(new GLabel("0", (float) getWidth() / 2, 10f));
        scoreLabel = label;
        addSprite(label);
    }

    public void onTowerSelected(TurretType turretType) {
        if (!focusedTurretLand.isOccupied()) {
            SimpleBitmap bitmap = SimpleBitmap.with(this);
            Bitmap scaled = bitmap.scaleToWidth(R.drawable.tower1, getWidth() / 7.5f);
            GSprite turretSprite = new GSprite(scaled,
                    focusedTurretLand.getX() + 10, focusedTurretLand.getY() + 10);
            turretSprite.setLocation(focusedTurretLand.getX() + 10, focusedTurretLand.getY() + 10);
            Turret turret = new Turret(turretSprite, enemies);


            Bullet bullet = new Bullet(new GOval(turret.getX() + turret.getWidth(),
                    turret.getY() + turret.getHeight() / 2 - 15f, 15f, 15f), turret);
            turret.addBullet(bullet);

            addSprite(turretSprite, turret, bullet);


            if (MyApplication.debugging()) {
                float range = 1500f;
                GRect rect = new GRect(focusedTurretLand.getCenterX() - range / 4, focusedTurretLand.getCenterY() - range / 4, range / 2, range / 2);
                add(rect);
            }

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
        }
        TurretLand turretLand2 = new TurretLand(new GRect(getWidth() - 340f, 300f, landWidth, landWidth));
        addSprite(turretLand2);
        turretLands.add(turretLand2);

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

    private void createBorder() {
        GSprite topBorder = new GSprite(new GRect(0, 100f, getWidth(), 1));
        GSprite leftBorder = new GSprite(new GRect((float) getWidth() / 4, 100, 1, getHeight()));
        GSprite bottomBorder = new GSprite(new GRect(0, getHeight(), getWidth(), 1));
        addSprite(topBorder, leftBorder, bottomBorder);
    }


    public void resumeGame() {
        if (isNewGame) {
            animate(120, new AnimationTickListener() {
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
//        Enemy enemy = new Enemy(new GOval(rand.nextInt(500) + 400f, 100f, 100f, 100f),
//                4f, rand.nextInt(2) + 1, scoreLabel);
        OvalEnemy enemy = new OvalEnemy(new GOval(100f, 100f), 2, scoreLabel, realPath);
        addSprite(enemy);
        enemies.add(enemy);
    }

    private void addSprite(GSprite... sprites) {
        for (GSprite sprite : sprites) {
            gameSprites.add(sprite);
            add(sprite);
        }
    }
}
