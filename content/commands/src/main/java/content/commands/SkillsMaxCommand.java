package content.commands;

import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.chat.command.Command;
import com.terryscape.game.chat.command.CommandDescription;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.player.PlayerSkillsComponent;

import java.util.List;

@Singleton
public class SkillsMaxCommand implements Command {
    @Override
    public String getPhrase() {
        return "skills_max";
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of("Set all your levels to 30");
    }

    @Override
    public void execute(PlayerComponent playerComponent, List<String> arguments) {
        var skills = playerComponent.getEntity().getComponentOrThrow(PlayerSkillsComponent.class);
        skills.setAttack(30);
        skills.setDefence(30);
        skills.setStrength(30);

        playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class)
            .sendGameMessage("You have set your attack & strength & defence to 30.");
    }
}
