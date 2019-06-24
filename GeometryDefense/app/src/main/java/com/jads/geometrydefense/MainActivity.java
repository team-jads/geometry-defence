package com.jads.geometrydefense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startGameButton;
        Button findPlayerButton;
        Button scoreButton;

        startGameButton = findViewById(R.id.start_game);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gamePage = new Intent(MainActivity.this, GamePageActivity.class);
                startActivity(gamePage);
            }
        });

        findPlayerButton = findViewById(R.id.find_player);
        findPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findPlayerPage = new Intent(MainActivity.this, FindPlayerActivity.class);
                startActivity(findPlayerPage);
            }
        });

        scoreButton = findViewById(R.id.user_score);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scorePage = new Intent(MainActivity.this, ScoreActivity.class);
                startActivity(scorePage);
            }
        });

    }
}
