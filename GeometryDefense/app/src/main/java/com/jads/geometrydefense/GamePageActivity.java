package com.jads.geometrydefense;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GamePageActivity extends AppCompatActivity {

    private Button startGame;
    private GameBoardCanvas canvas;
    private boolean gameStarted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
        startGame = findViewById(R.id.start);
        canvas = findViewById(R.id.game_view);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameStarted) {
                    canvas.resumeGame();
                    gameStarted = !gameStarted;
                    startGame.setText("Pause");
                } else {
                    canvas.pauseGame();
                    gameStarted = !gameStarted;
                    startGame.setText("Start");
                }
            }
        });
    }


}
