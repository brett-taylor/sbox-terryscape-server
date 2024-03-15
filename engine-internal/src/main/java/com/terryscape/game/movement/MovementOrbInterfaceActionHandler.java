package com.terryscape.game.movement;

import com.google.inject.Singleton;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.net.Client;

import java.nio.ByteBuffer;
import java.util.Set;

@Singleton
public class MovementOrbInterfaceActionHandler implements InterfaceActionHandler {

    @Override
    public Set<String> getInterfaceId() {
        return Set.of("movement_orb");
    }

    @Override
    public void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet) {
        var player = client.getPlayer().orElseThrow();
        var movement = player.getEntity().getComponentOrThrow(MovementComponent.class);

        var newMovementSpeed = movement.getMovementSpeed() == MovementSpeed.WALK ? MovementSpeed.RUN : MovementSpeed.WALK;
        movement.setMovementSpeed(newMovementSpeed);
    }
}
