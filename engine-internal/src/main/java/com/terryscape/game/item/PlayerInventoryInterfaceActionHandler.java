package com.terryscape.game.item;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.cache.item.WeaponItemDefinition;
import com.terryscape.cache.sound.SoundDefinition;
import com.terryscape.entity.EntityPrefabFactory;
import com.terryscape.game.chat.PlayerChatSystem;
import com.terryscape.game.equipment.EquipmentSlot;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.sound.SoundManager;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;
import com.terryscape.entity.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.Set;

@Singleton
public class PlayerInventoryInterfaceActionHandler implements InterfaceActionHandler {

    private static final Logger LOGGER = LogManager.getLogger(PlayerInventoryInterfaceActionHandler.class);

    private final ItemInteractionDispatcher itemInteractionDispatcher;

    private final SoundManager soundManager;

    private final EntityPrefabFactory entityPrefabFactory;

    private final EntityManager entityManager;

    private final SoundDefinition equipGenericSoundDefinition;

    private final PlayerChatSystem playerChatSystem;

    @Inject
    public PlayerInventoryInterfaceActionHandler(ItemInteractionDispatcher itemInteractionDispatcher,
                                                 SoundManager soundManager,
                                                 EntityPrefabFactory entityPrefabFactory,
                                                 EntityManager entityManager,
                                                 @Named("equip_generic") SoundDefinition equipGenericSoundDefinition,
                                                 PlayerChatSystem playerChatSystem) {

        this.itemInteractionDispatcher = itemInteractionDispatcher;
        this.equipGenericSoundDefinition = equipGenericSoundDefinition;
        this.soundManager = soundManager;
        this.entityPrefabFactory = entityPrefabFactory;
        this.entityManager = entityManager;
        this.playerChatSystem = playerChatSystem;
    }

    @Override
    public Set<String> getInterfaceId() {
        return Set.of("inventory");
    }

    @Override
    public void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet) {
        var slotNumber = IncomingPacket.readInt32(packet);
        var modifyAction = IncomingPacket.readBoolean(packet);

        var player = client.getPlayer().orElseThrow();
        var playerInventory = player.getInventory();

        var itemOptional = playerInventory.getItemAt(slotNumber);
        if (itemOptional.isEmpty()) {
            return;
        }

        var item = itemOptional.get();

        if (interfaceAction.equals("examine")) {
            playerChatSystem.sendGameMessage(player, item.getItemDefinition().getDescription());
        }

        if (!player.canDoActions()) {
            return;
        }

        if (interfaceAction.equals("equip")) {
            equipItem(modifyAction, player, slotNumber, item);
        }

        if (interfaceAction.equals("interact")) {
            itemInteractionDispatcher.dispatchItemInteraction(client, slotNumber);
        }

        if (interfaceAction.equals("drop")) {
            dropItem(player, item, slotNumber);
        }

        if (interfaceAction.equals("swap")) {
            var slotB = IncomingPacket.readInt32(packet);
            swapItems(player, slotNumber, slotB);
        }
    }

    private void equipItem(boolean modifyAction, PlayerComponent playerComponent, int slotNumber, ItemContainerItem item) {
        var equipOptional = item.getItemDefinition().getEquipDefinition();
        if (equipOptional.isEmpty()) {
            LOGGER.warn("Player {} attempted to equip item {} which can not be equipped", playerComponent.getUsername(), item.getItemDefinition().getId());
            return;
        }

        var slot = equipOptional.get().getEquipmentSlot();
        var isTwoHanded = equipOptional.get().getWeaponDefinition().map(WeaponItemDefinition::isTwoHanded).orElse(false);
        if (slot == EquipmentSlot.MAIN_HAND && modifyAction && !isTwoHanded) {
            slot = EquipmentSlot.OFF_HAND;
        }

        if (isTwoHanded && !playerComponent.getInventory().hasFreeSlots(1)) {
            playerChatSystem.sendGameMessage(playerComponent, "You do not have the inventory space to equip your %s.".formatted(item.getItemDefinition().getName()));
            return;
        }

        var mainHand = playerComponent.getEquipment().getSlot(EquipmentSlot.MAIN_HAND);

        var previouslyEquippedItem = playerComponent.getEquipment().getSlot(slot);

        playerComponent.getInventory().removeItemAt(slotNumber);
        playerComponent.getEquipment().setSlot(slot, item.getItemDefinition(), item.getQuantity());

        previouslyEquippedItem.ifPresent(itemContainerItem -> playerComponent.getInventory().addItemAt(slotNumber, itemContainerItem.getItemDefinition(), 1));

        soundManager.playSoundEffect(playerComponent.getClient(), equipGenericSoundDefinition);

        if (slot == EquipmentSlot.OFF_HAND) {
            if (mainHand.isPresent() && mainHand.get().getItemDefinition().getEquipDefinitionOrThrow().getWeaponDefinitionOrThrow().isTwoHanded()) {
                var mainHandItem = mainHand.get();
                playerComponent.getEquipment().removeSlot(EquipmentSlot.MAIN_HAND);
                playerComponent.getInventory().addItemAt(slotNumber, mainHandItem.getItemDefinition(), mainHandItem.getQuantity());
            }
        }

        if (isTwoHanded) {
            var previousEquippedOffhandItem = playerComponent.getEquipment().getSlot(EquipmentSlot.OFF_HAND);
            playerComponent.getEquipment().removeSlot(EquipmentSlot.OFF_HAND);
            previousEquippedOffhandItem.ifPresent(itemContainerItem -> playerComponent.getInventory().addItem(itemContainerItem.getItemDefinition(), 1));
        }

        var taskComponent = playerComponent.getEntity().getComponentOrThrow(TaskComponent.class);
        taskComponent.cancelPrimaryTask();
    }

    private void dropItem(PlayerComponent playerComponent, ItemContainerItem itemContainerItem, int slotNumber) {
        playerComponent.getInventory().removeItemAt(slotNumber);
        var currentWorldCoordinate = playerComponent.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();

        var groundItem = entityPrefabFactory.createGroundItemPrefab(itemContainerItem, currentWorldCoordinate);
        entityManager.registerEntity(groundItem);

        soundManager.playSoundEffect(playerComponent.getClient(), equipGenericSoundDefinition);

        var taskComponent = playerComponent.getEntity().getComponentOrThrow(TaskComponent.class);
        taskComponent.cancelPrimaryTask();
    }

    private void swapItems(PlayerComponent playerComponent, int slotA, int slotB) {
        var slotAItem = playerComponent.getInventory().getItemAt(slotA);
        var slotBItem = playerComponent.getInventory().getItemAt(slotB);

        playerComponent.getInventory().removeItemAt(slotA);
        playerComponent.getInventory().removeItemAt(slotB);

        slotAItem.ifPresent(itemContainerItem -> playerComponent.getInventory().addItemAt(slotB, itemContainerItem.getItemDefinition(), itemContainerItem.getQuantity()));
        slotBItem.ifPresent(itemContainerItem -> playerComponent.getInventory().addItemAt(slotA, itemContainerItem.getItemDefinition(), itemContainerItem.getQuantity()));

        soundManager.playSoundEffect(playerComponent.getClient(), equipGenericSoundDefinition);

        var taskComponent = playerComponent.getEntity().getComponentOrThrow(TaskComponent.class);
        taskComponent.cancelPrimaryTask();
    }
}
