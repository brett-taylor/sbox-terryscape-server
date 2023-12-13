package content.devtools.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.chat.command.Command;
import com.terryscape.game.chat.command.CommandArgumentDescription;
import com.terryscape.game.chat.command.CommandDescription;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.player.PlayerComponent;

import java.util.List;

@Singleton
public class AnimationCommand implements Command {

    private final CacheLoader cacheLoader;

    @Inject
    public AnimationCommand(CacheLoader cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    @Override
    public String getPhrase() {
        return "animation";
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of(
            "Force your player to do an animation",
            CommandArgumentDescription.mandatory(String.class, "anim_id")
        );
    }

    @Override
    public void execute(PlayerComponent playerComponent, List<String> arguments) {
        var chat = playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        var animation = playerComponent.getEntity().getComponentOrThrow(AnimationComponent.class);

        animation.playAnimation(arguments.get(0));
        chat.sendGameMessage("Played animation %s".formatted(arguments.get(0)));
    }
}
