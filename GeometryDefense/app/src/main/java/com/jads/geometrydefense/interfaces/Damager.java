package com.jads.geometrydefense.interfaces;

import com.jads.geometrydefense.entities.enemies.Enemy;

public interface Damager {
    // Besides "Dealing" damage, it also applies modifier on the enemy (movement slow etc)
    void dealDamage(Enemy enemy);

}
