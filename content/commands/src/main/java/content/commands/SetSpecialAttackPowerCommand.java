package content.commands;

import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.chat.command.Command;
import com.terryscape.game.chat.command.CommandArgumentDescription;
import com.terryscape.game.chat.command.CommandDescription;
import com.terryscape.game.player.PlayerComponent;

import java.util.List;

@Singleton
public class SetSpecialAttackPowerCommand implements Command {
    @Override
    public String getPhrase() {
        return "set_spec";
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of(
            "Set your player's special attack power",
            CommandArgumentDescription.mandatory(Integer.class, "special_attack_power")
        );
    }

    @Override
    public void execute(PlayerComponent playerComponent, List<String> arguments) {
        var chat = playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        var newSpecialAttackPowerAmount = Math.clamp(Integer.parseInt(arguments.get(0)), 0, 100);

        playerComponent.setSpecialAttackPower(newSpecialAttackPowerAmount);
        chat.sendGameMessage("You have set your special attack power to %s.".formatted(newSpecialAttackPowerAmount));
    }
}
