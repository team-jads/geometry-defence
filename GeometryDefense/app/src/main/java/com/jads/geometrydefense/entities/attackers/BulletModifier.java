package com.jads.geometrydefense.entities.attackers;

import com.jads.geometrydefense.entities.enemies.Enemy;
import com.jads.geometrydefense.interfaces.Damageable;
import com.jads.geometrydefense.interfaces.Damager;

public abstract class BulletModifier implements Damager {

    protected Damager modifiedDamager;
    protected Damageable enemy;

    public BulletModifier(Damager modifiedDamager) {
        this.modifiedDamager = modifiedDamager;
    }

    @Override
    public void dealDamage(Enemy enemy) {
        if (modifiedDamager != null) {
            this.enemy = enemy;
            modify();
        }
    }

    protected abstract void modify();


}
