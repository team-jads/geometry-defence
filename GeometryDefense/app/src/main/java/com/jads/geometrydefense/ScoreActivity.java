package com.jads.geometrydefense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        EditText firstScore = findViewById(R.id.first_score);
        EditText secondScore = findViewById(R.id.second_score);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        String raw = preferences.getString("Score", "");
        List<Integer> ints = new ArrayList<>();
        for (String s : raw.trim().split(",")) {
            if (!s.isEmpty()) {
                Integer temp = Integer.parseInt(s);
                ints.add(temp);
            }

        }


        Collections.sort(ints, Collections.<Integer>reverseOrder());


        String newString = "";
        if (ints.size() > 0) {
            firstScore.setText(String.valueOf(ints.get(0)));
            newString += ints.get(0) + ",";
        }
        if (ints.size() > 1) {
            secondScore.setText(String.valueOf(ints.get(1)));
            newString += ints.get(1);
        }


        editor.putString("Score", newString);
        editor.apply();


        EditText menuText;

        menuText = findViewById(R.id.menu);
        menuText.setFocusable(false);
        menuText.setClickable(true);
        menuText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuPage = new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(menuPage);
            }
        });


    }
}
