package content.devtools.commands;

import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.chat.command.Command;
import com.terryscape.game.chat.command.CommandArgumentDescription;
import com.terryscape.game.chat.command.CommandDescription;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.movement.MovementSpeed;
import com.terryscape.game.player.PlayerComponent;

import java.util.List;

@Singleton
public class SetRunCommand implements Command {

    @Override
    public String getPhrase() {
        return "set_run";
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of(
            "Set whether your player runs or not",
            CommandArgumentDescription.mandatory(Boolean.class, "run_enabled")
        );
    }

    @Override
    public void execute(PlayerComponent playerComponent, List<String> arguments) {
        var chat = playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        var movement = playerComponent.getEntity().getComponentOrThrow(MovementComponent.class);

        var runEnabled = Boolean.parseBoolean(arguments.get(0));

        if (runEnabled) {
            movement.setMovementSpeed(MovementSpeed.RUN);
            chat.sendGameMessage("You are now running.");
            return;
        }

        movement.setMovementSpeed(MovementSpeed.WALK);
        chat.sendGameMessage("You are now walking.");
    }
}
