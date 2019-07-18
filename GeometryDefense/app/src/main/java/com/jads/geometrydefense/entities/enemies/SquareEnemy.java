package com.jads.geometrydefense.entities.enemies;

import stanford.androidlib.graphics.GColor;
import stanford.androidlib.graphics.GObject;

public class SquareEnemy extends Enemy {
    public SquareEnemy(GObject object) {
        super(object);
        initilizeHealth(3);
        this.setFillColor(GColor.makeColor(204, 0, 102));
    }

}
