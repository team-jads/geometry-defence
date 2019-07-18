package com.jads.geometrydefense.entities.enemies;

import stanford.androidlib.graphics.GColor;
import stanford.androidlib.graphics.GObject;

public class OvalEnemy extends Enemy {

    public OvalEnemy(GObject object) {
        super(object);
        initilizeHealth(2);
        this.setFillColor(GColor.makeColor(255, 69, 0));
    }
}
