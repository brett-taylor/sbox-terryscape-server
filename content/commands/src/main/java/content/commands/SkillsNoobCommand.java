package content.commands;

import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.chat.command.Command;
import com.terryscape.game.chat.command.CommandDescription;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.player.PlayerSkillsComponent;

import java.util.List;

@Singleton
public class SkillsNoobCommand implements Command {
    @Override
    public String getPhrase() {
        return "skills_noob";
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of("Set all your levels to 1");
    }

    @Override
    public void execute(PlayerComponent playerComponent, List<String> arguments) {
        var skills = playerComponent.getEntity().getComponentOrThrow(PlayerSkillsComponent.class);
        skills.setAttack(1);
        skills.setDefence(1);
        skills.setStrength(1);

        playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class)
            .sendGameMessage("You have set your attack & strength & defence to 1.");
    }
}
