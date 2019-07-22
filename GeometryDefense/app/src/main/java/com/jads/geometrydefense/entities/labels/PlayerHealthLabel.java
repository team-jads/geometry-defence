package com.jads.geometrydefense.entities.labels;

import com.jads.geometrydefense.GameManager;

import java.util.Observable;
import java.util.Observer;

import stanford.androidlib.graphics.GColor;
import stanford.androidlib.graphics.GLabel;
import stanford.androidlib.graphics.GSprite;

public class PlayerHealthLabel extends GSprite implements Observer {
    private int health = 10;
    private GLabel scoreLabel;

    public PlayerHealthLabel(GLabel scoreLabel) {
        super(scoreLabel);
        this.scoreLabel = scoreLabel;
        scoreLabel.setFontSize(80f);
        scoreLabel.setColor(GColor.WHITE);
    }


    @Override
    public void update() {
        this.scoreLabel.setText("Health: " + health);
        super.update();
    }

    @Override
    public void update(Observable observable, Object o) {
        GameManager gm = (GameManager)observable;
        health = gm.getPlayerHealth();
    }
}
