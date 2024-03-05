package content.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.chat.command.Command;
import com.terryscape.game.chat.command.CommandArgumentDescription;
import com.terryscape.game.chat.command.CommandDescription;
import com.terryscape.game.player.PlayerComponent;

import java.util.List;

@Singleton
public class NpcStatsCommand implements Command {

    private final CacheLoader cacheLoader;

    @Inject
    public NpcStatsCommand(CacheLoader cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    @Override
    public String getPhrase() {
        return "npc_stats";
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of(
            "Get the stats of an npc",
            CommandArgumentDescription.mandatory(String.class, "npc_id")
        );
    }

    @Override
    public void execute(PlayerComponent playerComponent, List<String> arguments) {
        var chat = playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        var npcId = arguments.get(0);
        var npcOptional = cacheLoader.getNpcSafe(npcId);

        if (npcOptional.isEmpty()) {
            chat.sendGameMessage("No npc found with id %s.".formatted(npcId));
            return;
        }

        chat.sendGameMessage("Npc stats for %s".formatted(npcOptional.get().getName()));

        var stats = npcOptional.get().getStatsDefinition();
        chat.sendGameMessage("health=%s".formatted(stats.getHealth()));

        var skills = stats.getCombatSkillsDefinition();
        chat.sendGameMessage("attack=%s, strength=%s, defence=%s".formatted(skills.getAttack(), skills.getStrength(), skills.getDefence()));

        var bonuses = stats.getCombatBonusesDefinition();
        chat.sendGameMessage("offensive_stab=%s, offensive_slash=%s".formatted(bonuses.getOffensiveStab(), bonuses.getOffensiveSlash()));
        chat.sendGameMessage("defensive_stab=%s, defensive_slash=%s".formatted(bonuses.getDefensiveStab(), bonuses.getDefensiveSlash()));
    }
}
