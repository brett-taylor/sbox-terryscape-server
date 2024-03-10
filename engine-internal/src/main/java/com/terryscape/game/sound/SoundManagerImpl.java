package com.terryscape.game.sound;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.sound.SoundDefinition;
import com.terryscape.net.Client;
import com.terryscape.net.PacketManager;

@Singleton
public class SoundManagerImpl implements SoundManager {

    private final PacketManager packetManager;

    @Inject
    public SoundManagerImpl(PacketManager packetManager) {
        this.packetManager = packetManager;
    }

    @Override
    public void playSoundEffect(Client client, SoundDefinition soundDefinition) {
        var packet = new PlaySoundEffectOutgoingPacket(soundDefinition);
        packetManager.send(client, packet);
    }
}
