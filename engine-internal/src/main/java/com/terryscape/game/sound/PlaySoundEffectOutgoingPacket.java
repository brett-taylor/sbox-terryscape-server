package com.terryscape.game.sound;

import com.terryscape.cache.sound.SoundDefinition;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

public class PlaySoundEffectOutgoingPacket implements OutgoingPacket {

    private final SoundDefinition soundDefinition;

    public PlaySoundEffectOutgoingPacket(SoundDefinition soundDefinition) {
        this.soundDefinition = soundDefinition;
    }

    @Override
    public String getPacketName() {
        return "server_client_play_sound_effect";
    }

    @Override
    public void writePacket(OutputStream packet) {
        OutgoingPacket.writeString(packet, soundDefinition.getId());
    }

}
