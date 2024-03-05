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
public class ItemStatsCommand implements Command {

    private final CacheLoader cacheLoader;

    @Inject
    public ItemStatsCommand(CacheLoader cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    @Override
    public String getPhrase() {
        return "item_stats";
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of(
            "Get the stats of an item",
            CommandArgumentDescription.mandatory(String.class, "item_id")
        );
    }

    @Override
    public void execute(PlayerComponent playerComponent, List<String> arguments) {
        var chat = playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        var itemId = arguments.get(0);
        var itemOptional = cacheLoader.getItemSafe(itemId);

        if (itemOptional.isEmpty()) {
            chat.sendGameMessage("No item found with id %s.".formatted(itemId));
            return;
        }

        chat.sendGameMessage("Item stats for %s".formatted(itemOptional.get().getName()));

        var stats = itemOptional.get().getItemStatsDefinition();
        chat.sendGameMessage("weight=%s".formatted(stats.getWeight()));
        chat.sendGameMessage("offensive_stab=%s, offensive_slash=%s".formatted(stats.getOffensiveStab(), stats.getOffensiveSlash()));
        chat.sendGameMessage("defensive_stab=%s, defensive_slash=%s".formatted(stats.getDefensiveStab(), stats.getDefensiveSlash()));
    }
}
