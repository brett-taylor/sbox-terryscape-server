package content.food;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;

public class EatDelayComponent extends BaseEntityComponent {

    private final int delay;

    private int startingDelay;

    private boolean notifiedPlayer;

    public EatDelayComponent(Entity entity, int delay) {
        super(entity);
        this.delay = delay;
    }

    @Override
    public void onRegistered() {
        this.startingDelay = delay;
    }

    @Override
    public void tick() {
        super.tick();

        startingDelay -= 1;

        if (startingDelay <= 0) {
            removeFromEntity();
        }
    }

    public boolean hasNotifiedPlayer() {
        return notifiedPlayer;
    }

    public EatDelayComponent setNotifiedPlayer(boolean notifiedPlayer) {
        this.notifiedPlayer = notifiedPlayer;
        return this;
    }
}
