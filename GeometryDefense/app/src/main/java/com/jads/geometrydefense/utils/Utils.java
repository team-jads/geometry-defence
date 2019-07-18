package com.jads.geometrydefense.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<List<Integer>> create2DIntMatrixFromFile(Context context, String filename) {
        ArrayList<List<Integer>> matrix = new ArrayList<>();

        try {
            InputStream stream = context.getAssets().open(filename);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));

            String mLine = buffer.readLine();
            while (mLine != null) {
                ArrayList<Integer> newRow = new ArrayList<>();
                for (int col = 0; col < mLine.length(); col++) {
                    newRow.add(Integer.parseInt(String.valueOf(mLine.charAt(col))));
                }
                matrix.add(newRow);
                mLine = buffer.readLine();
            }
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }


        return matrix;
    }
}
