package com.jads.geometrydefense.entities;

import android.util.Log;

import com.jads.geometrydefense.GameBoardCanvas;
import com.jads.geometrydefense.interfaces.CompoundGSprite;

import java.util.ArrayList;
import java.util.List;

import stanford.androidlib.graphics.GColor;
import stanford.androidlib.graphics.GRect;
import stanford.androidlib.graphics.GSprite;

public class HealthBar extends GSprite implements CompoundGSprite {
    private GSprite frame;
    private GSprite fill;
    private GSprite parent;

    int curentHealth = 0;
    int totalHealth = 0;
    float originalWidth = 0;
    float originalHeight = 0;

    public HealthBar(GSprite parent, int health) {
        this.parent = parent;
        curentHealth = health;
        totalHealth = health;
        originalWidth = parent.getWidth();
        originalHeight = parent.getHeight() / 6;
        frame = new GSprite(new GRect(parent.getX(), originalWidth, parent.getWidth(), originalHeight));
        frame.setColor(GColor.BLACK);
        fill = new GSprite(new GRect(parent.getX(), originalWidth, parent.getWidth(), originalHeight));
        fill.setFillColor(GColor.GREEN);
    }

    public void destory() {
        destoryCompoundChildren();
    }

    public void initilizeHealth(int health) {
        this.totalHealth = health;
        this.curentHealth = health;
    }

    public void updateHealth(int health) {
        this.curentHealth = health;
        float percentage = (float) curentHealth / totalHealth;
        float newWidth = originalWidth * percentage;
        fill.setSize(newWidth, originalHeight);
    }

    @Override
    public GSprite setLocation(float x, float y) {
        frame.setLocation(x, y - 10);
        fill.setLocation(x, y - 10);
        return super.setLocation(x, y);
    }

    @Override
    public List<GSprite> getCompoundChildren() {
        return new ArrayList<GSprite>() {{
            add(fill);
            add(frame);
        }};
    }

    @Override
    public void destoryCompoundChildren() {
        setVisible(false);
        fill.setVisible(false);
        frame.setVisible(false);
        if (getGCanvas() != null) {
            GameBoardCanvas g = (GameBoardCanvas) getGCanvas();
            g.remove(fill);
            g.remove(frame);
            g.remove(this);
        }
    }

    @Override
    public void update() {
        super.update();
    }
}
