package com.jads.geometrydefense.entities.labels;

import com.jads.geometrydefense.GameManager;

import java.util.Observable;
import java.util.Observer;

import stanford.androidlib.graphics.GLabel;
import stanford.androidlib.graphics.GSprite;

public class ScoreLabel extends GSprite implements Observer {
    private int score = 0;
    private GLabel scoreLabel;

    public ScoreLabel(GLabel scoreLabel) {
        super(scoreLabel);
        this.scoreLabel = scoreLabel;
        scoreLabel.setFontSize(80f);
    }


    @Override
    public void update() {
        this.scoreLabel.setText("Score: " + score);
        super.update();
    }

    @Override
    public void update(Observable observable, Object o) {
        GameManager gm = (GameManager) observable;
        score = gm.getPlayerScore();
    }
}
