package com.jads.geometrydefense.entities.attackers.bullets;

import com.jads.geometrydefense.interfaces.Damager;

public class SlowModifier extends BulletModifier {
    private float slowPercentage;

    public SlowModifier(Damager modifiedDamager, float slowPercentage) {
        super(modifiedDamager);
        this.slowPercentage = slowPercentage;
    }

    @Override
    protected void modify() {
        this.enemy.slowMovement(slowPercentage);
    }
}
