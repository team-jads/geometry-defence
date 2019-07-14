package com.jads.geometrydefense;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jads.geometrydefense.entities.attackers.TurretType;

public class GamePageActivity extends AppCompatActivity {

    private Button startGame;
    private GameBoardCanvas canvas;
    private ImageView basicTower;
    private ImageView movementSlowTower;
    private View towerMenu;
    private boolean gameStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
        startGame = findViewById(R.id.start);
        canvas = findViewById(R.id.game_view);
        basicTower = findViewById(R.id.basic_tower);
        movementSlowTower = findViewById(R.id.movement_slow_tower);
        towerMenu = findViewById(R.id.tower_menu);

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

        // TODO: Write a recycleview or listview here instead of setting of onclick for every tower
        basicTower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.onTowerSelected(TurretType.BASIC_TURRET);
            }
        });

        movementSlowTower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.onTowerSelected(TurretType.MOVEMENT_SLOW_TURRET);
            }
        });

    }


    public void focusOn() {
        towerMenu.setVisibility(View.VISIBLE);
    }

    public void focusOff() {
        towerMenu.setVisibility(View.GONE);
    }

}
