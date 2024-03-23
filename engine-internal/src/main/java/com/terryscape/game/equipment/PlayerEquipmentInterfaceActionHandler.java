package com.terryscape.game.equipment;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.cache.sound.SoundDefinition;
import com.terryscape.game.chat.PlayerChatSystem;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.game.item.ItemContainerItem;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.sound.SoundManager;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;

import java.nio.ByteBuffer;
import java.util.Set;

@Singleton
public class PlayerEquipmentInterfaceActionHandler implements InterfaceActionHandler {

    private final InterfaceManager interfaceManager;

    private final SoundManager soundManager;

    private final SoundDefinition equipGenericSoundDefinition;

    private final PlayerChatSystem playerChatSystem;

    @Inject
    public PlayerEquipmentInterfaceActionHandler(InterfaceManager interfaceManager,
                                                 SoundManager soundManager,
                                                 @Named("equip_generic") SoundDefinition equipGenericSoundDefinition,
                                                 PlayerChatSystem playerChatSystem) {

        this.interfaceManager = interfaceManager;
        this.soundManager = soundManager;
        this.equipGenericSoundDefinition = equipGenericSoundDefinition;
        this.playerChatSystem = playerChatSystem;
    }

    @Override
    public Set<String> getInterfaceId() {
        return Set.of("equipment", "character_equipment");
    }

    @Override
    public void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet) {
        var player = client.getPlayer().orElseThrow();

        if (!player.canDoActions()) {
            return;
        }

        if (interfaceId.equals("equipment")) {
            handleEquipmentInterfaceAction(player, interfaceAction, packet);
        } else if (interfaceId.equals("character_equipment")) {
            handleCharacterEquipmentInterfaceAction(player, interfaceAction);
        }
    }

    private void handleEquipmentInterfaceAction(PlayerComponent player, String interfaceAction, ByteBuffer packet) {
        var playerEquipment = player.getEquipment();
        var playerInventory = player.getInventory();

        if (interfaceAction.equals("show_character_equipment")) {
            interfaceManager.showInterface(player.getClient(), "character_equipment");
            return;
        }

        var slotNumber = IncomingPacket.readInt32(packet);
        var slot = EquipmentSlot.parseFromSlotId(slotNumber);

        var itemOptional = playerEquipment.getSlot(slot);
        if (itemOptional.isEmpty()) {
            return;
        }

        if (!playerInventory.hasFreeSlots(1)) {
            playerChatSystem.sendGameMessage(player, "You do not have the space to remove your %s.".formatted(itemOptional.get().getItemDefinition().getName()));
            return;
        }

        var item = itemOptional.get();
        if (interfaceAction.equals("item_remove")) {
            removeItem(player, item, slot);
        } else if (interfaceAction.equals("item_examine")) {
            examineItem(player, item);
        }
    }

    private void handleCharacterEquipmentInterfaceAction(PlayerComponent playerComponent, String interfaceAction) {
        if (interfaceAction.equals("close")) {
            interfaceManager.closeInterface(playerComponent.getClient(), "character_equipment");
        }
    }

    private void removeItem(PlayerComponent player, ItemContainerItem item, EquipmentSlot slot) {
        player.getEquipment().removeSlot(slot);
        player.getInventory().addItem(item.getItemDefinition(), item.getQuantity());
        soundManager.playSoundEffect(player.getClient(), equipGenericSoundDefinition);

        var taskComponent = player.getEntity().getComponentOrThrow(TaskComponent.class);
        taskComponent.cancelPrimaryTask();
    }

    private void examineItem(PlayerComponent player, ItemContainerItem item) {
        playerChatSystem.sendGameMessage(player, item.getItemDefinition().getDescription());
    }
}
