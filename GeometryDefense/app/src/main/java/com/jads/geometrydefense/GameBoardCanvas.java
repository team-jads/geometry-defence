package com.jads.geometrydefense;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.jads.geometrydefense.entities.labels.GameLevelLabel;
import com.jads.geometrydefense.entities.labels.GoldLabel;
import com.jads.geometrydefense.entities.labels.PlayerHealthLabel;
import com.jads.geometrydefense.entities.labels.ScoreLabel;
import com.jads.geometrydefense.entities.attackers.turrets.TurretType;
import com.jads.geometrydefense.entities.enemies.Enemy;
import com.jads.geometrydefense.entities.attackers.turrets.Turret;
import com.jads.geometrydefense.entities.attackers.TurretLand;
import com.jads.geometrydefense.entities.enemies.EnemyType;
import com.jads.geometrydefense.interfaces.CompoundGSprite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

import stanford.androidlib.AnimationTickListener;
import stanford.androidlib.SimpleActivity;
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
    private final Queue<GSprite> gameSprites = new ConcurrentLinkedDeque<>();
    private final ArrayList<GSprite> blackHoles = new ArrayList<>();

    private List<Point> realPath = new ArrayList<>();


    private TurretLand focusedTurretLand;

    private boolean isNewGame = true;
    private int enemyCount = 15;
    private int cellWidth = 0;
    private int cellHeight = 0;
    private int statusBarHeight = 0;
    Random rand = new Random();

    public GameBoardCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init() {
        setBackgroundColor(GColor.makeColor(72, 72, 72));
        gm = getInstance();
        gm.startNewGame();

        statusBarHeight = getHeight() / 8;
        createBorder();
        setUpNewGame();
    }

    public void setUpNewGame() {
        cleanUpBoard();

        if (gm.getCurrentLevel() == 1) {
            enemyCount = 15;
        } else if (gm.getCurrentLevel() == 2) {
            enemyCount = 20;
        } else {
            enemyCount = 25;
        }


        List<List<Integer>> map = gm.getMap();
        int rows = map.size();
        int cols = map.get(0).size();

        cellWidth = getWidth() / cols;
        cellHeight = getHeight() / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                float x = j * cellWidth;
                float y = i * cellWidth;
                int cur = map.get(i).get(j);
                if (GameManager.debugging()) {
                    GSprite rec = new GSprite(new GRect(x, y, cellWidth, cellWidth));
                    if (gm.getMap().get(i).get(j) == 1) {
                        rec.setFillColor(GColor.makeColor(255, 69, 0));
                    }
                    addSprite(rec);
                }


                if (cur == 0) {
                    TurretLand turretLand = new TurretLand(new GRect(x, y + statusBarHeight, cellWidth, cellWidth));
                    addSprite(turretLand);
                    turretLands.add(turretLand);
                } else if (cur == 2) {
                    SimpleBitmap bitmap = SimpleBitmap.with(this);
                    Bitmap scaled = bitmap.scaleToWidth(R.drawable.bh, getWidth() / GameManager.getImageScaleCoefficient());
                    GSprite sprite = new GSprite(scaled, x, y + statusBarHeight, cellWidth, cellWidth);
                    blackHoles.add(sprite);
                    addSprite(sprite);
                }
            }
        }

        List<Point> conceptPath = gm.getConceptPath();
        for (int i = 0; i < conceptPath.size(); i++) {
            int x = conceptPath.get(i).y * cellWidth;
            int y = conceptPath.get(i).x * cellWidth + statusBarHeight;
            realPath.add(new Point(x + cellWidth / 4, y + cellWidth / 4));
        }

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

    }

    private void cleanUpBoard() {
        realPath.clear();
        for (TurretLand turretLand : turretLands) {
            if (turretLand.isOccupied()) {
                turretLand.sellTurret();
            }
            remove(turretLand);
        }
        turretLands.clear();

        for (GSprite sprite : blackHoles) {
            remove(sprite);
        }
    }

    public void onTowerSelected(TurretType turretType) {
        if (!focusedTurretLand.isOccupied()) {
            if (gm.getTurretPrice(turretType) <= gm.getPlayerGold()) {
                gm.setPlayerGold(gm.getPlayerGold() - gm.getTurretPrice(turretType));
                Turret turret = gm.createTurret(turretType, this);
                turret.setLocation(focusedTurretLand.getX() + 10, focusedTurretLand.getY() + 10);
                turret.setEnemies(enemies);

                addSprite(turret);
                focusedTurretLand.setTurret(turret);
                if (GameManager.debugging()) {
                    float range = Turret.testingRange;
                    GOval oval = new GOval(focusedTurretLand.getCenterX() - range / 4, focusedTurretLand.getCenterY() - range / 4, range / 2, range / 2);
                    add(oval);
                }
            } else {
                Toast.makeText(getContext(), "Not Enough Gold", Toast.LENGTH_LONG).show();
            }

        }
        focusOff();

    }

    public void upgradeTower() {
        if (focusedTurretLand.isUpgradable() && gm.getPlayerGold() >= focusedTurretLand.getUpgradePrice()) {
            gm.setPlayerGold(gm.getPlayerGold() - focusedTurretLand.getUpgradePrice());
            focusedTurretLand.upgradeTower();
        }
        focusOff();
    }

    public void sellTower() {
        focusedTurretLand.sellTurret();
        focusOff();
    }

    private void focusOn(TurretLand land) {
        this.focusedTurretLand = land;
        land.focusOn();
        ((GamePageActivity) getActivity()).focusOn(land.isOccupied());
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


    private void createBorder() {
        GameLevelLabel levelLabel = new GameLevelLabel(new GLabel("Level: ", 60f, (float) statusBarHeight / 2 - 80f));
        PlayerHealthLabel playerHealthLabel = new PlayerHealthLabel(new GLabel("Health: ", getWidth() - 420f, (float) statusBarHeight / 2 - 80f));
        ScoreLabel scoreLabel = new ScoreLabel(new GLabel("Score: ", 60f, (float) statusBarHeight / 2));
        GoldLabel goldLabel = new GoldLabel(new GLabel("Gold: ", getWidth() - 420f, (float) statusBarHeight / 2));

        gm.addObservers(scoreLabel, goldLabel, levelLabel, playerHealthLabel);
        addSprite(scoreLabel, goldLabel, levelLabel, playerHealthLabel);
    }


    public void resumeGame() {
        if (isNewGame) {
            animate(GameManager.FPS, new AnimationTickListener() {
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
        int tickCount = getAnimationTickCount();
        if (tickCount % 30 == 0 && enemyCount != 0) {
            if (rand.nextBoolean()) {
                addNewEnemy(EnemyType.OVAL_ENEMY);
            } else {
                addNewEnemy(EnemyType.SQUARE_ENEMY);
            }

            enemyCount--;
        }
        for (GSprite sprite : gameSprites) {
            sprite.update();
        }
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    @Override
    public void remove(GObject obj) {
        super.remove(obj);
        gameSprites.remove(obj);
        if (obj instanceof Enemy) {
            enemies.remove(obj);
            if (enemies.size() == 0 && enemyCount == 0) {
                if (gm.isGameOver() || gm.getCurrentLevel() == 3) {
                    doGameOverShit();
                } else {
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    pauseGame();
                                }
                            },
                            100
                    );

                    gm.loadNextMap();
                    setUpNewGame();
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
//                                    setUpNewGame();
                                    resumeGame();
                                }
                            },
                            2000
                    );

                }
            } else {
                if (gm.isGameOver()) {
                    doGameOverShit();
                }
            }
        }
    }

    public void doGameOverShit() {
        pauseGame();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        String raw = preferences.getString("Score", "");
        raw += gm.getPlayerScore() + ",";
        editor.putString("Score", raw);
        editor.apply();

        new AlertDialog.Builder(getContext())
                .setTitle("Game Over!")
                .setCancelable(false)
                .setMessage("Your final score is: " + gm.getPlayerScore())
                .setPositiveButton("Leader Board", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent gamePage = new Intent(getContext(), ScoreActivity.class);
                        getContext().startActivity(gamePage);
                    }
                })

                .setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent gamePage = new Intent(getContext(), MainActivity.class);
                        getContext().startActivity(gamePage);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void addNewEnemy(EnemyType enemyType) {
        Enemy enemy = gm.createEnemy(enemyType, this);
        enemy.setEnemyPath(realPath);
        addSprite(enemy);
        enemies.add(enemy);
    }

    public void addSprite(GSprite... sprites) {
        for (GSprite sprite : sprites) {
            if (sprite instanceof CompoundGSprite) {
                for (GSprite child : ((CompoundGSprite) sprite).getCompoundChildren()) {
                    addSprite(child);
                }
            }
            gameSprites.add(sprite);
            add(sprite);
        }
    }
}
