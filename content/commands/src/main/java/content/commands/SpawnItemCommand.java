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
public class SpawnItemCommand implements Command {

    private final CacheLoader cacheLoader;

    @Inject
    public SpawnItemCommand(CacheLoader cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    @Override
    public String getPhrase() {
        return "item";
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of(
            "Spawn a Item",
            CommandArgumentDescription.mandatory(String.class, "item_id"),
            CommandArgumentDescription.optional(Integer.class, "amount")
        );
    }

    @Override
    public void execute(PlayerComponent playerComponent, List<String> arguments) {
        var chat = playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class);
        var itemId = arguments.get(0);
        var itemOptional = cacheLoader.getItemDefinitionSafe(itemId);

        var amount = arguments.size() >= 2 ? Integer.parseInt(arguments.get(1)) : 1;
        amount = Integer.max(amount, 1);

        if (itemOptional.isPresent()) {
            playerComponent.getInventory().addItem(itemOptional.get(), amount);
            chat.sendGameMessage("You spawned %s x%s.".formatted(itemOptional.get().getName(), amount));
        } else {
            chat.sendGameMessage("No item found with id %s.".formatted(itemId));
        }
    }
}
