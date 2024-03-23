package com.terryscape.game.grounditem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.cache.sound.SoundDefinition;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.ComponentSystem;
import com.terryscape.game.sound.SoundManager;

import java.io.OutputStream;

@Singleton
public class GroundItemComponentSystem extends ComponentSystem<GroundItemComponent> {

    private final SoundManager soundManager;

    private final SoundDefinition equipGenericSoundDefinition;

    @Inject
    public GroundItemComponentSystem(SoundManager soundManager, @Named("equip_generic") SoundDefinition equipGenericSoundDefinition) {
        this.soundManager = soundManager;
        this.equipGenericSoundDefinition = equipGenericSoundDefinition;
    }

    @Override
    public Class<GroundItemComponent> forComponentType() {
        return GroundItemComponent.class;
    }

    @Override
    public boolean isNetworked() {
        return true;
    }

    @Override
    public String getComponentNetworkIdentifier() {
        return "component_ground_item";
    }

    @Override
    public void writeEntityAddedPacket(Entity entity, GroundItemComponent component, OutputStream packet) {
        component.getItemContainerItem().writeToPacket(packet);
        component.getWorldCoordinate().writeToPacket(packet);
    }

    @Override
    public void onTick(Entity entity, GroundItemComponent component) {
        if (component.getTakenBy() == null) {
            return;
        }

        var itemContainer = component.getItemContainerItem();
        component.getTakenBy().getInventory().addItem(itemContainer.getItemDefinition(), itemContainer.getQuantity());

        soundManager.playSoundEffect(component.getTakenBy().getClient(), equipGenericSoundDefinition);

        entity.delete();
    }
}
