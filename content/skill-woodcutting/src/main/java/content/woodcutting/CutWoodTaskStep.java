package content.woodcutting;

import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.equipment.EquipmentSlot;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.sound.SoundManager;
import com.terryscape.game.task.step.TaskStep;

import java.util.Objects;

public class CutWoodTaskStep extends TaskStep {

    private final PlayerComponent playerComponent;

    private final CacheLoader cacheLoader;

    private final ItemDefinition hatchet;

    private final ItemDefinition log;

    private final PlayerChatComponent playerChatComponent;

    private final AnimationComponent animationComponent;

    private final SoundManager soundManager;

    private boolean isFinished;

    private float tickCycle;

    public CutWoodTaskStep(PlayerComponent playerComponent, Tree tree, CacheLoader cacheLoader, SoundManager soundManager) {
        this.playerComponent = playerComponent;
        this.cacheLoader = cacheLoader;
        this.soundManager = soundManager;

        playerChatComponent = playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        animationComponent = playerComponent.getEntity().getComponentOrThrow(AnimationComponent.class);

        hatchet = cacheLoader.getItemDefinition("hatchet");
        log = cacheLoader.getItemDefinition(tree.getLogItemId());
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void firstTick() {
        super.firstTick();

        if (!playerComponent.getInventory().hasFreeSlots(1)) {
            playerChatComponent.sendGameMessage("You need at least one free inventory spot to chop down the tree..");
            isFinished = true;
            return;
        }

        var mainHand = playerComponent.getEquipment().getSlot(EquipmentSlot.MAIN_HAND);
        if (mainHand.isEmpty() || !Objects.equals(mainHand.get().getItemDefinition(), hatchet)) {
            playerChatComponent.sendGameMessage("You need a hatchet in your main hand to chop down the tree.");
            isFinished = true;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (isFinished) {
            return;
        }

        if (!playerComponent.getInventory().hasFreeSlots(1)) {
            isFinished = true;
            return;
        }

        if (tickCycle == 0) {
            soundManager.playSoundEffect(playerComponent.getClient(), cacheLoader.getSoundDefinition("skill_chop"));
            animationComponent.playAnimation("Sword_Attack_L3");
            tickCycle++;
        } else if (tickCycle == 1) {
            soundManager.playSoundEffect(playerComponent.getClient(), cacheLoader.getSoundDefinition("skill_chop"));
            animationComponent.playAnimation("Sword_Attack_L3");
            tickCycle++;
        } else if (tickCycle == 2) {
            playerComponent.getInventory().addItem(log, 1);
            tickCycle = 0;
        }
    }
}
