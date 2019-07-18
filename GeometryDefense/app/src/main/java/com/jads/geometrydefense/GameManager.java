package com.jads.geometrydefense;

import android.graphics.Point;
import android.util.Log;

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

public class GameManager {
    private static final GameManager ourInstance = new GameManager();

    private static final boolean debug = false;
    public static final int FPS = 60;

    private static List<List<Integer>> map;
    private List<Point> conceptPath = new ArrayList<>();

    private TurretFactory turretFactory;
    private EnemyFactory enemyFactory;

    public static GameManager getInstance() {
        return ourInstance;
    }

    private GameManager() {
        loadMap();
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


    private void loadMap() {
        map = Utils.create2DIntMatrixFromFile(MyApplication.getContext(), "GameMap.txt");
        Log.v("GameMap", Arrays.toString(map.toArray()));

        int m = map.size(), n = map.get(0).size();
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (map.get(i).get(j) == 1 && !visited[i][j]) {
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

    public static float getImageScaleCoefficient() {
        return map.get(0).size() * 1.071f;
    }

    public static boolean debugging() {
        return debug;
    }

    public List<Point> getPath() {
        return conceptPath;
    }

    public List<List<Integer>> getMap() {
        return map;
    }
}
