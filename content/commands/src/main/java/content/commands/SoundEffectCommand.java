package content.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.chat.command.Command;
import com.terryscape.game.chat.command.CommandArgumentDescription;
import com.terryscape.game.chat.command.CommandDescription;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.sound.SoundManager;

import java.util.List;

@Singleton
public class SoundEffectCommand implements Command {

    private final CacheLoader cacheLoader;

    private final SoundManager soundManager;

    @Inject
    public SoundEffectCommand(CacheLoader cacheLoader, SoundManager soundManager) {
        this.cacheLoader = cacheLoader;
        this.soundManager = soundManager;
    }

    @Override
    public String getPhrase() {
        return "sound_effect";
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of(
            "Play a sound effect",
            CommandArgumentDescription.mandatory(String.class, "sound_id")
        );
    }

    @Override
    public void execute(PlayerComponent playerComponent, List<String> arguments) {
        var chat = playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        var soundId = arguments.get(0);
        var soundOptional = cacheLoader.getSoundDefinitionSafe(soundId);

        if (soundOptional.isEmpty()) {
            chat.sendGameMessage("No sound found with id %s.".formatted(soundId));
            return;
        }

        chat.sendGameMessage("You've play sound %s.".formatted(soundId));
        soundManager.playSoundEffect(playerComponent.getClient(), soundOptional.get());
    }
}
