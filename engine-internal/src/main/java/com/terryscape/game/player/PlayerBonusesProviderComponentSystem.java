package com.terryscape.game.player;

import com.google.inject.Singleton;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.ComponentSystem;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

@Singleton
public class PlayerBonusesProviderComponentSystem extends ComponentSystem<PlayerBonusesProviderComponentImpl> {

    @Override
    public Class<PlayerBonusesProviderComponentImpl> forComponentType() {
        return PlayerBonusesProviderComponentImpl.class;
    }

    @Override
    public boolean isNetworked() {
        return true;
    }

    @Override
    public String getComponentNetworkIdentifier() {
        return "component_player_bonuses_provider";
    }

    @Override
    public void writeEntityAddedPacket(Entity entity, PlayerBonusesProviderComponentImpl component, OutputStream packet) {
        writePacket(component, packet);
    }

    @Override
    public void writeEntityUpdatedPacket(Entity entity, PlayerBonusesProviderComponentImpl component, OutputStream packet) {
        writePacket(component, packet);
    }

    private void writePacket(PlayerBonusesProviderComponentImpl component, OutputStream packet) {
        OutgoingPacket.writeFloat(packet, component.getWeight());

        OutgoingPacket.writeFloat(packet, component.getOffensiveStab());
        OutgoingPacket.writeFloat(packet, component.getOffensiveSlash());
        OutgoingPacket.writeFloat(packet, component.getOffensiveAir());
        OutgoingPacket.writeFloat(packet, component.getOffensiveFire());
        OutgoingPacket.writeFloat(packet, component.getOffensiveArrow());

        OutgoingPacket.writeFloat(packet, component.getDefensiveStab());
        OutgoingPacket.writeFloat(packet, component.getDefensiveSlash());
        OutgoingPacket.writeFloat(packet, component.getDefensiveAir());
        OutgoingPacket.writeFloat(packet, component.getDefensiveFire());
        OutgoingPacket.writeFloat(packet, component.getDefensiveArrow());

        OutgoingPacket.writeFloat(packet, component.getStrengthMelee());
        OutgoingPacket.writeFloat(packet, component.getStrengthMagic());
        OutgoingPacket.writeFloat(packet, component.getStrengthRange());
    }
}
