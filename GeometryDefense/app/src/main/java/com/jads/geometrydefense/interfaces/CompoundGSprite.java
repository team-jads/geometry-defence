package com.jads.geometrydefense.interfaces;

import java.util.List;

import stanford.androidlib.graphics.GSprite;

public interface CompoundGSprite {
    List<GSprite> getCompoundChildren();
    void destoryCompoundChildren();
}
