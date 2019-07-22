package com.jads.geometrydefense;

import android.graphics.Point;
import android.util.Log;

import com.jads.geometrydefense.entities.attackers.turrets.BasicTurret;
import com.jads.geometrydefense.entities.attackers.turrets.MovementSlowTurret;
import com.jads.geometrydefense.entities.attackers.turrets.Turret;
import com.jads.geometrydefense.entities.attackers.TurretFactory;
import com.jads.geometrydefense.entities.attackers.turrets.TurretType;
import com.jads.geometrydefense.entities.enemies.Enemy;
import com.jads.geometrydefense.entities.enemies.EnemyFactory;
import com.jads.geometrydefense.entities.enemies.EnemyType;
import com.jads.geometrydefense.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GameManager extends Observable {
    private static final GameManager ourInstance = new GameManager();

    private static final boolean debug = false;
    public static final int FPS = 60;

    private int gameLevel = 0;
    private int maxLevel = 3;
    private int playerInitialGold = 20;
    private int playerGold = 35;
    private int playerScore = 0;
    private int playerHealth = 10;
    private int playerInitialHealth = 10;

    private static List<List<Integer>> map;
    private List<Point> conceptPath = new ArrayList<>();

    private TurretFactory turretFactory;
    private EnemyFactory enemyFactory;

    public static GameManager getInstance() {
        return ourInstance;
    }

    private GameManager() {
    }

    public Turret createTurret(TurretType turretType, GameBoardCanvas canvas) {
        if (turretFactory == null) {
            turretFactory = new TurretFactory(canvas);
        }
        return turretFactory.createTurret(turretType);
    }

    public Enemy createEnemy(EnemyType enemyType, GameBoardCanvas canvas) {
        if (enemyFactory == null) {
            enemyFactory = new EnemyFactory(canvas);
        }
        return enemyFactory.createEnemy(enemyType);
    }


    private void loadMap(int gameLevel) {
        map = Utils.create2DIntMatrixFromFile(MyApplication.getContext(), "map_level_" + gameLevel + ".txt");
        Log.v("gameMap: ", Arrays.toString(map.toArray()));

        int m = map.size(), n = map.get(0).size();
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (map.get(i).get(j) == 2 && !visited[i][j]) {
                    dfs(i, j, visited);
                }
            }
        }

        Log.v("path", conceptPath.toString());
    }

    private void dfs(int i, int j, boolean[][] visited) {
        if (i < 0 || j < 0 || i >= map.size() || j >= map.get(0).size() || visited[i][j] || map.get(i).get(j) == 0) {
            return;
        }
        conceptPath.add(new Point(i, j));
        visited[i][j] = true;
        dfs(i + 1, j, visited);
        dfs(i - 1, j, visited);
        dfs(i, j + 1, visited);
        dfs(i, j - 1, visited);
    }

    public void onEnemyDeath(int damage) {
        playerScore += damage;
        playerGold += damage / 2;
        setChanged();
        notifyObservers();
    }

    public void onEnemyEscape(int damage) {
        playerHealth -= damage;
        playerHealth = Math.max(playerHealth, 0);
        setChanged();
        notifyObservers();
    }

    public int getTurretPrice(TurretType turretType) {
        if (turretType == TurretType.BASIC_TURRET) {
            return BasicTurret.BUILD_PRICE;
        } else if (turretType == TurretType.MOVEMENT_SLOW_TURRET) {
            return MovementSlowTurret.BUILD_PRICE;
        } else {
            return 0;
        }
    }

    public static float getImageScaleCoefficient() {
        return map.get(0).size() * 1.071f;
    }

    public static boolean debugging() {
        return debug;
    }

    public List<Point> getConceptPath() {
        return conceptPath;
    }

    public List<List<Integer>> getMap() {
        return map;
    }

    public void addObservers(Observer... observers) {
        for (Observer observer : observers) {
            addObserver(observer);
        }
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public int getGameLevel() {
        return gameLevel;
    }

    public int getPlayerGold() {
        return playerGold;
    }

    public void setPlayerGold(int newPlayerGold) {
        this.playerGold = newPlayerGold;
        setChanged();
        notifyObservers();
    }

    public boolean purchase(int amount) {
        if (amount <= this.playerGold) {
            setPlayerGold(this.playerGold - amount);
            return true;
        }
        return false;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public boolean isGameOver() {
        return playerHealth == 0 || gameLevel > maxLevel;
    }

    public int getCurrentLevel() {
        return gameLevel;
    }

    public void loadNextMap() {
        resetMap();
        gameLevel++;
        loadMap(gameLevel);
        setChanged();
        notifyObservers();
    }

    private void resetMap() {
        if (map != null) {
            map.clear();
        }
        conceptPath.clear();

    }

    public void startNewGame() {
        resetMap();
        playerGold = playerInitialGold;
        playerHealth = playerInitialHealth;
        playerScore = 0;
        gameLevel = 0;
        loadNextMap();
    }
}
