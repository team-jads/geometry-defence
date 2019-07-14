package com.jads.geometrydefense;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.util.Pair;

import com.jads.geometrydefense.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameManager {
    private static final GameManager ourInstance = new GameManager();


    private List<List<Integer>> map;
    private List<Point> conceptPath = new ArrayList<>();

    public static GameManager getInstance() {
        return ourInstance;
    }

    private GameManager() {
        initMap();
    }

    private void initMap() {
        map = Utils.create2DIntMatrixFromFile(MyApplication.getContext(), "GameMap.txt");
        Log.v("GameMap", Arrays.toString(map.toArray()));

        int m = map.size(), n = map.get(0).size();
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (map.get(i).get(j) == 1 && !visited[i][j]) {
                    dfs(i, j, visited);
//                    conceptPath.add(new Point(i, j));
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

    public List<Point> getPath() {
        return conceptPath;
    }

    public List<List<Integer>> getMap() {
        return map;
    }
}
