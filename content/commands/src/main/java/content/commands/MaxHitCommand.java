package content.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.chat.command.Command;
import com.terryscape.game.chat.command.CommandDescription;
import com.terryscape.game.combat.CombatBonusesProviderComponent;
import com.terryscape.game.combat.CombatSkillsProviderComponent;
import com.terryscape.game.diceroll.CombatDiceRoll;
import com.terryscape.game.player.PlayerComponent;

import java.util.List;

@Singleton
public class MaxHitCommand implements Command {

    private final CombatDiceRoll combatDiceRoll;

    @Inject
    public MaxHitCommand(CombatDiceRoll combatDiceRoll) {
        this.combatDiceRoll = combatDiceRoll;
    }

    @Override
    public String getPhrase() {
        return "max_hit";
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of("Get your current max hit");
    }

    @Override
    public void execute(PlayerComponent playerComponent, List<String> arguments) {
        var playerSkills = playerComponent.getEntity().getComponentOrThrow(CombatSkillsProviderComponent.class);
        var playerBonuses = playerComponent.getEntity().getComponentOrThrow(CombatBonusesProviderComponent.class);
        var chat = playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class);

        var maxHit = combatDiceRoll.calculateMaxHit(playerSkills, playerBonuses);
        chat.sendGameMessage("Your current max hit is: %s.".formatted(maxHit));
    }
}
