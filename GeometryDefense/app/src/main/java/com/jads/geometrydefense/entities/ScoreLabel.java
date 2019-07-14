package com.jads.geometrydefense.entities;

import stanford.androidlib.graphics.GLabel;
import stanford.androidlib.graphics.GSprite;

public class ScoreLabel extends GSprite {
    private int score = 0;
    private GLabel scoreLabel;

    public ScoreLabel(GLabel scoreLabel) {
        super(scoreLabel);
        this.scoreLabel = scoreLabel;
        scoreLabel.setFontSize(100f);
    }

    public void updateScore() {
        score++;
    }

    public int getScore() {
        return score;
    }


    @Override
    public void update() {
        this.scoreLabel.setText(String.valueOf(score));
        super.update();
    }
}
