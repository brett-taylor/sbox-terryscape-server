package content.devtools.commands;

import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.chat.command.Command;
import com.terryscape.game.chat.command.CommandArgumentDescription;
import com.terryscape.game.chat.command.CommandDescription;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.player.PlayerGender;

import java.util.List;

@Singleton
public class GenderCommand implements Command {

    @Override
    public String getPhrase() {
        return "gender";
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of("Change your player's gender", CommandArgumentDescription.mandatory(String.class, "male/female"));
    }

    @Override
    public void execute(PlayerComponent playerComponent, List<String> arguments) {
        var chat = playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        var gender = arguments.get(0);

        if (gender.equals("male")) {
            chat.sendGameMessage("You have swapped to a Male.");
            playerComponent.setGender(PlayerGender.MALE);
        }

        if (gender.equals("female")) {
            chat.sendGameMessage("You have swapped to a Female.");
            playerComponent.setGender(PlayerGender.FEMALE);
        }
    }
}
