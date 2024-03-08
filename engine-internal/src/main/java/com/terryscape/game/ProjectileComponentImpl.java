package com.terryscape.game;

import com.terryscape.cache.item.WeaponDefinition;
import com.terryscape.cache.item.WeaponDefinitionImpl;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.ProjectileComponent;
import com.terryscape.game.combat.script.Combat;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateStep;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.world.WorldManager;
import com.terryscape.world.coordinate.WorldCoordinate;

import java.io.OutputStream;

public class ProjectileComponentImpl extends BaseEntityComponent implements ProjectileComponent {

    private WeaponDefinition weapon;
    private Entity attacker;

    public String getComponentIdentifier() {
        return "projectile_added";
    }

    private WorldCoordinate source, target;
    private String imgUrl = "";
    private int duration = 0;
    private int currentTick = 0;
    private final WorldManager worldManager;
    public ProjectileComponentImpl(Entity entity, WorldManager worldManager) {
        super(entity);
        this.worldManager = worldManager;
    }

    public void setAttacker(Entity attacker) {
        this.attacker = attacker;
    }
    public void setWeapon(WeaponDefinition weapon) {
        this.weapon = weapon;
    }
    public void setSource(WorldCoordinate source) {
        this.source = source;
    }
    public void setTarget(WorldCoordinate target) {
        this.target = target;
    }

    public void setImageUrl(String img) {
        imgUrl = img;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void tick() {
        super.tick();
        ++currentTick;
        if (currentTick > duration) {
            selfDestruct();
        } else if (currentTick == duration) {
            var targetEntity = worldManager.getEntity(target);
            targetEntity.ifPresent(entity -> Combat.slap(0, attacker, entity, weapon, true));
        }
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        source.writeToPacket(packet);
        target.writeToPacket(packet);
        OutgoingPacket.writeString(packet, imgUrl);
        OutgoingPacket.writeInt32(packet, duration);
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {
        OutgoingPacket.writeInt32(packet, currentTick);
    }


    private void selfDestruct() {
        getEntity().getComponentOrThrow(TaskComponent.class).setPrimaryTask(
                ImmediateStep.run(() -> worldManager.deleteEntity(getEntity().getIdentifier()))
        );
    }
}
