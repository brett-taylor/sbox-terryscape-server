package com.terryscape.game.combat;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.equipment.EquipmentSlot;

public class SpecialBarImpl extends BaseEntityComponent {
    private int specialBar;
    private int timeSinceLastUpdate;
    private final int delayTime = 3;
    private EquipmentSlot specialAttackSlot = null;

    public SpecialBarImpl(Entity entity) {
        super(entity);
    }

    public boolean canRun(int specialRequired) {
        if(specialBar > specialRequired) {
            specialBar -= specialRequired;
            return true;
        }
        return false;
    }

    public void setSlot(EquipmentSlot slot) {
        specialAttackSlot = slot;
    }

    public EquipmentSlot getSlot(){
        var slotValue = specialAttackSlot;
        specialAttackSlot = null;
        return slotValue;
    }

    @Override
    public void tick() {
        if(timeSinceLastUpdate <= 0 && specialBar != 100) {
            timeSinceLastUpdate = delayTime;
            specialBar++;
        } else {
            timeSinceLastUpdate--;
        }
    }
}
