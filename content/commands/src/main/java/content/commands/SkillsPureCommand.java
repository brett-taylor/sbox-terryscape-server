package content.commands;

import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.chat.command.Command;
import com.terryscape.game.chat.command.CommandDescription;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.player.PlayerSkillsComponent;

import java.util.List;

@Singleton
public class SkillsPureCommand implements Command {
    @Override
    public String getPhrase() {
        return "skills_pure";
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of("Set all your levels to 30, and your defence to 1");
    }

    @Override
    public void execute(PlayerComponent playerComponent, List<String> arguments) {
        var skills = playerComponent.getEntity().getComponentOrThrow(PlayerSkillsComponent.class);
        skills.setAttack(30);
        skills.setDefence(1);
        skills.setStrength(30);
        skills.setMagic(30);
        skills.setRange(30);

        playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class)
            .sendGameMessage("You have set your skills to \"pure\".");
    }
}
