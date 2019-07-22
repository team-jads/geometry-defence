package com.jads.geometrydefense.entities.labels;

import com.jads.geometrydefense.GameManager;

import java.util.Observable;
import java.util.Observer;

import stanford.androidlib.graphics.GColor;
import stanford.androidlib.graphics.GLabel;
import stanford.androidlib.graphics.GSprite;

public class GameLevelLabel extends GSprite implements Observer {
    private int level = 1;
    private GLabel scoreLabel;

    public GameLevelLabel(GLabel scoreLabel) {
        super(scoreLabel);
        this.scoreLabel = scoreLabel;
        scoreLabel.setFontSize(80f);
        scoreLabel.setColor(GColor.WHITE);
    }


    @Override
    public void update() {
        this.scoreLabel.setText("Level: " + level);
        super.update();
    }

    @Override
    public void update(Observable observable, Object o) {
        GameManager gm = (GameManager)observable;
        level = gm.getGameLevel();
    }
}
