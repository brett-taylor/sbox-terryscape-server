package content.startingzone.npcs;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.component.EntityComponent;
import com.terryscape.game.npc.NpcOverheadTextComponent;
import com.terryscape.maths.RandomUtil;
import com.terryscape.world.WorldClock;

import java.util.function.Supplier;

// TODO: This probably could be swapped over to a task when we can have non-primary cancellable tasks.
public class RecurringNpcOverheadTextComponent extends BaseEntityComponent implements EntityComponent {

    private final WorldClock worldClock;

    private final int minWaitTicks;

    private final int maxWaitTicks;

    private final Supplier<String> messageSupplier;

    private float nextSayTick;

    public RecurringNpcOverheadTextComponent(Entity entity, WorldClock worldClock, int minWaitTicks, int maxWaitTicks, Supplier<String> messageSupplier) {
        super(entity);

        this.worldClock = worldClock;
        this.minWaitTicks = minWaitTicks;
        this.maxWaitTicks = maxWaitTicks;
        this.messageSupplier = messageSupplier;
    }

    @Override
    public void onRegistered() {
        super.onRegistered();

        resetTimer();
    }

    @Override
    public void tick() {
        super.tick();

        if (nextSayTick <= worldClock.getNowTick()) {
            resetTimer();

            var message = messageSupplier.get();
            getEntity().getComponentOrThrow(NpcOverheadTextComponent.class).say(message);
        }
    }

    private void resetTimer() {
        nextSayTick = worldClock.getNowTick() + RandomUtil.randomNumber(minWaitTicks, maxWaitTicks);
    }
}
