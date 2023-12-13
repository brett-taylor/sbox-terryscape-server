package content.devtools.commands;

import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.chat.command.Command;
import com.terryscape.game.chat.command.CommandArgumentDescription;
import com.terryscape.game.chat.command.CommandDescription;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.player.PlayerComponent;

import java.util.List;

@Singleton
public class SetHealthCommand implements Command {

    @Override
    public String getPhrase() {
        return "set_health";
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of("Set your player's health", CommandArgumentDescription.mandatory(Integer.class, "health"));
    }

    @Override
    public void execute(PlayerComponent playerComponent, List<String> arguments) {
        var chat = playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        var newHealth = Integer.parseInt(arguments.get(0));

        playerComponent.getEntity().getComponentOrThrow(HealthComponent.class).setHealth(newHealth);
        chat.sendGameMessage("You have set your health to %s.".formatted(newHealth));
    }
}
