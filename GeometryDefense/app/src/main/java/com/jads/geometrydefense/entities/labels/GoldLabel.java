package com.jads.geometrydefense.entities.labels;

import com.jads.geometrydefense.GameManager;

import java.util.Observable;
import java.util.Observer;

import stanford.androidlib.graphics.GLabel;
import stanford.androidlib.graphics.GSprite;

public class GoldLabel extends GSprite implements Observer {
    private int gold = 20;
    private GLabel scoreLabel;

    public GoldLabel(GLabel scoreLabel) {
        super(scoreLabel);
        this.scoreLabel = scoreLabel;
        scoreLabel.setFontSize(80f);
    }


    @Override
    public void update() {
        this.scoreLabel.setText("Gold: " + gold);
        super.update();
    }

    @Override
    public void update(Observable observable, Object o) {
        GameManager gm = (GameManager)observable;
        gold = gm.getPlayerGold();
    }
}
