package com.terryscape.game.sound;

import com.terryscape.cache.sound.SoundDefinition;
import com.terryscape.net.Client;

public interface SoundManager {

    void playSoundEffect(Client client, SoundDefinition soundDefinition);

}
