package com.terryscape.game.specialattack;

import com.google.inject.Singleton;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.net.Client;

import java.nio.ByteBuffer;
import java.util.Set;

@Singleton
public class SpecialAttackOrbInterfaceActionHandler implements InterfaceActionHandler {

    @Override
    public Set<String> getInterfaceId() {
        return Set.of("special_attack_orb");
    }

    @Override
    public void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet) {
        var player = client.getPlayer().orElseThrow();
        player.setWantsToSpecialAttack(!player.wantsToSpecialAttack());
    }
}
