package content.commands;

import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.chat.command.Command;
import com.terryscape.game.chat.command.CommandDescription;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.player.PlayerComponent;

import java.util.List;

@Singleton
public class CoordinatesCommand implements Command {

    @Override
    public String getPhrase() {
        return "coordinates";
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of("Get your player's current world coordinates");
    }

    @Override
    public void execute(PlayerComponent playerComponent, List<String> arguments) {
        var chat = playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        var movement = playerComponent.getEntity().getComponentOrThrow(MovementComponent.class);

        chat.sendGameMessage("%s facing %s".formatted(movement.getWorldCoordinate(), movement.getDirection()));
    }
}
